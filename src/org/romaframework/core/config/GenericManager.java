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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.romaframework.core.Utility;
import org.springframework.util.StringUtils;

/**
 * Generic Manager of components instantiable in one shot by setComponents method (usually in configuration using Spring) and by
 * addComponent method at run-time.
 * 
 * Components will be registered with the name given from toString() call.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 * 
 */
public class GenericManager<C> {
	public GenericManager() {
		components = new HashMap<String, Class<C>>();
	}

	/**
	 * Setup multiple renders at startup
	 * 
	 * @param iComponentClassNames
	 *          List of FrontEndRender instances to register.
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public void setComponents(List<String> iComponentClassNames) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		componentClassNames = iComponentClassNames;
	}

	protected void setupComponents() {
		String componentName;
		for (Iterator<String> it = componentClassNames.iterator(); it.hasNext();) {
			componentName = it.next();

			if (componentName == null || componentName.length() == 0)
				continue;

			componentName.replace('\t', ' ');
			componentName.replace('\n', ' ');
			componentName = componentName.trim();
			componentName = StringUtils.trimLeadingWhitespace(componentName);

			try {
				addComponent(componentName);
			} catch (Exception e) {
				log.error("[ComponentManager.setupComponents] Error" + e);
			}
		}
		componentClassNames = null;
	}

	/**
	 * Register a single Manageable
	 * 
	 * @param iComponentClassName
	 *          Manageable's class name
	 */
	@SuppressWarnings("unchecked")
	public void addComponent(String iComponentClassName) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		addComponent((Class<C>) Class.forName(iComponentClassName));
	}

	/**
	 * Register a single Manageable
	 * 
	 * @param iComponentClass
	 *          Manageable's class
	 */
	public void addComponent(Class<C> iComponentClass) throws InstantiationException, IllegalAccessException {
		addComponent(Utility.getClassName(iComponentClass), iComponentClass);
	}

	/**
	 * Register a single Manageable
	 * 
	 * @param iComponentClass
	 *          Manageable's class
	 */
	public void addComponent(String iComponentName, Class<C> iComponentClass) throws InstantiationException, IllegalAccessException {
		// REGISTER THE COMPONENT INSTANCE WITH ITS NAME (GIVEN FROM toString()
		// METHOD)
		components.put(iComponentName, iComponentClass);
	}

	/**
	 * Get the render by the key
	 * 
	 * @param iKey
	 *          String that identify the render (the same that FrontEndRender instance uses to register to the PluginManager
	 * @return FrontEndRender if found, otherwise null
	 */
	public Class<C> getComponent(String iKey) {
		if (componentClassNames != null) {
			synchronized (components) {
				if (componentClassNames != null)
					setupComponents();
			}
		}
		return components.get(iKey);
	}

	protected List<String>					componentClassNames;
	protected Map<String, Class<C>>	components;

	protected static Log						log	= LogFactory.getLog(GenericManager.class);
}
