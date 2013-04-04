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
import org.romaframework.core.Roma;
import org.romaframework.core.exception.ConfigurationException;

/**
 * Abstract base Application entry-point.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 * 
 */
public abstract class AbstractApplicationConfiguration extends Configurable<String> implements ApplicationConfiguration {
	
	protected String	applicationName;
	protected String	applicationVersion	= "1.0";
	protected String	applicationPackage;
	protected boolean	applicationDevelopment;

	public void login(LoginListener iListener) {
		throw new ConfigurationException("No login configured. If you want to enable the auto-login feature please define CustomApplicationConfiguration.login(listener)");
	}


	/**
	 * {@inheritDoc}
	 */
	public String getApplicationName() {
		return applicationName;
	}

	/**
	 * 
	 * @param applicationName
	 */
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getApplicationVersion() {
		return applicationVersion;
	}

	/**
	 * 
	 * @param applicationVersion
	 */
	public void setApplicationVersion(String applicationVersion) {
		this.applicationVersion = applicationVersion;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getApplicationPackage() {
		return applicationPackage;
	}

	/**
	 * 
	 * @param applicationPackage
	 */
	public void setApplicationPackage(String applicationPackage) {
		this.applicationPackage = applicationPackage;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isApplicationDevelopment() {
		return applicationDevelopment;
	}

	/**
	 * @param applicationDevelopment
	 */
	public void setApplicationDevelopment(boolean applicationDevelopment) {
		this.applicationDevelopment = applicationDevelopment;
	}

	/**
	 * {@inheritDoc}
	 */
	public final void createUserSession() {
		try {
			Roma.context().create();
			startUserSession();
		} finally {
			Roma.context().destroy();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	public final void destroyUserSession() {
		try {
			Roma.context().create();
			endUserSession();
		} finally {
			Roma.context().destroy();
		}
	}

	/**
	 * @return String : {@link Serviceable#STATUS_UNKNOWN}
	 */
	public String getStatus() {
		return STATUS_UNKNOWN;
	}
}
