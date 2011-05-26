/*
 * Copyright 2006 Luca Garulli (luca.garulli--at--assetdata.it) Licensed under the
 * Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package org.romaframework.aspect.core.feature;

import org.romaframework.aspect.core.CoreAspect;
import org.romaframework.core.schema.Feature;
import org.romaframework.core.schema.FeatureType;

public class CoreActionFeatures {

	public static final Feature<String>	CALLBACK_ON_ACTION					= new Feature<String>(CoreAspect.ASPECT_NAME, "callbackOnAction", FeatureType.ACTION,
																																			String.class);
	public static final Feature<String>	CALLBACK_BEFORE_ACTION			= new Feature<String>(CoreAspect.ASPECT_NAME, "callbackBeforeAction", FeatureType.ACTION,
																																			String.class);
	public static final Feature<String>	CALLBACK_AFTER_ACTION				= new Feature<String>(CoreAspect.ASPECT_NAME, "callbackAfterAction", FeatureType.ACTION,
																																			String.class);
	public static final Feature<String>	CALLBACK_ON_FIELD_READ			= new Feature<String>(CoreAspect.ASPECT_NAME, "callbackOnFieldRead", FeatureType.ACTION,
																																			String.class);
	public static final Feature<String>	CALLBACK_BEFORE_FIELD_READ	= new Feature<String>(CoreAspect.ASPECT_NAME, "callbackBeforeFieldRead", FeatureType.ACTION,
																																			String.class);
	public static final Feature<String>	CALLBACK_AFTER_FIELD_READ		= new Feature<String>(CoreAspect.ASPECT_NAME, "callbackAfterFieldRead", FeatureType.ACTION,
																																			String.class);
	public static final Feature<String>	CALLBACK_ON_FIELD_WRITE			= new Feature<String>(CoreAspect.ASPECT_NAME, "callbackOnFieldWrite", FeatureType.ACTION,
																																			String.class);
	public static final Feature<String>	CALLBACK_BEFORE_FIELD_WRITE	= new Feature<String>(CoreAspect.ASPECT_NAME, "callbackBeforeFieldWrite", FeatureType.ACTION,
																																			String.class);
	public static final Feature<String>	CALLBACK_AFTER_FIELD_WRITE	= new Feature<String>(CoreAspect.ASPECT_NAME, "callbackAfterFieldWrite", FeatureType.ACTION,
																																			String.class);
}
