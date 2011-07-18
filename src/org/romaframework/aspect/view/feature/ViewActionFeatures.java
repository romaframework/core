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

package org.romaframework.aspect.view.feature;

import org.romaframework.aspect.view.ViewAspect;
import org.romaframework.core.schema.Feature;
import org.romaframework.core.schema.FeatureType;

public class ViewActionFeatures {

	public static final Feature<String>		DESCRIPTION	= new Feature<String>(ViewAspect.ASPECT_NAME, "description", FeatureType.ACTION, String.class);
	public static final Feature<String>		LAYOUT			= new Feature<String>(ViewAspect.ASPECT_NAME, "layout", FeatureType.ACTION, String.class);
	public static final Feature<String>		RENDER			= new Feature<String>(ViewAspect.ASPECT_NAME, "render", FeatureType.ACTION, String.class);
	public static final Feature<String>		STYLE				= new Feature<String>(ViewAspect.ASPECT_NAME, "style", FeatureType.ACTION, String.class);
	public static final Feature<String>		LABEL				= new Feature<String>(ViewAspect.ASPECT_NAME, "label", FeatureType.ACTION, String.class);
	public static final Feature<Boolean>	ENABLED			= new Feature<Boolean>(ViewAspect.ASPECT_NAME, "enabled", FeatureType.ACTION, Boolean.class);
	public static final Feature<Boolean>	VISIBLE			= new Feature<Boolean>(ViewAspect.ASPECT_NAME, "visible", FeatureType.ACTION, Boolean.class, Boolean.TRUE);
	public static final Feature<Boolean>	BIND				= new Feature<Boolean>(ViewAspect.ASPECT_NAME, "bind", FeatureType.ACTION, Boolean.class);
	public static final Feature<Boolean>	SUBMIT			= new Feature<Boolean>(ViewAspect.ASPECT_NAME, "submit", FeatureType.ACTION, Boolean.class);

}
