/*
 * Copyright 2009 Luigi Dell'Aquila (luigi.dellaquila@assetdata.it)
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
package org.romaframework.aspect.security.feature;

import org.romaframework.aspect.security.SecurityAspect;
import org.romaframework.core.schema.Feature;
import org.romaframework.core.schema.FeatureType;

public class SecurityFieldFeatures {

	public static final Feature<String[]>	READ_ROLES						= new Feature<String[]>(SecurityAspect.ASPECT_NAME, "readRoles", FeatureType.FIELD, String[].class);
	public static final Feature<String[]>	WRITE_ROLES						= new Feature<String[]>(SecurityAspect.ASPECT_NAME, "writeRoles", FeatureType.FIELD, String[].class);
	public static final Feature<Boolean>	ENCRYPT								= new Feature<Boolean>(SecurityAspect.ASPECT_NAME, "writeRoles", FeatureType.FIELD, Boolean.class);
	public static final Feature<String>		ENCRYPTION_ALGORITHM	= new Feature<String>(SecurityAspect.ASPECT_NAME, "encryptionAlgorithm", FeatureType.FIELD, String.class);

}
