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

package org.romaframework.aspect.validation;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Handle multiple validation exception in one shot.
 * 
 * @author Luca Garulli (l.garulli--at--assetdata.it)
 * 
 */
public class MultiValidationException extends RuntimeException {

	private ArrayList<ValidationException>	exceptions;

	public MultiValidationException() {
		super();
	}

	public MultiValidationException(ValidationException e) {
		super();
		addException(e);
	}

	public Iterator<ValidationException> getDetailIterator() {
		if (exceptions == null)
			return null;

		return exceptions.iterator();
	}

	public boolean hasExceptions() {
		return exceptions != null;
	}

	/**
	 * Return a string with all exception messages.
	 */
	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		if (exceptions != null)
			for (ValidationException e : exceptions) {
				buffer.append(e.toString());
				buffer.append("\n");
			}
		return buffer.toString();
	}

	public boolean isEmpty() {
		return exceptions == null || exceptions.isEmpty();
	}

	public void addException(ValidationException e) {
		if (exceptions == null)
			exceptions = new ArrayList<ValidationException>();
		exceptions.add(e);
	}
}
