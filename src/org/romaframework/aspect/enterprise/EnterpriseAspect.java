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
package org.romaframework.aspect.enterprise;

import java.util.HashMap;

import org.romaframework.core.aspect.Aspect;

public interface EnterpriseAspect extends Aspect {

	public static final String	ASPECT_NAME	= "enterprise";

	/**
	 * Expose Services to the remote Registry
	 * 
	 * @param definitionMap
	 *          the HashMap that contains the wsdl relative to the annotated Class
	 **/
	public void exposeServices(HashMap<Class<?>, Object> definitionMap);

}
