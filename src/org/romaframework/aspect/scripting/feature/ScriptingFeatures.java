/*
 * Copyright 2006-2009 Luca Garulli (luca.garulli--at--assetdata.it)
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
package org.romaframework.aspect.scripting.feature;

import org.romaframework.aspect.scripting.ScriptingAspect;
import org.romaframework.core.schema.Feature;
import org.romaframework.core.schema.FeatureType;

public class ScriptingFeatures {
	public static final Feature<String>	LANGUAGE	= new Feature<String>(ScriptingAspect.ASPECT_NAME, "language", FeatureType.ACTION, String.class);
	public static final Feature<String>	CODE			= new Feature<String>(ScriptingAspect.ASPECT_NAME, "code", FeatureType.ACTION, String.class);
}
