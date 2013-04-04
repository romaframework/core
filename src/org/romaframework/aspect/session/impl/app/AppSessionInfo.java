/*
 * Copyright 2006-2008 Luca Garulli (luca.garulli--at--assetdata.it)
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
package org.romaframework.aspect.session.impl.app;

import java.util.HashMap;
import java.util.Locale;

import org.romaframework.aspect.session.SessionInfo;

/**
 * Applicative SessionInfo extension. It stores the Locale configuration and custom properties inside the object. <br/>
 * This class is used by AppSessionManager.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 * @see AppSessionManager
 * 
 */
public class AppSessionInfo extends SessionInfo {
	
	protected HashMap<String, Object>	properties;
	protected Locale									locale;

	/**
	 * 
	 * @param id
	 */
	public AppSessionInfo(Object id) {
		super(id);
	}

	/**
	 * Retrieves a property from the object. <code>null</code> if does not exist
	 * <br><b>The operation is synchronized</b>
	 * 
	 * @param key
	 * @return Object
	 */
	public synchronized Object getProperty(String key) {
		return properties == null ? null : properties.get(key);
	}

	/**
	 * Adds a new property within the session object.
	 * <br><b>The operation is synchronized</b>
	 * 
	 * 
	 * @param key
	 * @param value
	 */
	public synchronized void setProperty(String key, Object value) {
		if (properties == null)
			properties = new HashMap<String, Object>();

		properties.put(key, value);
	}

	/**
	 * 
	 * @return Locale
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * 
	 * @param locale
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}
}
