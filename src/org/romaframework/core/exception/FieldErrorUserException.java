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

public class FieldErrorUserException extends UserException {

	public FieldErrorUserException(Object iObject, String iFieldName, String iMessage) {
		super(iObject, iMessage);
		fieldName = iFieldName;
	}

	public FieldErrorUserException(Object iObject, String iFieldName, String iMessage, Throwable cause) {
		super(iObject, iMessage, cause);
		fieldName = iFieldName;
	}

	public String getFieldName() {
		return fieldName;
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder(super.toString());
		if (fieldName != null) {
			buffer.append(" Field: ");
			buffer.append(fieldName);
		}
		return buffer.toString();
	}

	protected Object	component;
	protected String	fieldName;
	protected Object	actualValue;
}
