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

import org.romaframework.core.schema.Feature;

public abstract class ViewElementFeatures {

	public static final Feature<String>		DESCRIPTION	= ViewFieldFeatures.DESCRIPTION;
	public static final Feature<String>		LAYOUT			= ViewFieldFeatures.LAYOUT;
	public static final Feature<String>		RENDER			= ViewFieldFeatures.RENDER;
	public static final Feature<String>		STYLE				= ViewFieldFeatures.STYLE;
	public static final Feature<String>		LABEL				= ViewFieldFeatures.LABEL;
	public static final Feature<Boolean>	ENABLED			= ViewFieldFeatures.ENABLED;
	public static final Feature<Boolean>	VISIBLE			= ViewFieldFeatures.VISIBLE;

}
