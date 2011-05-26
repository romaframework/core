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

public class HookActionFeatures extends HookElementFeatures {

	private static final long	serialVersionUID	= -1371119906875820494L;

	public HookActionFeatures() {
		defineAttribute(HOOK_AROUND_ACTION, null);
		defineAttribute(HOOK_BEFORE_ACTION, null);
		defineAttribute(HOOK_AFTER_ACTION, null);
		defineAttribute(HOOK_AROUND_FIELD_READ, null);
		defineAttribute(HOOK_BEFORE_FIELD_READ, null);
		defineAttribute(HOOK_AFTER_FIELD_READ, null);
		defineAttribute(HOOK_AROUND_FIELD_WRITE, null);
		defineAttribute(HOOK_BEFORE_FIELD_WRITE, null);
		defineAttribute(HOOK_AFTER_FIELD_WRITE, null);
		defineAttribute(HOOK_AROUND_EVENT, null);
		defineAttribute(HOOK_BEFORE_EVENT, null);
		defineAttribute(HOOK_AFTER_EVENT, null);
	}

	public static final String	HOOK_AROUND_ACTION			= "hookAroundAction";
	public static final String	HOOK_BEFORE_ACTION			= "hookBeforeAction";
	public static final String	HOOK_AFTER_ACTION				= "hookAfterAction";
	public static final String	HOOK_AROUND_FIELD_READ	= "hookAroundFieldRead";
	public static final String	HOOK_BEFORE_FIELD_READ	= "hookBeforeFieldRead";
	public static final String	HOOK_AFTER_FIELD_READ		= "hookAfterFieldRead";
	public static final String	HOOK_AROUND_FIELD_WRITE	= "hookAroundFieldWrite";
	public static final String	HOOK_BEFORE_FIELD_WRITE	= "hookBeforeFieldWrite";
	public static final String	HOOK_AFTER_FIELD_WRITE	= "hookAfterFieldWrite";
	public static final String	HOOK_AROUND_EVENT				= "hookAroundEvent";
	public static final String	HOOK_BEFORE_EVENT				= "hookBeforeEvent";
	public static final String	HOOK_AFTER_EVENT				= "hookAfterEvent";
}
