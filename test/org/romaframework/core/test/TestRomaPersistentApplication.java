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
package org.romaframework.core.test;

import org.romaframework.aspect.persistence.PersistenceAspect;
import org.romaframework.core.Roma;
import org.romaframework.core.flow.ObjectContext;

/**
 * Base JUnit class to make tests in application based on Roma Meta Framework that use Persistence.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 * @see TestRomaApplication
 */

public abstract class TestRomaPersistentApplication extends TestRomaApplication {

	@Override
	protected void start() {
		
	}

	protected void onTearDown() throws Exception {
		PersistenceAspect db = Roma.context().persistence();
		ObjectContext.getInstance().setContextComponent(PersistenceAspect.class, null);

		try {
			if (db.isActive())
				db.commit();
		} catch (Throwable t) {
			db.close();
		}
	}

	public PersistenceAspect getPersistence() {
		return Roma.context().persistence();
	}
}
