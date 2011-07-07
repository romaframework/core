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

package org.romaframework.aspect.hook.feature;

import org.romaframework.aspect.hook.HookAspect;
import org.romaframework.aspect.hook.annotation.HookScope;
import org.romaframework.core.schema.Feature;
import org.romaframework.core.schema.FeatureType;

public class HookActionFeatures {

	public static final Feature<HookScope>	SCOPE										= new Feature<HookScope>(HookAspect.ASPECT_NAME, "scope", FeatureType.ACTION, HookScope.class);
	public static final Feature<String>			HOOK_AROUND_ACTION			= new Feature<String>(HookAspect.ASPECT_NAME, "hookAroundAction", FeatureType.ACTION, String.class);
	public static final Feature<String>			HOOK_BEFORE_ACTION			= new Feature<String>(HookAspect.ASPECT_NAME, "hookBeforeAction", FeatureType.ACTION, String.class);
	public static final Feature<String>			HOOK_AFTER_ACTION				= new Feature<String>(HookAspect.ASPECT_NAME, "hookAfterAction", FeatureType.ACTION, String.class);
	public static final Feature<String>			HOOK_AROUND_FIELD_READ	= new Feature<String>(HookAspect.ASPECT_NAME, "hookAroundFieldRead", FeatureType.ACTION, String.class);
	public static final Feature<String>			HOOK_BEFORE_FIELD_READ	= new Feature<String>(HookAspect.ASPECT_NAME, "hookBeforeFieldRead", FeatureType.ACTION, String.class);
	public static final Feature<String>			HOOK_AFTER_FIELD_READ		= new Feature<String>(HookAspect.ASPECT_NAME, "hookAfterFieldRead", FeatureType.ACTION, String.class);
	public static final Feature<String>			HOOK_AROUND_FIELD_WRITE	= new Feature<String>(HookAspect.ASPECT_NAME, "hookAroundFieldWrite", FeatureType.ACTION, String.class);
	public static final Feature<String>			HOOK_BEFORE_FIELD_WRITE	= new Feature<String>(HookAspect.ASPECT_NAME, "hookBeforeFieldWrite", FeatureType.ACTION, String.class);
	public static final Feature<String>			HOOK_AFTER_FIELD_WRITE	= new Feature<String>(HookAspect.ASPECT_NAME, "hookAfterFieldWrite", FeatureType.ACTION, String.class);
	public static final Feature<String>			HOOK_AROUND_EVENT				= new Feature<String>(HookAspect.ASPECT_NAME, "hookAroundEvent", FeatureType.ACTION, String.class);
	public static final Feature<String>			HOOK_BEFORE_EVENT				= new Feature<String>(HookAspect.ASPECT_NAME, "hookBeforeEvent", FeatureType.ACTION, String.class);
	public static final Feature<String>			HOOK_AFTER_EVENT				= new Feature<String>(HookAspect.ASPECT_NAME, "hookAfterEvent", FeatureType.ACTION, String.class);
}
