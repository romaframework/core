/*
 * Copyright 2006-2007 Giordano Maestro (giordano.maestro--at--assetdata.it)
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
package org.romaframework.aspect.logging.loggers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.romaframework.aspect.logging.AbstractLogger;
import org.romaframework.aspect.logging.LoggingAspect;
import org.romaframework.aspect.logging.LoggingConstants;

/**
 * It log the message using the common logins
 * 
 * @author Giordano Maestro (giordano.maestro--at--assetdata.it)
 * 
 */
public class FileLogger extends AbstractLogger {

	public FileLogger(LoggingAspect loggingAspect) {
		super(loggingAspect);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.romaframework.aspect.logging.Logger#getModes()
	 */
	public String[] getModes() {
		String[] result = { LoggingConstants.MODE_FILE };
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.romaframework.aspect.logging.Logger#print(int, java.lang.String, java.lang.String)
	 */
	public void print(int level, String category, String message) {
		Log logger = LogFactory.getLog(category);
		if (level == LoggingConstants.LEVEL_DEBUG) {
			logger.debug(message);
		} else if (level == LoggingConstants.LEVEL_WARNING) {
			logger.warn(message);
		} else if (level == LoggingConstants.LEVEL_ERROR) {
			logger.error(message);
		} else if (level == LoggingConstants.LEVEL_FATAL) {
			logger.fatal(message);
		} else if (level == LoggingConstants.LEVEL_INFO) {
			logger.info(message);
		}

	}

}
