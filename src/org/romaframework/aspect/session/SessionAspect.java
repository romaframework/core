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

import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.romaframework.core.aspect.Aspect;
import org.romaframework.core.schema.SchemaClass;
import org.romaframework.core.schema.SchemaObject;
import org.romaframework.core.schema.SchemaObjectHandler;

public interface SessionAspect extends Aspect, SchemaObjectHandler {
	public static final String	ASPECT_NAME	= "session";

	public SessionInfo getActiveSessionInfo();

	public Object getActiveSystemSession();

	public SessionInfo getSession(Object iSystemSession);

	public SessionInfo addSession(Object iSession);

	public SessionInfo removeSession(Object iSession);

	public Collection<SessionInfo> getSessionInfos();

	public void invalidateSession(Object iSystemSession);

	/**
	 * Invalidate the current session.
	 */
	public void invalidate();

	/**
	 * Retrieve an instance of a specified class from session, if not exist create new.
	 * 
	 * @param clazz
	 *          the type of class to retrieve.
	 * @return the new instance.
	 */
	public <T> T getObject(Class<T> clazz);

	/**
	 * Retrieve an instance of a specified SchemaClass from session, if not exist create new.
	 * 
	 * @param clazz
	 *          the type of class to retrieve.
	 * @return the new instance.
	 */
	public <T> T getObject(SchemaClass clazz);

	/**
	 * Retrieve an instance of specified class name from session,if not exist create new.
	 * 
	 * @param name
	 *          of class instance to create
	 * @return the new instance.
	 */
	public <T> T getObject(String name);

	/**
	 * Retrieve the current session locale.
	 * 
	 * @return the locale for the session.
	 */
	public Locale getActiveLocale();

	/**
	 * Set the locale for the current session.
	 * 
	 * @param iLocale
	 *          to set.
	 */
	public void setActiveLocale(Locale iLocale);

	/**
	 * Get a session context attribute value.
	 * 
	 * @param iSession
	 *          User Session
	 * @param iKey
	 *          Attribute name
	 * @return Attribute value
	 */
	public <T> T getProperty(Object iSession, String iKey);

	/**
	 * Get an attribute value from the current session.
	 * 
	 * @param iKey
	 *          attribute name
	 * @return Attribute value
	 */
	public <T> T getProperty(String iKey);

	/**
	 * Set an attribute in a User Session giving a name and a value.
	 * 
	 * @param iKey
	 *          attribute name
	 * @param iValue
	 *          attribute value
	 */
	public <T> void setProperty(Object iSession, String iKey, T iValue);

	/**
	 * Set an attribute in the current Session giving a name and a value.
	 * 
	 * @param iKey
	 *          attribute name
	 * @param iValue
	 *          attribute value
	 */
	public <T> void setProperty(String iKey, T iValue);

	/**
	 * Retrieve all SchemaObjects in the current session can assign to this schemaClass.
	 * 
	 * @param schemaClass
	 *          where find instances.
	 * @return found instances of this schema class.
	 */
	public List<SchemaObject> getSchemaObjects(SchemaClass schemaClass);
	
	/**
	 * sets session timeout 
	 * @param mins number of minutes to timeout
	 */
	public void setTimeout(int mins);
	
	/**
	 * returns sesson timeout in minutes
	 * @return the session timeout in minutes
	 */
	public int getTimeout();
}
