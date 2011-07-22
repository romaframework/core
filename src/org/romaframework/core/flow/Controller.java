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

package org.romaframework.core.flow;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.romaframework.aspect.i18n.I18NAspect;
import org.romaframework.aspect.validation.MultiValidationException;
import org.romaframework.core.exception.UserException;
import org.romaframework.core.schema.SchemaAction;
import org.romaframework.core.util.ListenerManager;

public class Controller extends ListenerManager<Class<?>> {

	private static Log							log						= LogFactory.getLog(Controller.class);
	private static final Controller	INSTANCE			= new Controller();

	private static final String			ERROR_LABEL		= ".error";

	protected Controller() {
	}

	public void executeAction(Object iContent, SchemaAction iAction) throws Throwable {
		if (iContent == null || iAction == null) {
			return;
		}

		if (log.isDebugEnabled()) {
			log.debug("[Controller.executeAction] Executing action: " + iAction + " on object " + iContent.toString() + "...");
		}

		try {
			iAction.invoke(iContent);
		} catch (InvocationTargetException ex) {
			Throwable nestedExc = ex.getTargetException();
			if (nestedExc == null || !(nestedExc instanceof UserException) && !(nestedExc instanceof MultiValidationException)) {

				String errorLabel = I18NAspect.VARNAME_PREFIX + iAction.getName() + ERROR_LABEL;

				log.warn("[Controller.executeAction] error on execution of method: " + iAction.getName(), nestedExc);

				// WRAP THE EXCEPTION IN A NEW USER EXCEPTION TAKING THE ACTION
				// NAME
				throw new UserException(iContent, errorLabel, nestedExc);
			}
			// THROW THE NESTED EXCEPTION (BYPASS THE REFLECTION)
			throw nestedExc;
		}
	}

	public static Controller getInstance() {
		return INSTANCE;
	}
}
