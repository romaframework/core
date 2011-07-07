/*
 * Copyright 2006 Giordano Maestro (giordano.maestro--at--assetdata.it)
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
package org.romaframework.aspect.service.feature;

import org.romaframework.aspect.service.ServiceAspectAbstract;
import org.romaframework.core.schema.Feature;
import org.romaframework.core.schema.FeatureType;

public class ServiceClassFeatures {
	public static final Feature<Class>	INTERFACE_CLASS				= new Feature<Class>(ServiceAspectAbstract.ASPECT_NAME, "interfaceClass", FeatureType.CLASS, Class.class);
	public static final Feature<String>	SERVICE_NAME					= new Feature<String>(ServiceAspectAbstract.ASPECT_NAME, "serviceName", FeatureType.CLASS, String.class, "");
	public static final Feature<Class>	ASPECT_IMPLEMENTATION	= new Feature<Class>(ServiceAspectAbstract.ASPECT_NAME, "aspectImplementation", FeatureType.CLASS, Class.class);

}
