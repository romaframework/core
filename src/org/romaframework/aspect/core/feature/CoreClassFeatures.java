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

package org.romaframework.aspect.core.feature;

import org.romaframework.aspect.core.CoreAspect;
import org.romaframework.core.schema.Feature;
import org.romaframework.core.schema.FeatureType;
import org.romaframework.core.schema.SchemaClass;

public class CoreClassFeatures {

	public static final Feature<SchemaClass>	ENTITY				= new Feature<SchemaClass>(CoreAspect.ASPECT_NAME, "entity", FeatureType.CLASS, SchemaClass.class);
	public static final Feature<String>				ORDER_FIELDS	= new Feature<String>(CoreAspect.ASPECT_NAME, "orderFields", FeatureType.CLASS, String.class);
	public static final Feature<String>				ORDER_ACTIONS	= new Feature<String>(CoreAspect.ASPECT_NAME, "orderActions", FeatureType.CLASS, String.class);

}
