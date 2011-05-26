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

package org.romaframework.aspect.logging.feature;

import org.romaframework.core.schema.Feature;

public class LoggingElementFeatures {

	public static final Feature<Integer>	LEVEL							= LoggingFieldFeatures.LEVEL;
	public static final Feature<String>		CATEGORY					= LoggingFieldFeatures.CATEGORY;

	public static final Feature<String>		EXCEPTION					= LoggingFieldFeatures.EXCEPTION;
	public static final Feature<Class[]>	EXCEPTIONS_TO_LOG	= LoggingFieldFeatures.EXCEPTIONS_TO_LOG;

	public static final Feature<String>		POST							= LoggingFieldFeatures.POST;
	public static final Feature<String>		MODE							= LoggingFieldFeatures.MODE;
	public static final Feature<Boolean>	ENABLED						= LoggingFieldFeatures.ENABLED;

}
