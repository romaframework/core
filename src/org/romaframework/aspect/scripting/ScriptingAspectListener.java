/*
 * Copyright 2009 Luca Garulli (luca.garulli--at--assetdata.it)
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
package org.romaframework.aspect.scripting;

import java.io.Reader;
import java.util.Map;

public interface ScriptingAspectListener {
	/**
	 * Callback called before a script is executed. You can change the current context or the code to be executed with a soft form of
	 * enhancement.
	 * 
	 * @return The code to execute. If there are not changes return the iScript received.
	 */
	public Reader onBeforeExecution(String iLanguage, Reader iScript, Map<String, Object> iContext);

	/**
	 * Callback called after the script was executed. You can change the current context returned by the script or the return value.
	 * 
	 * @return The return value of the script execution. If there are not changes return the same return value received as parameter
	 *         (iReturnedValue).
	 */
	public Object onAfterExecution(String iLanguage, Reader iScript, Map<String, Object> iContext, Object iReturnedValue);
}