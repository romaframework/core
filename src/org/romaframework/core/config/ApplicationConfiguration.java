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

package org.romaframework.core.config;

import org.romaframework.aspect.authentication.LoginListener;

/**
 * End-User Application entry-point.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 * 
 */
public interface ApplicationConfiguration extends Serviceable {
	
	/**
	 * Create a new user session.
	 */
	public void createUserSession();

	/**
	 * Destroy a user session.
	 */
	public void destroyUserSession();

	public void startUserSession();

	public void endUserSession();

	/**
	 * @return String : the application name
	 */
	public String getApplicationName();

	/**
	 * 
	 * @return String 
	 */
	public String getApplicationVersion();

	/**
	 * 
	 * @return String
	 */
	public String getApplicationPackage();

	/**
	 * 
	 * @return boolean
	 */
	public boolean isApplicationDevelopment();

	/**
	 * 
	 * @param applicationDevelopment
	 */
	public void setApplicationDevelopment(boolean applicationDevelopment);

	/**
	 * 
	 * @param iKey
	 * @return String
	 */
	public String getConfiguration(String iKey);

	public void login(LoginListener iListener);
}
