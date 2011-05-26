/*
 * Copyright 2006-2007 Giordano Maestro (giordano.maestro--at--assetdata.it)
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
package org.romaframework.core.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("unchecked")
public class ListenerManager<R> {

	private HashMap<R, List<?>>	listeners;

	public ListenerManager() {
		listeners = new HashMap<R, List<?>>();
	}

	/*
	 * Get the listeners registered for a given type
	 */
	public <T> List<T> getListeners(R iType) {
		List<?> classListeners = listeners.get(iType);
		if (classListeners == null)
			classListeners = registerListenerType(iType);

		List<T> result = new ArrayList<T>();
		synchronized (listeners) {
			for (Object ref : classListeners) {
				result.add((T) ref);
			}
		}
		return result;
	}

	/**
	 * Register a listener giving a type
	 * 
	 * @param iType
	 *          Listener type
	 * @param iInstance
	 *          Listener instance
	 */
	public <T> void registerListener(R iType, T iInstance) {
		synchronized (listeners) {
			List<T> listenersForType = (List<T>) listeners.get(iType);
			if (listenersForType == null)
				listenersForType = registerListenerType(iType);

			listenersForType.add(iInstance);
		}
	}

	private <T> List<T> registerListenerType(R iType) {
		List<T> listenersForType = new ArrayList<T>();
		listeners.put(iType, listenersForType);
		return listenersForType;
	}

	/**
	 * Unregister a listener for a given type.
	 * 
	 * @param iType
	 *          Listener type
	 * @param iInstance
	 *          Listener instance
	 */
	public <T> void unregisterListener(R iType, T iInstance) {
		synchronized (listeners) {
			List<?> listenersForType = listeners.get(iType);
			if (listenersForType != null) {
				for (int i = 0; i < listenersForType.size(); ++i) {
					Object ref = listenersForType.get(i);
					if (ref != null && ref.equals(iInstance)) {
						listenersForType.remove(i);
						break;
					}
				}
			}
		}
	}
}
