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

package org.romaframework.core.flow;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.romaframework.aspect.i18n.I18NAspect;
import org.romaframework.aspect.session.SessionAspect;
import org.romaframework.aspect.session.SessionInfo;
import org.romaframework.aspect.validation.MultiValidationException;
import org.romaframework.core.Roma;
import org.romaframework.core.Utility;
import org.romaframework.core.domain.entity.ComposedEntity;
import org.romaframework.core.entity.EntityHelper;
import org.romaframework.core.exception.ConfigurationException;
import org.romaframework.core.exception.UserException;
import org.romaframework.core.schema.SchemaAction;
import org.romaframework.core.schema.SchemaClass;
import org.romaframework.core.util.ListenerManager;

@SuppressWarnings("unchecked")
public class Controller extends ListenerManager<Class<?>> {

	private static Log							log						= LogFactory.getLog(Controller.class);
	private static final Controller	INSTANCE			= new Controller();

	private static final String			ERROR_LABEL		= ".error";
	public static final String			CONTEXT_ATTR	= "roma.controller.context";

	protected Controller() {
	}

	public void executeAction(Object iContent, SchemaAction iAction) throws Throwable {
		if (iContent == null || iAction == null) {
			return;
		}

		if (log.isDebugEnabled()) {
			log.debug("[Controller.executeAction] Executing action: " + iAction + " on object " + iContent.toString() + "...");
		}

		try {
			iAction.invoke(iContent);
		} catch (InvocationTargetException ex) {
			Throwable nestedExc = ex.getTargetException();
			if (nestedExc == null || !(nestedExc instanceof UserException) && !(nestedExc instanceof MultiValidationException)) {

				String errorLabel = I18NAspect.VARNAME_PREFIX + iAction.getName() + ERROR_LABEL;

				log.warn("[Controller.executeAction] error on execution of method: " + iAction.getName(), nestedExc);

				// WRAP THE EXCEPTION IN A NEW USER EXCEPTION TAKING THE ACTION
				// NAME
				throw new UserException(iContent, errorLabel, nestedExc);
			}
			// THROW THE NESTED EXCEPTION (BYPASS THE REFLECTION)
			throw nestedExc;
		}
	}

	/**
	 * Flush all objects of a class.
	 * 
	 * @param iEntityClass
	 *          Class name of objects to flush
	 */
	public void flushObjectsOfClass(SchemaClass iEntityClass) {
		Collection<SessionInfo> sessions = Roma.session().getSessionInfos();
		for (SessionInfo sess : sessions) {
			ControllerContext ctx = (ControllerContext) Roma.session().getProperty(sess.getSystemSession(), CONTEXT_ATTR);
			if (ctx != null)
				ctx.removeObject(iEntityClass);
		}
	}

	/**
	 * Get the instance of user class iNextClass. If the instance is in the user session's pool then recycle it, otherwise create a
	 * new one and insert in to the user session's pool.
	 * 
	 * @param iEntityClass
	 *          User Class of requested object
	 * @param iEntityInstance
	 *          Entity instance to set inside the object requested in case of ComposedEntity
	 * @return instance of class requested
	 */
	public <T> T getObject(Class<T> iEntityClass, Object iEntityInstance) {
		return (T) getObject(Utility.getClassName(iEntityClass), iEntityInstance);
	}

	/**
	 * Get the instance of user class iEntityClassName. If the instance is in the user session's pool then recycle it, otherwise
	 * create a new one and insert in to the user session's pool.
	 * 
	 * @param iEntityClassName
	 *          User Class name of requested object
	 * @param iEntityInstance
	 *          Entity instance to set inside the object requested in case of ComposedEntity
	 * @return instance of class requested
	 */
	public <T> T getObject(String iEntityClassName, Object iEntityInstance) {
		return (T) getObject(Roma.schema().getSchemaClass(iEntityClassName), iEntityInstance);
	}

	/**
	 * Get the instance of user class iNextClass. If the instance is in the user session's pool then recycle it, otherwise create a
	 * new one and insert in to the user session's pool.
	 * 
	 * @param iEntityClass
	 *          User Class of requested object
	 * @param iEntityInstance
	 *          Entity instance to set inside the object requested in case of ComposedEntity
	 * @return instance of class requested
	 */
	public <T> T getObject(SchemaClass iEntityClass, Object iEntityInstance) {
		ControllerContext context = getContext();

		T obj = null;
		synchronized (context.getObjects()) {
			obj = (T) context.getObjects().get(iEntityClass);
			if (obj == null) {
				obj = (T) createNewObjectInstance(iEntityClass, iEntityInstance);
			} else if (obj instanceof ComposedEntity<?>) {
				obj = (T) createNewObjectInstance(iEntityClass, iEntityInstance);

				// if (iEntityInstance != null) {
				// // OBJECT ALREADY EXISTENT, SO UPDATE INTERNAL ENTITY
				// EntityHelper.assignEntity(obj, iEntityInstance);
				// }
			}
		}

		return obj;
	}

	private <T> T createNewObjectInstance(SchemaClass iEntityClass, Object iEntityInstance) {
		ControllerContext context = getContext();
		T result;
		try {
			result = (T) EntityHelper.createObject(iEntityInstance, iEntityClass);
		} catch (Exception e) {
			throw new ConfigurationException("Cannot create instance for class " + iEntityClass, e);
		}
		context.getObjects().put(iEntityClass, result);
		return result;
	}

	/**
	 * Return the context for current user.
	 * 
	 * @return ControllerContext object
	 */
	public ControllerContext getContext() {
		SessionAspect sess = Roma.session();
		if (sess.getActiveSessionInfo() == null)
			return null;

		return (ControllerContext) sess.getProperty(CONTEXT_ATTR);
	}

	/**
	 * Create an empty context for current user.
	 */
	public void createContext() {
		Roma.session().setProperty(Controller.CONTEXT_ATTR, new ControllerContext());
	}

	public static Controller getInstance() {
		return INSTANCE;
	}
}
