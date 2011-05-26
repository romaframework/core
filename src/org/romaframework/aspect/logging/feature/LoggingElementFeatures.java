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

package org.romaframework.aspect.logging.feature;

public class LoggingElementFeatures extends LoggingClassFeatures {
	public LoggingElementFeatures() {
		defineAttribute(LEVEL, null);
		defineAttribute(CATEGORY, null);

		defineAttribute(EXCEPTION, null);
		defineAttribute(EXCEPTIONS_TO_LOG, null);
		defineAttribute(POST, null);
	}

	public static final String	LEVEL							= "level";
	public static final String	CATEGORY					= "category";

	public static final String	EXCEPTION					= "exception";
	public static final String	EXCEPTIONS_TO_LOG	= "exceptionsToLog";

	public static final String	POST							= "post";

}
