/*
 * Copyright 2006-2009 Luca Garulli (luca.garulli--at--assetdata.it)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.romaframework.aspect.hook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.romaframework.aspect.hook.annotation.HookScope;
import org.romaframework.aspect.hook.feature.HookActionFeatures;
import org.romaframework.aspect.hook.feature.HookFieldFeatures;
import org.romaframework.aspect.session.SessionInfo;
import org.romaframework.core.Roma;
import org.romaframework.core.aspect.Aspect;
import org.romaframework.core.config.RomaApplicationContext;
import org.romaframework.core.config.Serviceable;
import org.romaframework.core.flow.Controller;
import org.romaframework.core.flow.FieldRefreshListener;
import org.romaframework.core.flow.SchemaActionListener;
import org.romaframework.core.flow.SchemaFieldListener;
import org.romaframework.core.module.SelfRegistrantModule;
import org.romaframework.core.schema.Feature;
import org.romaframework.core.schema.SchemaAction;
import org.romaframework.core.schema.SchemaClass;
import org.romaframework.core.schema.SchemaClassDefinition;
import org.romaframework.core.schema.SchemaClassElement;
import org.romaframework.core.schema.SchemaEvent;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.schema.SchemaObject;

/**
 * Aspect that intercepts events against POJO and call the registrant.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public class HookAspect extends SelfRegistrantModule implements Aspect, FieldRefreshListener, SchemaActionListener, SchemaFieldListener {

	public static final String							ASPECT_NAME		= "hook";
	public static final String							WILDCARD_ANY	= "*";

	protected Map<String, List<HookEntry>>	exactHooks		= new HashMap<String, List<HookEntry>>();
	protected Map<String, List<HookEntry>>	wildcardHooks	= new HashMap<String, List<HookEntry>>();
	protected static Log										log						= LogFactory.getLog(HookAspect.class);

	public HookAspect() {
		Controller.getInstance().registerListener(SchemaActionListener.class, this);
		Controller.getInstance().registerListener(FieldRefreshListener.class, this);
		Controller.getInstance().registerListener(SchemaFieldListener.class, this);
	}

	public void startup() throws RuntimeException {
	}

	public void shutdown() throws RuntimeException {
	}

	@Override
	public void showConfiguration() {
	}

	public void beginConfigClass(SchemaClassDefinition iClass) {
	}

	public void endConfigClass(SchemaClassDefinition iClass) {
	}

	public void configClass(SchemaClassDefinition iClass) {
	}

	public void configField(SchemaField iField) {

		HookScope scope = iField.getFeature(HookFieldFeatures.SCOPE);
		if (scope != null)
			scope = HookScope.SESSION;

		registerHook(iField, HookFieldFeatures.FIELD, scope);

	}

	public void configAction(SchemaAction iAction) {

		HookScope scope = iAction.getFeature(HookActionFeatures.SCOPE) != null ? iAction.getFeature(HookActionFeatures.SCOPE) : HookScope.SESSION;

		registerHook(iAction, HookActionFeatures.HOOK_AROUND_ACTION, scope);
		registerHook(iAction, HookActionFeatures.HOOK_BEFORE_ACTION, scope);
		registerHook(iAction, HookActionFeatures.HOOK_AFTER_ACTION, scope);

		registerHook(iAction, HookActionFeatures.HOOK_AROUND_FIELD_READ, scope);
		registerHook(iAction, HookActionFeatures.HOOK_BEFORE_FIELD_READ, scope);
		registerHook(iAction, HookActionFeatures.HOOK_AFTER_FIELD_READ, scope);

		registerHook(iAction, HookActionFeatures.HOOK_AROUND_FIELD_READ, scope);
		registerHook(iAction, HookActionFeatures.HOOK_BEFORE_FIELD_READ, scope);
		registerHook(iAction, HookActionFeatures.HOOK_AFTER_FIELD_READ, scope);

		registerHook(iAction, HookActionFeatures.HOOK_AROUND_EVENT, scope);
		registerHook(iAction, HookActionFeatures.HOOK_AROUND_EVENT, scope);
		registerHook(iAction, HookActionFeatures.HOOK_AFTER_EVENT, scope);
	}

	/**
	 * Register the hook in the genericHooks map if it uses wild-cards, otherwise put the hook in the exactHooks map.
	 */
	protected void registerHook(SchemaClassElement iElement, Feature<String> feature, HookScope iScope) {
		String iHookTo = iElement.getFeature(feature);
		if (iHookTo == null)
			return;

		String key = feature.getName() + " " + iHookTo;

		List<HookEntry> entries;
		if (iHookTo.contains(WILDCARD_ANY)) {
			// USE THE GENERIC HOOKS MAP
			entries = wildcardHooks.get(key);
			if (entries == null) {
				// REGISTER THE HOOK COLLECTION
				entries = new ArrayList<HookEntry>();
				wildcardHooks.put(key, entries);
			}
		} else {
			// USE THE EXACT-MATCH HOOKS MAP
			entries = exactHooks.get(key);
			if (entries == null) {
				// REGISTER THE HOOK COLLECTION
				entries = new ArrayList<HookEntry>();
				exactHooks.put(key, entries);
			}
		}

		// REGISTER THE HOOK
		entries.add(new HookEntry(iElement, iScope));
	}

	public void configEvent(SchemaEvent event) {
	}

	public String aspectName() {
		return ASPECT_NAME;
	}

	@Override
	public String getStatus() {
		return Serviceable.STATUS_UP;
	}

	public String moduleName() {
		return aspectName();
	}

	public Object getUnderlyingComponent() {
		return null;
	}

	public Object onAfterFieldRead(Object iContent, SchemaField iField, Object iCurrentValue) {
		return iCurrentValue;
	}

	public Object onAfterFieldWrite(Object iContent, SchemaField iField, Object iCurrentValue) {
		searchForHooks(iContent, HookFieldFeatures.FIELD, iField, true);
		return iCurrentValue;
	}

	public void onAfterAction(Object iContent, SchemaAction iAction, Object returnedValue) {
		searchForHooks(iContent, HookActionFeatures.HOOK_AFTER_ACTION, iAction, true);
	}

	@Override
	public boolean onBeforeAction(Object iContent, SchemaAction iAction) {
		if (isHookDefined(HookActionFeatures.HOOK_AROUND_ACTION, iAction)) {
			// THERE IS AN "AROUND" HOOK: CALL IT AND RETURN FALSE TO AVOID THE CALL CHAIN
			searchForHooks(iContent, HookActionFeatures.HOOK_AROUND_ACTION, iAction, false);
			return false;
		}

		// INVOKE THE BEFORE ACTION AND CONTINUE ALWAYS THE CALL CHAIN
		searchForHooks(iContent, HookActionFeatures.HOOK_BEFORE_ACTION, iAction, true);
		return true;
	}

	@Override
	public void onExceptionAction(Object iContent, SchemaAction iAction, Exception exception) {
	}

	public Object onBeforeFieldWrite(Object iContent, SchemaField iField, Object iCurrentValue) {
		if (isHookDefined(HookActionFeatures.HOOK_AROUND_FIELD_WRITE, iField)) {
			// THERE IS AN "AROUND" HOOK: CALL IT AND RETURN THE VALUE OF THE HOOKED ACTION
			return searchForHooks(iContent, HookActionFeatures.HOOK_AROUND_FIELD_WRITE, iField, false);
		} else if (isHookDefined(HookActionFeatures.HOOK_BEFORE_FIELD_WRITE, iField)) {
			// THERE IS AN "BEFORE" HOOK: CALL IT AND RETURN THE VALUE OF THE HOOKED ACTION
			return searchForHooks(iContent, HookActionFeatures.HOOK_BEFORE_FIELD_WRITE, iField, true);
		}
		return iCurrentValue;
	}

	public Object onBeforeFieldRead(Object iContent, SchemaField iField, Object iCurrentValue) {
		if (isHookDefined(HookActionFeatures.HOOK_AROUND_FIELD_READ, iField)) {
			// THERE IS AN "AROUND" HOOK: CALL IT AND RETURN THE VALUE OF THE HOOKED ACTION
			return searchForHooks(iContent, HookActionFeatures.HOOK_AROUND_FIELD_READ, iField, false);
		} else if (isHookDefined(HookActionFeatures.HOOK_BEFORE_FIELD_READ, iField)) {
			// THERE IS AN "BEFORE" HOOK: CALL IT AND RETURN THE VALUE OF THE HOOKED ACTION
			return searchForHooks(iContent, HookActionFeatures.HOOK_BEFORE_FIELD_READ, iField, true);
		}

		return IGNORED;
	}

	public void onFieldRefresh(SessionInfo iSession, Object iContent, SchemaField iField) {
		searchForHooks(iContent, HookFieldFeatures.FIELD, iField, true);
	}

	public boolean isHookDefined(Feature<String> iHookType, SchemaClassElement iElement) {
		String key;

		// FOLLOW THE INHERITANCE TREE
		SchemaClass cls = iElement.getEntity().getSchemaClass();
		while (cls != null) {
			key = getHookKey(iHookType, iElement, cls);

			List<HookEntry> entries = exactHooks.get(key);
			if (entries != null)
				return true;

			for (Entry<String, List<HookEntry>> entry : wildcardHooks.entrySet())
				if (hookMatches(key, entry.getKey()))
					return true;

			cls = cls.getSuperClass();
		}
		return false;
	}

	public Map<String, List<HookEntry>> getExactHooks() {
		return exactHooks;
	}

	public Map<String, List<HookEntry>> getWildcardHooks() {
		return wildcardHooks;
	}

	private Object searchForHooks(Object iContent, Feature<String> iHookType, SchemaClassElement iElement, boolean iFollowTheChain) {
		if (iElement == null)
			return null;

		// FOLLOW THE INHERITANCE TREE
		SchemaClass cls = iElement.getEntity().getSchemaClass();
		Object result;
		String key;

		while (cls != null) {
			key = getHookKey(iHookType, iElement, cls);
			if (log.isDebugEnabled())
				log.debug("[HookAspect.searchForHooks] Searching hooks registered for: " + key);

			// SEARCH AS EXACT MATCH
			List<HookEntry> entries = exactHooks.get(key);
			result = invokeAllRegisteredHooks(iContent, iElement, key, entries, iFollowTheChain);
			if (result != null)
				// BREAK THE CHAIN
				return result;

			// SEARCH THE HOOKS REGISTERED USING THE WILD-CARDS
			for (Entry<String, List<HookEntry>> entry : wildcardHooks.entrySet()) {
				if (hookMatches(key, entry.getKey())) {
					result = invokeAllRegisteredHooks(iContent, iElement, key, entry.getValue(), iFollowTheChain);
					if (result != null)
						// BREAK THE CHAIN
						return result;
				}
			}
			cls = cls.getSuperClass();
		}
		return null;
	}

	private String getHookKey(Feature<String> iHookType, SchemaClassElement iElement, SchemaClass cls) {
		return iHookType.getName() + " " + iElement.getFullName();
	}

	private boolean hookMatches(String iSource, String iMatch) {
		String source[] = iSource.split(" ");
		String match[] = iMatch.split(" ");

		if (!source[0].equals(match[0]))
			return false;

		String compare;
		if (match[1].startsWith(WILDCARD_ANY)) {
			compare = match[1].substring(WILDCARD_ANY.length());
			if (!source[1].endsWith(compare))
				return false;
		} else
			compare = match[1];

		if (compare.endsWith(WILDCARD_ANY)) {
			compare = match[1].substring(0, compare.length() - WILDCARD_ANY.length());

			if (!source[1].startsWith(compare))
				return false;
		}

		return true;
	}

	@SuppressWarnings("unchecked")
	private Object invokeAllRegisteredHooks(Object iContent, SchemaClassElement iElement, String key, List<HookEntry> entries, boolean iFollowTheChain) {
		if (entries == null)
			return null;

		Object result = null;

		for (HookEntry entry : entries) {
			if (entry.scope == HookScope.SESSION) {
				
				List<SchemaObject> objects= Roma.session().getSchemaObjects(entry.clazzElement.getEntity().getSchemaClass());
				if (objects != null) {
					for (SchemaObject schemaObject : objects) {
						result = invokeHook(iContent, iElement, key, schemaObject.getInstance(), entry);
						if (result != null || !iFollowTheChain)
							// BREAK THE CHAIN
							return result;
					}
				}
			} else if (entry.scope == HookScope.APPLICATION) {
				Class<?> cls = (Class<?>) entry.clazzElement.getEntity().getSchemaClass().getLanguageType();

				Map<String, Object> componentsMap = (Map<String, Object>) RomaApplicationContext.getInstance().getComponentAspect().getComponentsOfClass(cls);
				if (componentsMap != null) {
					for (Object component : componentsMap.values()) {
						result = invokeHook(iContent, iElement, key, component, entry);
						if (result != null && !iFollowTheChain)
							// BREAK THE CHAIN
							return result;
					}
				}
			}
		}

		return result;
	}

	private Object invokeHook(Object iContent, SchemaClassElement iElement, String key, Object iTargetObject, HookEntry entry) {
		if (iContent == iTargetObject)
			// INVOKE RECURSEVELY: DO NOTHING
			return null;

		if (log.isDebugEnabled())
			log.debug("[HookAspect.invokeHooks] Invoking hook: " + key + ", against object: " + iTargetObject);

		if (entry.clazzElement instanceof SchemaAction)
			try {
				Object result = ((SchemaAction) entry.clazzElement).invoke(iTargetObject, iContent);

				if (result == null && ((SchemaAction) entry.clazzElement).getReturnType() == null)
					result = IGNORED;

				if (result != null)
					// OBJECT RETURNED: BREAK EXECUTION
					return result;

			} catch (Exception e) {
				log.error("[HookAspect.invokeHooks] Error on calling the hook defined in: " + key, e);
			}
		else if (entry.clazzElement instanceof SchemaField)
			try {
				((SchemaField) entry.clazzElement).setValue(iTargetObject, ((SchemaField) iElement).getValue(iContent));
				Roma.fieldChanged(iTargetObject, entry.clazzElement.getName());
			} catch (Exception e) {
				log.error("[HookAspect.invokeHooks] Error on calling the hook defined in: " + key, e);
			}
		return null;
	}
}
