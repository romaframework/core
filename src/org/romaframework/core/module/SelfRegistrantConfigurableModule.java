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

import java.util.List;

import org.romaframework.core.Utility;
import org.romaframework.core.config.Configurable;
import org.romaframework.core.handler.RomaObjectHandler;
import org.romaframework.core.schema.SchemaClass;

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

	/**
	 * Default implementation that tell to the caller that the user object is not handled by this module. Override this method to
	 * manage it.
	 * 
	 * @param iUserObject
	 *          The user POJO to get the handler
	 * @return always null
	 */
	public RomaObjectHandler getObjectHandler(Object iUserObject) {
		return null;
	}

	/**
	 * Return the ObjectHandler instances that handle objects of type iClass
	 * 
	 * @param iiClass
	 *          The SchemaClass instance to get the instances
	 * @return the ObjectHandler instances that handle objects of type iClass if is handled by this module otherwise null
	 */
	public List<RomaObjectHandler> getObjectHandlers(SchemaClass iClass) {
		return null;
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
