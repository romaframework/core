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

package org.romaframework.aspect.validation.feature;

import org.romaframework.core.util.DynaBean;

public class ValidationFieldFeatures extends DynaBean {
	public ValidationFieldFeatures() {
		defineAttribute(ENABLED, false);
		defineAttribute(REQUIRED, false);
		defineAttribute(MATCH, null);
		defineAttribute(MIN, null);
		defineAttribute(MAX, null);
	}

	public static final String	ENABLED		= "enabled";
	public static final String	REQUIRED	= "required";
	public static final String	MATCH			= "match";
	public static final String	MIN				= "min";
	public static final String	MAX				= "max";
}
