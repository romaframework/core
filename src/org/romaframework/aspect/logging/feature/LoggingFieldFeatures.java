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
package org.romaframework.aspect.logging.feature;

import org.romaframework.aspect.logging.LoggingAspect;
import org.romaframework.core.schema.Feature;
import org.romaframework.core.schema.FeatureType;

public class LoggingFieldFeatures {

	public static final Feature<Integer>	LEVEL							= new Feature<Integer>(LoggingAspect.ASPECT_NAME, "level", FeatureType.FIELD, Integer.class);
	public static final Feature<String>		CATEGORY					= new Feature<String>(LoggingAspect.ASPECT_NAME, "category", FeatureType.FIELD, String.class);

	public static final Feature<String>		EXCEPTION					= new Feature<String>(LoggingAspect.ASPECT_NAME, "exception", FeatureType.FIELD, String.class);
	@SuppressWarnings("rawtypes")
	public static final Feature<Class[]>	EXCEPTIONS_TO_LOG	= new Feature<Class[]>(LoggingAspect.ASPECT_NAME, "exceptionsToLog", FeatureType.FIELD, Class[].class);

	public static final Feature<String>		POST							= new Feature<String>(LoggingAspect.ASPECT_NAME, "post", FeatureType.FIELD, String.class);
	public static final Feature<String>		MODE							= new Feature<String>(LoggingAspect.ASPECT_NAME, "mode", FeatureType.FIELD, String.class);
	public static final Feature<Boolean>	ENABLED						= new Feature<Boolean>(LoggingAspect.ASPECT_NAME, "enabled", FeatureType.FIELD, Boolean.class);

}
