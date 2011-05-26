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
package org.romaframework.core.component;

import java.util.Map;

import org.romaframework.core.aspect.Aspect;
import org.romaframework.core.config.ContextException;
import org.romaframework.core.config.Serviceable;

/**
 * Interface to manage components as POJO.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 * 
 */
public interface ComponentAspect extends Aspect, Serviceable {
	public static final String	ASPECT_NAME	= "component";

	/**
	 * Get a component configured in the Component Engine by its class.
	 * 
	 * @param iClass
	 *          Interface of component implementation
	 * @return the component if any or null if not found
	 */
	public <T> T getComponent(Class<T> iClass) throws ContextException;

	/**
	 * Get components configured in the Component Engine of class iComponentClass.
	 * 
	 * @param iClass
	 *          Interface of component implementation
	 * @return the map of components found.
	 */
	public Map<String, Object> getComponentsOfClass(Class<?> iComponentClass) throws ContextException;

	/**
	 * Get a component configured in the Component Engine by name.
	 * 
	 * @param iName
	 *          Component name
	 * @return the component if any or null if not found
	 */
	public <T> T getComponent(String iName) throws ContextException;

	/**
	 * Check if a component was configured in the Component Engine by its class.
	 * 
	 * @param iClass
	 *          Interface of component implementation
	 * @return true if was configured, otherwise null
	 */
	public boolean existComponent(Class<? extends Object> iClass);

	/**
	 * Check if a component was configured in the Component Engine.
	 * 
	 * @param iComponentName
	 *          Name of component to search
	 * @return true if was configured, otherwise null
	 */
	public boolean existComponent(String iComponentName);
}
