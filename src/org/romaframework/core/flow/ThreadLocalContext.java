/*
 * Copyright 2006-2007 Luca Garulli (luca.garulli--at--assetdata.it)
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
package org.romaframework.core.flow;

import java.util.HashMap;
import java.util.Map;

/**
 * Save component to be reused in the same thread.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public class ThreadLocalContext extends ThreadLocal<Map<String, Object>> {

	@Override
	protected Map<String, Object> initialValue() {
		return new HashMap<String, Object>();
	}
}
