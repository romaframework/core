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

import org.romaframework.aspect.logging.AbstractLogger;
import org.romaframework.aspect.logging.LoggingAspect;
import org.romaframework.aspect.logging.LoggingConstants;

/**
 * It log the message to the System.out
 * 
 * @author Giordano Maestro (giordano.maestro--at--assetdata.it)
 * 
 */
public class ConsoleLogger extends AbstractLogger {

	public ConsoleLogger(LoggingAspect loggingAspect) {
		super(loggingAspect);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.romaframework.aspect.logging.Logger#getModes()
	 */
	public String[] getModes() {
		String[] result = { LoggingConstants.MODE_CONSOLE };
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.romaframework.aspect.logging.Logger#print(int, java.lang.String, java.lang.String)
	 */
	public void print(int level, String category, String message) {
		String result = "-----------------------------------\n" + "level:    " + level + "\n" + "category: " + category + "\n" + "message: " + message + "\n";
		System.out.println(result);
	}
}
