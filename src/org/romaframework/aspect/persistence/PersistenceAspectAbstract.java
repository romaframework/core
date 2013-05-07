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

package org.romaframework.aspect.persistence;

import org.romaframework.core.Roma;

/**
 * Persistence aspect. Manages application objects in datastore.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public abstract class PersistenceAspectAbstract implements PersistenceAspect {


	public String aspectName() {
		return ASPECT_NAME;
	}

	public static PersistenceAspect getPersistenceComponent(String iMode) {
		if (iMode == null)
			return null;

		String componentName;
		if (iMode.equals(PersistenceConstants.MODE_TX)) {
			componentName = "TxPersistenceAspect";
		} else if (iMode.equals(PersistenceConstants.MODE_ATOMIC)) {
			componentName = "PersistenceAspect";
		} else if (iMode.equals(PersistenceConstants.MODE_NOTX)) {
			componentName = "NoTxPersistenceAspect";
		} else
			throw new IllegalArgumentException("Persistence mode not supported: " + iMode);

		return Roma.component(componentName);
	}
}
