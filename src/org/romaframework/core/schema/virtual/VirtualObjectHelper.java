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

package org.romaframework.core.schema.virtual;

import java.util.HashMap;
import java.util.Map;

import org.romaframework.aspect.scripting.exception.ScriptingException;
import org.romaframework.aspect.scripting.feature.ScriptingFeatures;
import org.romaframework.core.Roma;
import org.romaframework.core.util.DynaBean;

/**
 * Represent a method of a class.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public class VirtualObjectHelper {
	public static final String	DEFAULT_LANGUAGE	= "JavaScript";

	public static Object invoke(VirtualObject iContent, DynaBean features) throws ScriptingException {
		String language = (String) features.getAttribute(ScriptingFeatures.LANGUAGE);
		if (language == null)
			// SET THE DEFAULT LANGUAGE
			language = DEFAULT_LANGUAGE;

		Map<String, Object> context = new HashMap<String, Object>();
		context.put("me", new VirtualObjectWrapper(iContent));

		return Roma.scripting().evaluate(language, (String) features.getAttribute(ScriptingFeatures.CODE), context);
	}
}
