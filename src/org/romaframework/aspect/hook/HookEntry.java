/*
 * Copyright 2006-2009 Luca Garulli (luca.garulli--at--assetdata.it)
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

package org.romaframework.aspect.hook;

import org.romaframework.aspect.hook.annotation.HookScope;
import org.romaframework.core.schema.SchemaClassElement;

/**
 * Keep tracks of hooks information.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public class HookEntry {

	public SchemaClassElement	clazzElement;
	public HookScope					scope;

	public HookEntry(SchemaClassElement clazzElement, HookScope scope) {
		this.clazzElement = clazzElement;
		this.scope = scope;
	}

	public HookScope getScope() {
		return scope;
	}

	public void setScope(HookScope scope) {
		this.scope = scope;
	}

	@Override
	public String toString() {
		return "HookEntry [clazzElement=" + clazzElement + ", scope=" + scope + "]";
	}
}
