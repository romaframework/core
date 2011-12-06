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

import org.romaframework.aspect.view.SelectionMode;
import org.romaframework.aspect.view.ViewAspect;
import org.romaframework.core.schema.Feature;
import org.romaframework.core.schema.FeatureType;
import org.romaframework.core.schema.SchemaClass;

public class ViewFieldFeatures {
	
	public static final Feature<String>					DESCRIPTION						= new Feature<String>(ViewAspect.ASPECT_NAME, "description", FeatureType.FIELD, String.class);
	public static final Feature<String>					POSITION								= new Feature<String>(ViewAspect.ASPECT_NAME, "position", FeatureType.FIELD, String.class);
	public static final Feature<String>					RENDER								= new Feature<String>(ViewAspect.ASPECT_NAME, "render", FeatureType.FIELD, String.class);
	public static final Feature<String>					STYLE									= new Feature<String>(ViewAspect.ASPECT_NAME, "style", FeatureType.FIELD, String.class);
	public static final Feature<String>					LABEL									= new Feature<String>(ViewAspect.ASPECT_NAME, "label", FeatureType.FIELD, String.class);
	public static final Feature<Boolean>				ENABLED								= new Feature<Boolean>(ViewAspect.ASPECT_NAME, "enabled", FeatureType.FIELD, Boolean.class, Boolean.TRUE);
	public static final Feature<Boolean>				VISIBLE								= new Feature<Boolean>(ViewAspect.ASPECT_NAME, "visible", FeatureType.FIELD, Boolean.class, Boolean.TRUE);

	public static final Feature<String[]>				DEPENDS_ON						= new Feature<String[]>(ViewAspect.ASPECT_NAME, "dependsOn", FeatureType.FIELD, String[].class);
	public static final Feature<String[]>				DEPENDS								= new Feature<String[]>(ViewAspect.ASPECT_NAME, "depends", FeatureType.FIELD, String[].class);
	public static final Feature<String>					SELECTION_FIELD				= new Feature<String>(ViewAspect.ASPECT_NAME, "selectionField", FeatureType.FIELD, String.class);
	public static final Feature<SelectionMode>	SELECTION_MODE				= new Feature<SelectionMode>(ViewAspect.ASPECT_NAME, "selectionMode", FeatureType.FIELD, SelectionMode.class,
																																				SelectionMode.SELECTION_MODE_VALUE);
	public static final Feature<String>					FORMAT								= new Feature<String>(ViewAspect.ASPECT_NAME, "format", FeatureType.FIELD, String.class);

	public static final Feature<SchemaClass>		DISPLAY_WITH					= new Feature<SchemaClass>(ViewAspect.ASPECT_NAME, "displayWith", FeatureType.FIELD, SchemaClass.class);

}
