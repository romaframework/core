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

public class EnterpriseClassFeatures {

	public static final Feature<String>	ESBHOST			= new Feature<String>(EnterpriseAspectAbstract.ASPECT_NAME, "esbHost", FeatureType.CLASS, String.class, "");
	public static final Feature<Long>		ESBPORT			= new Feature<Long>(EnterpriseAspectAbstract.ASPECT_NAME, "esbPort", FeatureType.CLASS, Long.class, new Long(0));
	public static final Feature<String>	USERNAME		= new Feature<String>(EnterpriseAspectAbstract.ASPECT_NAME, "username", FeatureType.CLASS, String.class, "");
	public static final Feature<String>	PASSW				= new Feature<String>(EnterpriseAspectAbstract.ASPECT_NAME, "password", FeatureType.CLASS, String.class, "");
	public static final Feature<String>	BCADDRESS		= new Feature<String>(EnterpriseAspectAbstract.ASPECT_NAME, "consumerAddress", FeatureType.CLASS, String.class, "");
	public static final Feature<String>	WSDLADDRESS	= new Feature<String>(EnterpriseAspectAbstract.ASPECT_NAME, "wsdlAddress", FeatureType.CLASS, String.class, "");

}
