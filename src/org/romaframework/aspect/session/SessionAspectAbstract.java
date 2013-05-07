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

package org.romaframework.aspect.session;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.romaframework.core.Roma;
import org.romaframework.core.config.ApplicationConfiguration;
import org.romaframework.core.flow.Controller;
import org.romaframework.core.module.SelfRegistrantConfigurableModule;
import org.romaframework.core.schema.SchemaClass;
import org.romaframework.core.schema.SchemaClassDefinition;
import org.romaframework.core.schema.SchemaObject;
import org.romaframework.core.schema.SchemaReloadListener;


public abstract class SessionAspectAbstract extends SelfRegistrantConfigurableModule<String> implements SessionAspect, SchemaReloadListener {

	public SessionAspectAbstract() {
		Controller.getInstance().registerListener(SchemaReloadListener.class, this);
	}

	private static final String	SESSION_SCHEMA_OBJECT	= "$$_SESSION_SCHEMA_OBJECT_$$";
	private static final String	SESSION_OBJECT				= "$$_SESSION_OBJECT_$$";

	/**
	 * Retrieve the map of schema object for current map.
	 * 
	 * @return the object-> schemaObject map
	 */
	public Map<Object, SchemaObject> getSchemaObjectMap() {
		Map<Object, SchemaObject> so = getProperty(SESSION_SCHEMA_OBJECT);
		if (so == null) {
			so = new IdentityWeakHashMap<Object, SchemaObject>();
			setProperty(SESSION_SCHEMA_OBJECT, so);
		}
		return so;
	}

	/**
	 * Retrieve the object cache for current session.
	 * 
	 * @param session
	 *          where retrieve the cache.
	 * @return the map cache for session.
	 */
	public Map<SchemaClass, Object> getObjectMap(SessionInfo session) {
		Map<SchemaClass, Object> objectMap = getProperty(session, SESSION_OBJECT);
		if (objectMap == null) {
			objectMap = new HashMap<SchemaClass, Object>();
			setProperty(SESSION_OBJECT, objectMap);
		}
		return objectMap;
	}

	/**
	 * {@inheritDoc}
	 */
	public SchemaObject getSchemaObject(Object object) {
		if (getActiveSessionInfo() == null)
			return null;
		synchronized (getActiveSessionInfo()) {
			if (object == null)
				return null;
			Map<Object, SchemaObject> so = getSchemaObjectMap();
			SchemaObject schemaObject = so.get(object);
			if (schemaObject == null) {
				if (object instanceof SchemaClassDefinition) {
					object = ((SchemaClassDefinition) object).getSchemaClass();
					schemaObject = new SchemaObject(((SchemaClassDefinition) object).getSchemaClass(), null);
				} else if (object instanceof Class<?>) {
					schemaObject = new SchemaObject(Roma.schema().getSchemaClass(object), null);
				} else
					schemaObject = new SchemaObject(Roma.schema().getSchemaClass(object), object);
				so.put(object, schemaObject);
			}
			return schemaObject;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public List<SchemaObject> getSchemaObjects(SchemaClass schemaClass) {
		synchronized (getActiveSessionInfo()) {
			Map<Object, SchemaObject> so = getSchemaObjectMap();
			List<SchemaObject> objects = new ArrayList<SchemaObject>();
			for (SchemaObject schemaObject : so.values()) {
				if (schemaObject.getSchemaClass().isAssignableAs(schemaClass)) {
					objects.add(schemaObject);
				}
			}
			return objects;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public <T> T getObject(SchemaClass clazz) {
		synchronized (getActiveSessionInfo()) {
			Map<SchemaClass, Object> map = getObjectMap(getActiveSessionInfo());
			T value = (T) map.get(clazz);
			if (value == null) {
				try {
					value = (T) clazz.newInstance();
				} catch (Exception e) {
					throw new RuntimeException("Error on object instanziation", e);
				}
			}
			return value;
		}
	}

	/**
	 * When update an SchemaClass remove all instances of this from all caches.
	 * 
	 */
	public void signalUpdatedClass(SchemaClass iClass, File iFile) {
		for (SessionInfo info : getSessionInfos()) {
			synchronized (info) {
				Map<SchemaClass, Object> map = getObjectMap(info);
				List<SchemaClass> objects = new ArrayList<SchemaClass>();
				for (SchemaClass schemaClass : map.keySet()) {
					if (schemaClass.isAssignableAs(iClass)) {
						objects.add(schemaClass);
					}
				}
				for (SchemaClass schemaClass : objects) {
					map.remove(schemaClass);
				}
			}
		}

	}

	/**
	 * {@inheritDoc}
	 */
	public <T> T getObject(Class<T> clazz) {
		return getObject(Roma.schema().getSchemaClass(clazz));
	}

	/**
	 * {@inheritDoc}
	 */
	public <T> T getObject(String name) {
		return getObject(Roma.schema().getSchemaClass(name));
	}

	/**
	 * {@inheritDoc}
	 */
	public String aspectName() {
		return ASPECT_NAME;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public <T extends SessionAccount> T getAccount() {
		return (T) getActiveSessionInfo().getAccount();
	}

	/**
	 * 
	 * logout and destroy the user session
	 * 
	 */
	public void logout() {
		Roma.component(ApplicationConfiguration.class).destroyUserSession();
	}

}
