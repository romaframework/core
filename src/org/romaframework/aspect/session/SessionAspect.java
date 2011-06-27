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
import java.util.Locale;

import org.romaframework.core.aspect.Aspect;
import org.romaframework.core.schema.SchemaObjectHandler;

public interface SessionAspect extends Aspect, SchemaObjectHandler {
	public static final String	ASPECT_NAME	= "session";

	public SessionInfo getActiveSessionInfo();

	public Object getActiveSystemSession();

	public SessionInfo getSession(Object iSystemSession);

	public SessionInfo addSession(Object iSession);

	public SessionInfo removeSession(Object iSession);

	public Collection<SessionInfo> getSessionInfos();

	public Locale getActiveLocale();

	public void setActiveLocale(Locale iLocale);

	public void destroyCurrentSession();

	public void destroyCurrentSession(Object iSystemSession);

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

	@Deprecated
	public void shutdown(Object iSystemSession);
}
