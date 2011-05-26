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

package org.romaframework.core.config;

import org.romaframework.core.Utility;

@SuppressWarnings("unchecked")
public class ComponentFactory<T> extends ComponentManager<Class<T>> {

	public void addComponent(final String iName, final Class<? extends T> iClass) {
		// REGISTER THE COMPONENT INSTANCE WITH ITS NAME (GIVEN FROM toString()
		// METHOD)
		components.put(iName, (Class<T>) iClass);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class<T> addComponent(final Class iClass) {
		// REGISTER THE COMPONENT INSTANCE WITH ITS NAME (GIVEN FROM toString()
		// METHOD)
		components.put(Utility.getClassName(iClass), iClass);
		return iClass;
	}

	public T getComponentInstance(final String iKey) throws InstantiationException, IllegalAccessException {
		return super.getComponent(iKey).newInstance();
	}
}
