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

/**
 * This class defines the features for the "actions" regarding the view aspect, defining their appearence, look and feel.    
 *
 */
public class ViewActionFeatures {
	

	public static final Feature<String>		DESCRIPTION	= new Feature<String>(ViewAspect.ASPECT_NAME, "description", FeatureType.ACTION, String.class);
	
	/**
	 * Contains the string identifying the area where this action must be rendered in. Due to partial evolution of the framework, the area must be specified with a 'form://' 
	 * string at the beginning. Note that while it resembles a XPath path, it is not and the destination area must be declared with just its name, without the whole path.
	 * Example: if the area is called "someActionsArea", the position must be "form://someActionsArea", even if this area is contained in other areas, as usually happens.
	 */
	public static final Feature<String>		POSITION		= new Feature<String>(ViewAspect.ASPECT_NAME, "position", FeatureType.ACTION, String.class);
	
	/**
	 * Contains the string identifying the renderer used for the action.
	 */
	public static final Feature<String>		RENDER			= new Feature<String>(ViewAspect.ASPECT_NAME, "render", FeatureType.ACTION, String.class);
	
	/**
	 * Contains a specific style: this will be matched with a CSS item with the same name. 
	 */
	public static final Feature<String>		STYLE				= new Feature<String>(ViewAspect.ASPECT_NAME, "style", FeatureType.ACTION, String.class);
	
	/**
	 * Contains the text that will appear on the action, on the button or link (it depends on the render) 
	 */
	public static final Feature<String>		LABEL				= new Feature<String>(ViewAspect.ASPECT_NAME, "label", FeatureType.ACTION, String.class);
	
	/**
	 * If not enabled, the action wont be clickable or triggerable.
	 */
	public static final Feature<Boolean>	ENABLED			= new Feature<Boolean>(ViewAspect.ASPECT_NAME, "enabled", FeatureType.ACTION, Boolean.class);
	
	/**
	 * Sets the visibility of the action. 
	 */
	public static final Feature<Boolean>	VISIBLE			= new Feature<Boolean>(ViewAspect.ASPECT_NAME, "visible", FeatureType.ACTION, Boolean.class, Boolean.TRUE);
	public static final Feature<Boolean>	BIND				= new Feature<Boolean>(ViewAspect.ASPECT_NAME, "bind", FeatureType.ACTION, Boolean.class);
	public static final Feature<Boolean>	SUBMIT			= new Feature<Boolean>(ViewAspect.ASPECT_NAME, "submit", FeatureType.ACTION, Boolean.class);

}


