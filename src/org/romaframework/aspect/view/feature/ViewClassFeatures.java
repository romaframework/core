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
import org.romaframework.core.schema.xmlannotations.XmlFormAreaAnnotation;

public class ViewClassFeatures {
	public static final Feature<String>									DESCRIPTION				= new Feature<String>(ViewAspect.ASPECT_NAME, "description", FeatureType.CLASS, String.class);
	public static final Feature<String>									POSITION					= new Feature<String>(ViewAspect.ASPECT_NAME, "position", FeatureType.CLASS, String.class);
	public static final Feature<String>									RENDER						= new Feature<String>(ViewAspect.ASPECT_NAME, "render", FeatureType.CLASS, String.class);
	public static final Feature<String>									STYLE							= new Feature<String>(ViewAspect.ASPECT_NAME, "style", FeatureType.CLASS, String.class);
	public static final Feature<String>									LABEL							= new Feature<String>(ViewAspect.ASPECT_NAME, "label", FeatureType.CLASS, String.class);

	public static final Feature<Boolean>								EXPLICIT_ELEMENTS	= new Feature<Boolean>(ViewAspect.ASPECT_NAME, "explicitElements", FeatureType.CLASS, Boolean.class,
																																						Boolean.FALSE);
	public static final Feature<Integer>								COLUMNS						= new Feature<Integer>(ViewAspect.ASPECT_NAME, "columns", FeatureType.CLASS, Integer.class);
	public static final Feature<String>									ORDER_AREAS				= new Feature<String>(ViewAspect.ASPECT_NAME, "orderAreas", FeatureType.CLASS, String.class);
	public static final Feature<XmlFormAreaAnnotation>	FORM							= new Feature<XmlFormAreaAnnotation>(ViewAspect.ASPECT_NAME, "form", FeatureType.CLASS,
																																						XmlFormAreaAnnotation.class);
}
