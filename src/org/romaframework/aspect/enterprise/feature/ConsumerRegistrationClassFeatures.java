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

package org.romaframework.aspect.enterprise.feature;

import org.romaframework.aspect.enterprise.EnterpriseAspectAbstract;
import org.romaframework.core.schema.Feature;
import org.romaframework.core.schema.FeatureType;

public class ConsumerRegistrationClassFeatures {

	public static final Feature<String>	SERVICE_DESC	= new Feature<String>(EnterpriseAspectAbstract.ASPECT_NAME, "serviceDesc", FeatureType.CLASS, String.class, "");
	public static final Feature<String>	REG_URI				= new Feature<String>(EnterpriseAspectAbstract.ASPECT_NAME, "registryURI", FeatureType.CLASS, String.class, "");
	public static final Feature<String>	USERNAME			= new Feature<String>(EnterpriseAspectAbstract.ASPECT_NAME, "username", FeatureType.CLASS, String.class, "");
	public static final Feature<String>	PASSW					= new Feature<String>(EnterpriseAspectAbstract.ASPECT_NAME, "password", FeatureType.CLASS, String.class, "");
	public static final Feature<String>	ORGANIZATION	= new Feature<String>(EnterpriseAspectAbstract.ASPECT_NAME, "organization", FeatureType.CLASS, String.class, "");
	public static final Feature<String>	AUTHOR				= new Feature<String>(EnterpriseAspectAbstract.ASPECT_NAME, "author", FeatureType.CLASS, String.class, "");
	public static final Feature<String>	ICONPATH			= new Feature<String>(EnterpriseAspectAbstract.ASPECT_NAME, "iconPath", FeatureType.CLASS, String.class, "");
	public static final Feature<String>	SERVICE_NAME	= new Feature<String>(EnterpriseAspectAbstract.ASPECT_NAME, "serviceName", FeatureType.CLASS, String.class, "");

}