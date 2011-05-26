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

package org.romaframework.aspect.core.feature;

import org.romaframework.core.util.DynaBean;

public class CoreActionFeatures extends DynaBean {

	private static final long	serialVersionUID	= -1371119906875820494L;

	public CoreActionFeatures() {
		defineAttribute(CALLBACK_ON_ACTION, null);
		defineAttribute(CALLBACK_BEFORE_ACTION, null);
		defineAttribute(CALLBACK_AFTER_ACTION, null);
		defineAttribute(CALLBACK_ON_FIELD_READ, null);
		defineAttribute(CALLBACK_BEFORE_FIELD_READ, null);
		defineAttribute(CALLBACK_AFTER_FIELD_READ, null);
		defineAttribute(CALLBACK_ON_FIELD_WRITE, null);
		defineAttribute(CALLBACK_BEFORE_FIELD_WRITE, null);
		defineAttribute(CALLBACK_AFTER_FIELD_WRITE, null);
	}

	public static final String	CALLBACK_ON_ACTION					= "callbackOnAction";
	public static final String	CALLBACK_BEFORE_ACTION			= "callbackBeforeAction";
	public static final String	CALLBACK_AFTER_ACTION				= "callbackAfterAction";
	public static final String	CALLBACK_ON_FIELD_READ			= "callbackOnFieldRead";
	public static final String	CALLBACK_BEFORE_FIELD_READ	= "callbackBeforeFieldRead";
	public static final String	CALLBACK_AFTER_FIELD_READ		= "callbackAfterFieldRead";
	public static final String	CALLBACK_ON_FIELD_WRITE			= "callbackOnFieldWrite";
	public static final String	CALLBACK_BEFORE_FIELD_WRITE	= "callbackBeforeFieldWrite";
	public static final String	CALLBACK_AFTER_FIELD_WRITE	= "callbackAfterFieldWrite";
}
