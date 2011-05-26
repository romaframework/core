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

package org.romaframework.core.exception;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

public class ExceptionHelper extends RuntimeException {

	public static String toString(Throwable iException) {
		StringBuilder buffer = new StringBuilder(iException.toString());
		if (iException.getCause() != null) {
			buffer.append("\nDetails:\n");

			ByteArrayOutputStream os = new ByteArrayOutputStream();
			PrintWriter pw = new PrintWriter(os);
			iException.getCause().printStackTrace(pw);
			pw.flush();

			buffer.append(os.toString());
		} else if (iException.getStackTrace() != null) {
			buffer.append("\n");
			StackTraceElement[] stackElems = iException.getStackTrace();
			for (StackTraceElement stackElem : stackElems)
				buffer.append(stackElem.toString() + "\n");
		}

		return buffer.toString();
	}
}
