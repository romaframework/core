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

/**
 * Logging constants.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public interface LoggingConstants {

	public static final int			LEVEL_DEBUG				= 0;
	public static final int			LEVEL_INFO				= 1;
	public static final int			LEVEL_WARNING			= 2;
	public static final int			LEVEL_ERROR				= 3;
	public static final int			LEVEL_FATAL				= 4;

	public static final String	MODE_DB						= "DB";
	public static final String	MODE_FILE					= "FILE";
	public static final String	MODE_CONSOLE			= "CONSOLE";

	public static final String	DEFAULT_CATEGORY	= "DefaultCategory";
	public static final String	DEFAULT_MESSAGE		= "@{who} @{where}";

}
