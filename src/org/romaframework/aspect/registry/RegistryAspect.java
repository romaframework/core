/*
 * Copyright 2009 Luca Acquaviva (lacquaviva:at:imolinfo.it)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.romaframework.aspect.registry;

import java.util.HashMap;

import org.romaframework.core.aspect.Aspect;

public interface RegistryAspect extends Aspect {

	public static final String	ASPECT_NAME	= "registry";
	

	/**
	 * Register Services to the remote Registry
	 * 
	 * @param definitionMap
	 *          the HashMap that contains the wsdl relative to the annotated Class
	 **/
	public void registerServices(HashMap<Class<?>, Object> definitionMap);
	
	/**
	 * Register Services to the remote Registry
	 * 
	 * @param wsdlDef
	 *          wsdlDefinition to upl
	 * @param registyURL
	 *          the registry URLAddress
	 * @param username
	 *          the registry username
	 * @param password
	 *          the registry password
	 * @param iconpath
	 *          the service icon path 
	 * @param author
	 * 					the service author
	 * @param desc
	 * 					the service description
	 * @param serviceName
	 * 					the Consumer Service Name
	 **/
	public void registerDef(Object wsdlDef, String registryURL, String username, String password,String orgPackage, String iconPath, String author,
			String desc,String serviceName) throws Exception;

}
