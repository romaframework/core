/*
 * Copyright 2006-2007 Luca Garulli (luca.garulli--at--assetdata.it)
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

package org.romaframework.core.install;

import org.romaframework.core.module.SelfRegistrantModule;

/**
 * Module to install the application the first time. Roma calls the install() method. If it returns true, then the application need
 * to be installed.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 * 
 */
public abstract class ApplicationInstaller extends SelfRegistrantModule {
	public static final String	MODULE_NAME	= "ApplicationInstaller";

	public String moduleName() {
		return MODULE_NAME;
	}

	/**
	 * Makes all the operation needed for the application installation
	 * 
	 * @return
	 */
	public abstract void install();

	/**
	 * Check if the installer was already performed.
	 * 
	 * @return true if already performed otherwise false.
	 */
	public abstract boolean checkInstall();

	@Override
	public void startup() throws RuntimeException {
		if (checkInstall()) {
			install();
		}
	}

	@Override
	public void shutdown() throws RuntimeException {
	}
}
