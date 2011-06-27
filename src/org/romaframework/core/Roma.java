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
package org.romaframework.core;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.romaframework.aspect.i18n.I18NAspect;
import org.romaframework.aspect.logging.LoggingAspect;
import org.romaframework.aspect.persistence.PersistenceAspect;
import org.romaframework.aspect.persistence.PersistenceAspectAbstract;
import org.romaframework.aspect.persistence.PersistenceConstants;
import org.romaframework.aspect.scripting.ScriptingAspect;
import org.romaframework.aspect.scripting.ScriptingAspectListener;
import org.romaframework.aspect.session.SessionAspect;
import org.romaframework.aspect.session.SessionInfo;
import org.romaframework.aspect.validation.ValidationAspect;
import org.romaframework.core.aspect.Aspect;
import org.romaframework.core.aspect.AspectManager;
import org.romaframework.core.exception.ConfigurationException;
import org.romaframework.core.exception.ConfigurationNotFoundException;
import org.romaframework.core.factory.GenericFactory;
import org.romaframework.core.flow.Controller;
import org.romaframework.core.flow.ObjectContext;
import org.romaframework.core.flow.ObjectRefreshListener;
import org.romaframework.core.handler.RomaObjectHandler;
import org.romaframework.core.module.Module;
import org.romaframework.core.module.ModuleManager;
import org.romaframework.core.repository.GenericRepository;
import org.romaframework.core.schema.Feature;
import org.romaframework.core.schema.FeatureRegistry;
import org.romaframework.core.schema.FeatureType;
import org.romaframework.core.schema.SchemaClass;
import org.romaframework.core.schema.SchemaFeatures;
import org.romaframework.core.schema.SchemaFeaturesChangeListener;
import org.romaframework.core.schema.SchemaManager;
import org.romaframework.core.schema.SchemaObject;

/**
 * Helper class that acts as unique entry point to the user to access to the Roma functionalities.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
@SuppressWarnings("unchecked")
public class Roma implements ScriptingAspectListener {
	// CACHED ASPECTS
	protected static AspectManager		aspectManager			= null;
	protected static SessionAspect		sessionAspect			= null;
	protected static I18NAspect				i18NAspect				= null;
	protected static ValidationAspect	validationAspect	= null;
	protected static I18NAspect				i18nAspect				= null;
	protected static ScriptingAspect	scriptingAspect		= null;
	protected static LoggingAspect		loggingAspect			= null;

	// OTHER COMPONENTS
	protected static SchemaManager		schemaManager			= null;
	protected static RomaContext			context						= new RomaContext();

	protected static Roma							singleton					= new Roma();

	protected Roma() {
		Controller.getInstance().registerListener(ScriptingAspectListener.class, this);
	}

	public static RomaContext context() {
		return context;
	}

	public static boolean existComponent(String iName) {
		return ObjectContext.getInstance().existComponent(iName);
	}

	public static <T> boolean existComponent(Class<T> iClass) {
		return ObjectContext.getInstance().existComponent(iClass);
	}

	public static <T> T component(String iName) {
		return (T) ObjectContext.getInstance().getComponent(iName);
	}

	public static <T> T component(Class<T> iClass) {
		return (T) ObjectContext.getInstance().getComponent(iClass);
	}

	public static <T> T aspect(Class<? extends T> iClass) {
		String aspectName = Utility.getClassName(iClass);
		int pos = aspectName.indexOf("Aspect");
		if (pos == -1)
			throw new ConfigurationNotFoundException("Cannot find aspect implementation for class: " + aspectName);

		aspectName = aspectName.substring(0, pos).toLowerCase();
		checkForAspectManager();
		return (T) aspectManager.getAspect(aspectName);
	}

	public static <T> T aspect(String iName) {
		checkForAspectManager();
		return (T) aspectManager.getAspect(iName);
	}

	public static Collection<Aspect> aspects() {
		checkForAspectManager();
		return aspectManager.getAspectCollection();
	}

	/**
	 * Refresh an object of the current active session.
	 * 
	 * @see #objectChanged(SessionInfo, Object)
	 * @param iUserObject
	 *          The User Object of changed property
	 */
	public static void objectChanged(Object iUserObject) {
		objectChanged(null, iUserObject);
	}

	/**
	 * Refresh an object.
	 * 
	 * @see ObjectContext#objectChanged(SessionInfo, Object)
	 * @param iUserSession
	 *          The User Session
	 * @param iUserObject
	 *          The User Object of changed property
	 */
	public static void objectChanged(SessionInfo iUserSession, Object iUserObject) {
		if (iUserObject == null)
			return;

		if (iUserSession == null)
			iUserSession = Roma.session().getActiveSessionInfo();

		List<ObjectRefreshListener> listeners = Controller.getInstance().getListeners(ObjectRefreshListener.class);

		for (ObjectRefreshListener objectRefreshListener : listeners) {
			objectRefreshListener.onObjectRefresh(iUserSession, iUserObject);
		}
	}

	/**
	 * Refresh a property feature and/or value of the current active session.
	 * 
	 * @see ObjectContext#fieldChanged(Object, String...)
	 * @param iUserObject
	 *          The User Object of changed property
	 * @param iFieldNames
	 *          Optional field names to signal the change
	 */
	public static void fieldChanged(Object iUserObject, String... iFieldNames) {
		ObjectContext.getInstance().fieldChanged(iUserObject, iFieldNames);
	}

	@Deprecated
	public static RomaObjectHandler getHandler(Object iUserObject) throws ConfigurationNotFoundException {
		// ASK TO ALL THE REGISTERED MODULES IF THEY MANAGE THE USER OBJECT
		// REQUESTED.
		RomaObjectHandler handler;
		for (Module module : ModuleManager.getInstance().getConfigurationValues()) {
			handler = module.getObjectHandler(iUserObject);
			if (handler != null)
				// FOUND THE HANDLER, USE IT FORWARD
				return handler;
		}

		return null;
	}

	@Deprecated
	public static List<RomaObjectHandler> getHandlers(SchemaClass iUserClass) throws ConfigurationNotFoundException {
		// ASK TO ALL THE REGISTERED MODULES IF THEY MANAGE INSTANCE OF THE USER
		// CLASS REQUESTED.
		List<RomaObjectHandler> handlers = new ArrayList<RomaObjectHandler>();

		List<RomaObjectHandler> moduleHandlers;
		for (Module module : ModuleManager.getInstance().getConfigurationValues()) {
			moduleHandlers = module.getObjectHandlers(iUserClass);
			if (moduleHandlers != null)
				// FOUND THE HANDLER, USE IT FORWARD
				handlers.addAll(moduleHandlers);
		}

		return handlers;
	}

	@Deprecated
	@SuppressWarnings("rawtypes")
	public static boolean setFieldFeature(Object iUserObject, String iAspectName, String iFieldName, String iFeatureName, Object iFeatureValue)
			throws ConfigurationNotFoundException {
		Feature fae = FeatureRegistry.getFeature(iAspectName, FeatureType.FIELD, iFeatureName);
		return setFeature(iUserObject, iFieldName, fae, iFeatureValue);
	}

	@Deprecated
	public static Object getFieldFeature(Object iUserObject, String iAspectName, String iFieldName, String iFeatureName) {
		Feature<?> fae = FeatureRegistry.getFeature(iAspectName, FeatureType.FIELD, iFeatureName);
		return getFeature(iUserObject, iFieldName, fae);
	}

	@Deprecated
	@SuppressWarnings("rawtypes")
	public static boolean setClassFeature(Object iUserObject, String iAspectName, String iFeatureName, Object iFeatureValue)
			throws ConfigurationNotFoundException {
		Feature fae = FeatureRegistry.getFeature(iAspectName, FeatureType.CLASS, iFeatureName);
		return setFeature(iUserObject, fae, iFeatureValue);
	}

	@Deprecated
	public static Object getClassFeature(Object iUserObject, String iAspectName, String iFeatureName) {
		Feature<?> fae = FeatureRegistry.getFeature(iAspectName, FeatureType.CLASS, iFeatureName);
		return getFeature(iUserObject, fae);
	}

	@SuppressWarnings("rawtypes")
	@Deprecated
	public static boolean setActionFeature(Object iUserObject, String iAspectName, String iActionName, String iFeatureName, Object iFeatureValue)
			throws ConfigurationNotFoundException {
		Feature fae = FeatureRegistry.getFeature(iAspectName, FeatureType.ACTION, iFeatureName);
		return setFeature(iUserObject, fae, iFeatureValue);
	}

	private static <T> SchemaFeatures getSchemaFeature(Object iUserObject, String elementName, Feature<T> feature) {
		SchemaObject schema = Roma.session().getSchemaObject(iUserObject);
		if (schema == null)
			return null;
		SchemaFeatures features = null;
		if (FeatureType.ACTION.equals(feature.getType())) {
			if (elementName == null)
				return null;
			features = schema.getAction(elementName);
			if (features == null) {
				if (schema.getSchemaClass().getAction(elementName) == null)
					throw new ConfigurationException("Action '" + elementName + "' not found in class '" + schema + "'");
			}
		} else if (FeatureType.FIELD.equals(feature.getType())) {
			if (elementName == null)
				return null;
			features = schema.getField(elementName);
			if (features == null) {
				if (schema.getSchemaClass().getField(elementName) == null)
					throw new ConfigurationException("Field '" + elementName + "' not found in class '" + schema + "'");
			}
		} else if (FeatureType.CLASS.equals(feature.getType()))
			features = schema;
		else if (FeatureType.EVENT.equals(feature.getType())) {
			if (elementName == null)
				return null;
			features = schema.getEvent(elementName);
		}

		return features;
	}

	public static <T> T getFeature(Object iUserObject, String elementName, Feature<T> feature) {
		SchemaFeatures features = getSchemaFeature(iUserObject, elementName, feature);
		if (features == null)
			return null;
		return features.getFeature(feature);
	}

	public static <T> boolean setFeature(Object iUserObject, Feature<T> feature, T value) {
		if (!FeatureType.CLASS.equals(feature.getType()))
			return false;
		return setFeature(iUserObject, null, feature, value);
	}

	public static <T> T getFeature(Object iUserObject, Feature<T> feature) {
		if (!FeatureType.CLASS.equals(feature.getType()))
			return null;
		return getFeature(iUserObject, feature);
	}

	public static <T> boolean setFeature(Object iUserObject, String elementName, Feature<T> feature, T value) {
		SchemaFeatures features = getSchemaFeature(iUserObject, elementName, feature);

		if (features == null)
			return false;
		T oldValue = features.getFeature(feature);
		features.setFeature(feature, value);
		// BROADCAST CHANGES TO ALL REGISTERED LISTENERS
		List<SchemaFeaturesChangeListener> listeners = Controller.getInstance().getListeners(SchemaFeaturesChangeListener.class);
		if (listeners != null)
			switch (feature.getType()) {
			case ACTION:
				for (SchemaFeaturesChangeListener listener : listeners) {
					listener.signalChangeAction(iUserObject, elementName, feature, oldValue, value);
				}

				break;
			case FIELD:
				for (SchemaFeaturesChangeListener listener : listeners) {
					listener.signalChangeField(iUserObject, elementName, feature, oldValue, value);
				}
			case EVENT:
			case CLASS:
				for (SchemaFeaturesChangeListener listener : listeners) {
					listener.signalChangeClass(iUserObject, feature, oldValue, value);
				}
			default:
				break;
			}
		return true;
	}

	public static SessionAspect session() {
		if (sessionAspect == null) {
			synchronized (Roma.class) {
				if (sessionAspect == null) {
					sessionAspect = Roma.aspect(SessionAspect.ASPECT_NAME);
				}
			}
		}
		return sessionAspect;
	}

	/**
	 * Return the Atomic Persistence Aspect instance. The instance is thread-safe and is shared by all the requests.
	 * 
	 * @return {@link PersistenceAspect}
	 */
	public static PersistenceAspect persistence() {
		return PersistenceAspectAbstract.getPersistenceComponent(PersistenceConstants.MODE_ATOMIC);
	}

	/**
	 * Return the Persistence Aspect instance based on the mode passed.
	 * 
	 * @param iMode
	 *          The mode between the MODE declared in {@link PersistenceConstants}
	 * @return {@link PersistenceAspect}
	 */
	public static PersistenceAspect persistence(String iMode) {
		return PersistenceAspectAbstract.getPersistenceComponent(iMode);
	}

	public static ValidationAspect validation() {
		if (validationAspect == null) {
			synchronized (Roma.class) {
				if (validationAspect == null) {
					validationAspect = Roma.aspect(ValidationAspect.ASPECT_NAME);
				}
			}
		}
		return validationAspect;
	}

	public static I18NAspect i18n() {
		if (i18nAspect == null) {
			synchronized (Roma.class) {
				if (i18nAspect == null) {
					i18nAspect = Roma.aspect(I18NAspect.ASPECT_NAME);
				}
			}
		}
		return i18nAspect;
	}

	public static ScriptingAspect scripting() {
		if (scriptingAspect == null) {
			synchronized (Roma.class) {
				if (scriptingAspect == null) {
					scriptingAspect = Roma.aspect(ScriptingAspect.ASPECT_NAME);
				}
			}
		}
		return scriptingAspect;
	}

	public static LoggingAspect logging() {
		if (loggingAspect == null) {
			synchronized (Roma.class) {
				if (loggingAspect == null) {
					loggingAspect = Roma.aspect(LoggingAspect.ASPECT_NAME);
				}
			}
		}
		return loggingAspect;
	}

	public static SchemaManager schema() {
		if (schemaManager == null) {
			synchronized (Roma.class) {
				if (schemaManager == null) {
					schemaManager = Roma.component(SchemaManager.class);
				}
			}
		}
		return schemaManager;
	}

	private static void checkForAspectManager() {
		if (aspectManager == null) {
			synchronized (Roma.class) {
				if (aspectManager == null) {
					aspectManager = Roma.component(AspectManager.class);
				}
			}
		}
	}

	public Reader onBeforeExecution(String iLanguage, Reader iScript, Map<String, Object> iContext) {
		iContext.put(getClass().getSimpleName(), singleton);
		return iScript;
	}

	public Object onAfterExecution(String iLanguage, Reader iScript, Map<String, Object> iContext, Object iReturnedValue) {
		return iReturnedValue;
	}

	/**
	 * returns a domain factory (if defined) for a particular schema class.
	 * 
	 * @param <T>
	 *          the type of the object that will be returned
	 * @param entityClass
	 *          the {@link SchemaClass} of the domain class
	 * @return a domain factory (if defined) for a particular schema class
	 */
	public static <T extends GenericFactory<?>> T factory(SchemaClass entityClass) {
		String factoryName = entityClass.getName() + "Factory";
		if (Roma.existComponent(factoryName)) {
			return (T) Roma.component(factoryName);
		}
		return null;
	}

	/**
	 * returns a domain factory (if defined) for a particular domain class
	 * 
	 * @param <T>
	 *          the type of the object that will be returned
	 * @param entityClass
	 *          the class (simple) name of the domain class
	 * @return a domain factory (if defined) for a particular domain class
	 */
	public static <T extends GenericFactory<?>> T factory(String entityClass) {
		return (T) factory(Roma.schema().getSchemaClass(entityClass));
	}

	/**
	 * 
	 * @param <T>
	 * @param entityClass
	 * @return
	 */
	public static <T extends GenericFactory<Z>, Z> T factory(Class<Z> entityClass) {
		return (T) factory(Roma.schema().getSchemaClass(entityClass));
	}

	/**
	 * Retrieve the repository (if defined) for a particular domain class.
	 * 
	 * @param <T>
	 *          the type of the object that will be returned
	 * @param <Z>
	 *          the type of domain class.
	 * @param entityClass
	 *          the {@link SchemaClass} of domain class.
	 * @return the repository instance.
	 */
	public static <T extends GenericRepository<?>> T repository(SchemaClass entityClass) {
		if (entityClass != null) {
			String repositoryName = entityClass.getName() + "Repository";
			if (Roma.existComponent(repositoryName)) {
				return (T) Roma.component(repositoryName);
			}
		}
		return null;
	}

	/**
	 * Retrieve the repository (if defined) for a particular domain class.
	 * 
	 * @param <T>
	 *          the type of the object that will be returned
	 * @param <Z>
	 *          the type of domain class.
	 * @param entityClass
	 *          the simple name of domain class.
	 * @return the repository instance.
	 */
	public static <T extends GenericRepository<?>> T repository(String entityClass) {
		return (T) repository(Roma.schema().getSchemaClass(entityClass));
	}

	/**
	 * Retrieve the repository (if defined) for a particular domain class.
	 * 
	 * @param <T>
	 *          the type of the object that will be returned
	 * @param <Z>
	 *          the type of domain class.
	 * @param entityClass
	 *          the {@link Class<Z>} of domain class.
	 * @return the repository instance.
	 */
	public static <T extends GenericRepository<Z>, Z> T repository(Class<Z> entityClass) {
		return entityClass != null ? (T) repository(Roma.schema().getSchemaClass(entityClass)) : null;
	}
}
