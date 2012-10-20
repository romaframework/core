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

package org.romaframework.core.module;

import org.romaframework.core.Utility;
import org.romaframework.core.config.Configurable;

/**
 * Base abstract class to define a self registrant module. This class is able to be configured. Extend this class to make your
 * module implementation.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 * 
 */
public abstract class SelfRegistrantConfigurableModule<T> extends Configurable<T> implements Module {
	
	protected String	status;

	protected SelfRegistrantConfigurableModule() {
		ModuleManager.getInstance().register(this);
	}

	/**
	 * Default implementation that do nothing. Override this to provide a configuration for the module.
	 */
	public void showConfiguration() {
	}

	public void shutdown() throws RuntimeException {
		status = STATUS_DOWN;
	}

	public void startup() throws RuntimeException {
		status = STATUS_UP;
	}

	public String moduleName() {
		return Utility.getClassName(getClass());
	}

	public String getStatus() {
		return status;
	}
}
