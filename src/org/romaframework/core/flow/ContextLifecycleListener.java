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

package org.romaframework.core.flow;

import java.util.Map;

/**
 * Listener interface to handle context access.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public interface ContextLifecycleListener {
	/**
	 * Callback called when the context is opened. Usually by the Roma Controller.
	 */
	public void onContextCreate();

	/**
	 * Callback called when the context is closed. Usually by the Roma Controller.
	 */
	public void onContextDestroy();

	/**
	 * Callback of an context block push.
	 * @param current 
	 * 
	 *
	 */
	public void onContextPush(Map<String, Object> current);

	/**
	 * Callback of an context block pop.
	 * @param current 
	 * 
	 */
	public void onContextPop(Map<String, Object> current);

	/**
	 * Callback called when a component needs to be created in the context.
	 * 
	 * @param iComponent
	 *          The Component requested
	 * @param iComponentInstance
	 *          TODO
	 * @return Return the object on context
	 */
	public Object onContextRead(String iComponent, Object iComponentInstance);
}
