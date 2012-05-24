/*
 * Copyright 2006 Luca Garulli (luca.garulli--at--assetdata.it)
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

package org.romaframework.aspect.core;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.romaframework.aspect.core.annotation.CoreClass;
import org.romaframework.aspect.core.feature.CoreClassFeatures;
import org.romaframework.aspect.core.feature.CoreFieldFeatures;
import org.romaframework.core.Roma;
import org.romaframework.core.Utility;
import org.romaframework.core.aspect.Aspect;
import org.romaframework.core.classloader.ClassLoaderListener;
import org.romaframework.core.config.ApplicationConfiguration;
import org.romaframework.core.config.RomaApplicationListener;
import org.romaframework.core.config.Serviceable;
import org.romaframework.core.domain.entity.ComposedEntity;
import org.romaframework.core.flow.Controller;
import org.romaframework.core.module.SelfRegistrantModule;
import org.romaframework.core.resource.AutoReloadManager;
import org.romaframework.core.schema.Feature;
import org.romaframework.core.schema.FeatureType;
import org.romaframework.core.schema.SchemaAction;
import org.romaframework.core.schema.SchemaClass;
import org.romaframework.core.schema.SchemaClassDefinition;
import org.romaframework.core.schema.SchemaClassResolver;
import org.romaframework.core.schema.SchemaElement;
import org.romaframework.core.schema.SchemaEvent;
import org.romaframework.core.schema.SchemaFeaturesChangeListener;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.schema.SchemaHelper;
import org.romaframework.core.schema.SchemaObject;
import org.romaframework.core.schema.reflection.SchemaActionDelegate;
import org.romaframework.core.schema.reflection.SchemaEventDelegate;
import org.romaframework.core.schema.reflection.SchemaFieldDelegate;
import org.romaframework.core.schema.reflection.SchemaFieldReflection;

public class CoreAspect extends SelfRegistrantModule implements Aspect, RomaApplicationListener, ClassLoaderListener, SchemaFeaturesChangeListener {

	public static final String	ASPECT_NAME							= "core";

	protected List<Class<?>>		classesToBeLoadedEarly	= new ArrayList<Class<?>>();						;

	protected static Log				log											= LogFactory.getLog(CoreAspect.class);

	public CoreAspect() {
		Controller.getInstance().registerListener(RomaApplicationListener.class, this);
		Controller.getInstance().registerListener(ClassLoaderListener.class, this);
		Controller.getInstance().registerListener(SchemaFeaturesChangeListener.class, this);
	}

	public void startup() {
		if (status == STATUS_STARTING || status == STATUS_UP)
			return;

		status = STATUS_STARTING;

		for (Aspect aspect : Roma.aspects()) {
			String clBaseName = Utility.ROMA_PACKAGE + ".aspect." + aspect.aspectName() + ".feature." + Utility.getCapitalizedString(aspect.aspectName());
			try {
				Class.forName(clBaseName + "Features");
			} catch (Exception e) {
				if (log.isDebugEnabled())
					log.debug("Not Found Features", e);
			}
			for (FeatureType type : FeatureType.values()) {
				String name = clBaseName + type.getBaseName() + "Features";
				try {
					Class.forName(name);
				} catch (Exception e) {
					if (log.isDebugEnabled())
						log.debug("Not Found Features", e);
				}
			}

		}

		SchemaClassResolver classResolver = Roma.component(SchemaClassResolver.class);

		// REGISTER THE APPLICATION DOMAIN AS FIRST ONE PATH
		classResolver.addDomainPackage(Roma.component(ApplicationConfiguration.class).getApplicationPackage());
		classResolver.addDomainPackage(Utility.ROMA_PACKAGE + Utility.PACKAGE_SEPARATOR + "core");
		classResolver.addPackage("java.lang");
		classResolver.addResourceClass(Object.class);

		status = STATUS_UP;
	}

	public void shutdown() {
		Roma.component(AutoReloadManager.class).shutdown();
		Roma.component(SchemaClassResolver.class).shutdown();
	}

	public void beginConfigClass(SchemaClassDefinition iClass) {
	}

	public void endConfigClass(SchemaClassDefinition iClass) {
	}

	public void configClass(SchemaClassDefinition iClass) {

		if (iClass.isSettedFeature(CoreClassFeatures.ENTITY) && iClass.getSchemaClass().isComposedEntity() && iClass.getField(ComposedEntity.NAME) != null) {
			iClass.getField(ComposedEntity.NAME).setType(iClass.getFeature(CoreClassFeatures.ENTITY));
		}
	}

	public void expandField(SchemaField iField, SchemaClassDefinition dest) {
		SchemaClass cl = iField.getType().getSchemaClass();
		Iterator<SchemaField> fields = cl.getFieldIterator();
		while (fields.hasNext()) {
			SchemaField sf = fields.next();
			SchemaField parentField = iField.getEntity().getField(sf.getName());
			if (parentField == null || parentField.getType().getSchemaClass().isAssignableAs(sf.getType().getSchemaClass())) {
				SchemaFieldDelegate sfd = new SchemaFieldDelegate(iField.getEntity(), iField, sf);
				sfd.configure();
				sfd.setOrder(iField.getEntity().getSchemaClass().getFieldOrder(sfd));
				dest.setField(sf.getName(), sfd);
			}
		}
		Iterator<SchemaAction> actions = cl.getActionIterator();
		while (actions.hasNext()) {
			SchemaAction sa = actions.next();
			SchemaActionDelegate sad = new SchemaActionDelegate(iField.getEntity(), iField, sa);
			sad.configure();
			sad.setOrder(iField.getEntity().getSchemaClass().getActionOrder(sad));
			dest.setAction(sa.getName(), sad);
		}
		Iterator<SchemaEvent> events = cl.getEventIterator();
		while (events.hasNext()) {
			SchemaEvent se = events.next();
			SchemaEventDelegate sed = new SchemaEventDelegate(iField.getEntity(), iField, se);
			sed.configure();
			dest.setEvent(se.getName(), sed);
		}

	}

	private void unexpandField(SchemaField iField) {
		SchemaClass cl = iField.getEntity().getSchemaClass();
		Iterator<SchemaField> fields = cl.getFieldIterator();
		List<SchemaField> toRemoveFields = new ArrayList<SchemaField>();
		while (fields.hasNext()) {
			SchemaField sf = fields.next();
			if (sf instanceof SchemaFieldDelegate) {
				if (((SchemaFieldDelegate) sf).getFieldObject().getName().equals(iField.getName()))
					toRemoveFields.add(sf);
			}
		}
		for (SchemaField schemaField : toRemoveFields) {
			cl.getFields().remove(schemaField.getName());
		}

		Iterator<SchemaAction> actions = cl.getActionIterator();
		List<SchemaAction> toRemoveActions = new ArrayList<SchemaAction>();
		while (actions.hasNext()) {
			SchemaAction sf = actions.next();
			if (sf instanceof SchemaActionDelegate) {
				if (((SchemaActionDelegate) sf).getFieldObject().getName().equals(iField.getName()))
					toRemoveActions.add(sf);
			}
		}
		for (SchemaAction schemaField : toRemoveActions) {
			cl.getActions().remove(schemaField.getName());
		}

		Iterator<SchemaEvent> events = cl.getEventIterator();
		List<SchemaEvent> toRemoveEvents = new ArrayList<SchemaEvent>();
		while (actions.hasNext()) {
			SchemaEvent sf = events.next();
			if (sf instanceof SchemaEventDelegate) {
				if (((SchemaEventDelegate) sf).getFieldObject().getName().equals(iField.getName()))
					toRemoveEvents.add(sf);
			}
		}
		for (SchemaEvent schemaEvent : toRemoveEvents) {
			cl.removeEvent(schemaEvent);
		}

	}

	public void configField(SchemaField iField) {

		if (iField.getFeature(CoreFieldFeatures.EXPAND)) {
			expandField(iField, iField.getEntity());
		} else if (iField.isSettedFeature(CoreFieldFeatures.EXPAND) && !iField.getFeature(CoreFieldFeatures.EXPAND)) {
			unexpandField(iField);
		}
		if (iField instanceof SchemaFieldReflection) {

			SchemaFieldReflection ref = (SchemaFieldReflection) iField;
			SchemaClass[] embeddedTypeGenerics = null;
			Type fieldType = null;
			if (ref.getGetterMethod() != null) {
				if ((fieldType = ref.getGetterMethod().getGenericReturnType()) == null) {
					fieldType = ref.getGetterMethod().getReturnType();
				}
			}

			if (fieldType == null && ref.getField() != null) {
				if ((fieldType = ref.getField().getGenericType()) == null) {
					fieldType = ref.getField().getType();
				}
			}

			if (fieldType != null && fieldType instanceof ParameterizedType) {
				ParameterizedType ownerType = SchemaHelper.resolveParameterizedType((Type) ref.getEntity().getSchemaClass().getLanguageType());
				ParameterizedType pt = (ParameterizedType) fieldType;
				if (pt.getActualTypeArguments() != null) {
					embeddedTypeGenerics = new SchemaClass[pt.getActualTypeArguments().length];
					int i = 0;
					for (Type argType : pt.getActualTypeArguments())
						embeddedTypeGenerics[i++] = Roma.schema().getSchemaClassIfExist(SchemaHelper.resolveClassFromType(argType, ownerType));
				}
				ref.setEmbeddedTypeGenerics(embeddedTypeGenerics);
				if (embeddedTypeGenerics.length > 0)
					ref.setEmbeddedType(embeddedTypeGenerics[0]);
			} else if (fieldType instanceof Class<?>) {
				Class<?> cls = (Class<?>) fieldType;
				if (cls.isArray())
					ref.setEmbeddedLanguageType(cls.getComponentType());
			}
		}
	}

	public void configAction(SchemaAction iAction) {
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

	public void onBeforeStartup() {
		Roma.context().create();
	}

	public void onAfterStartup() {
		for (Class<?> cls : classesToBeLoadedEarly)
			Roma.schema().getSchemaClass(cls);
		Roma.context().destroy();
	}

	public void onBeforeShutdown() {
		Roma.context().create();
	}

	public void onAfterShutdown() {
		Roma.context().destroy();
	}

	/**
	 * Register the class to be loaded early
	 */
	public void onClassLoading(Class<?> iClass) {
		CoreClass ann = iClass.getAnnotation(CoreClass.class);
		if (ann != null && ann.loading() == CoreClass.LOADING_MODE.EARLY)
			classesToBeLoadedEarly.add(iClass);
	}

	public <T> void signalChangeAction(Object iUserObject, String iActionName, Feature<T> iFeature, T iOldValue, T iFeatureValue) {
	};

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> void signalChangeClass(Object iUserObject, Feature<T> iFeature, T iOldValue, T iFeatureValue) {
		if (Roma.session().getActiveSessionInfo() != null) {
			if (iFeature == CoreClassFeatures.ORDER_FIELDS) {
				SchemaObject obje = Roma.session().getSchemaObject(iUserObject);
				obje.setOrderFields((List) orderElements((Collection) obje.getFields().values(), (String[]) iFeatureValue));
			} else if (iFeature == CoreClassFeatures.ORDER_ACTIONS) {
				SchemaObject obje = Roma.session().getSchemaObject(iUserObject);
				obje.setOrderActions((List) orderElements((Collection) obje.getActions().values(), (String[]) iFeatureValue));
			}
		}
	}

	private List<SchemaElement> orderElements(Collection<SchemaElement> elements, String[] orderedValues) {
		List<SchemaElement> ls = new ArrayList<SchemaElement>(elements);
		SchemaElement newOrdered[] = new SchemaElement[ls.size()];
		for (SchemaElement sf : ls) {
			for (int fieldNum = 0; fieldNum < orderedValues.length; ++fieldNum) {
				if (orderedValues[fieldNum].equals(sf.getName())) {
					newOrdered[fieldNum] = sf;
					sf.setOrder(fieldNum);
				}
			}
		}
		List<SchemaElement> newOrderedFields = new ArrayList<SchemaElement>();
		for (SchemaElement field : newOrdered) {
			if (field != null)
				newOrderedFields.add(field);
		}
		ls.removeAll(newOrderedFields);
		for (SchemaElement field : ls) {
			newOrderedFields.add(field);
			field.setOrder(newOrderedFields.size() - 1);
		}
		return newOrderedFields;
	}

	public <T> void signalChangeField(Object iUserObject, String iFieldName, Feature<T> iFeature, T iOldValue, T iFeatureValue) {
	};

}
