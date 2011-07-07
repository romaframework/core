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

package org.romaframework.aspect.logging;

import org.romaframework.core.aspect.Aspect;

/**
 * Logging Aspect behavior interface.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public interface LoggingAspect extends Aspect {
	/**
	 * The aspect name
	 */
	public static final String	ASPECT_NAME	= "logging";

	/**
	 * Register a logger to the aspect
	 * 
	 * @param iLogger
	 *          the logger to register
	 */
	public void registerLogger(Logger iLogger);

	/**
	 * Remove a logger to the aspect
	 * 
	 * @param iLogger
	 *          the logger to remove
	 */
	public void removeLogger(Logger iLogger);

	/**
	 * Log a message
	 * 
	 * @param level
	 *          the level of the message
	 * @param category
	 *          the category of the message
	 * @param mode
	 *          the mode of the message
	 * @param message
	 *          the message to log
	 */
	public void log(int level, String category, String mode, String message);
}
