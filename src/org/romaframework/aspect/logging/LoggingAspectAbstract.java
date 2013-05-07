/*
 * Copyright 2006-2007 Luca Garulli      (luca.garulli--at--assetdata.it)
 *                     Giordano Maestro  (giordano.maestro--at--assetdata.it)
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

package org.romaframework.aspect.logging;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.romaframework.core.module.SelfRegistrantConfigurableModule;

/**
 * Abstract implementation for Logging Aspect.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public abstract class LoggingAspectAbstract extends SelfRegistrantConfigurableModule<String> implements LoggingAspect {

	public static final String	ASPECT_NAME	= "logging";

	protected Log								log					= LogFactory.getLog(this.getClass());


	public String aspectName() {
		return ASPECT_NAME;
	}
}
