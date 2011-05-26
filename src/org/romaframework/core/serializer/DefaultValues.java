/*
 * Copyright 2007 Giordano Maestro (giordano.maestro--at--assetdata.it)
 * 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 * 
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.romaframework.core.serializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Register the default values of any aspect
 * 
 * @author Giordano Maestro (giordano.maestro--at--assetdata.it) 09/nov/07
 * 
 */
public final class DefaultValues {
	private static Map<String, Map<String, Object>>	initialValues	= new HashMap<String, Map<String, Object>>();

	private static DefaultValues										instance			= new DefaultValues();

	private DefaultValues() {
	}

	public static DefaultValues getInstance() {
		return instance;
	}

	/**
	 * Set the the default values for an aspect
	 * 
	 * @param aspectName
	 * @param defaultValues
	 */
	public void setDefaultValues(String aspectName, Map<String, Object> defaultValues) {
		if (initialValues.get(aspectName) == null) {
			initialValues.put(aspectName, defaultValues);
		}
	}

	/**
	 * Get the default values for an aspect features
	 * 
	 * @param iAspect
	 * @param feature
	 * @return
	 */
	public Object getDefaultValue(String iAspect, String feature) {
		Map<String, Object> map = initialValues.get(iAspect);
		if (map != null) {
			return map.get(feature);
		} else {
			return null;
		}
	}
}
