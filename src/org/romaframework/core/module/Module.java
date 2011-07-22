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

import org.romaframework.core.config.Serviceable;

/**
 * Interface to define a module.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public interface Module extends Serviceable {
	/**
	 * Return the module name.
	 * 
	 * @return The module name
	 */
	public String moduleName();

	/**
	 * Display the module's configuration
	 */
	public void showConfiguration();
}
