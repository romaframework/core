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

import org.romaframework.core.exception.FieldErrorUserException;

public class ValidationException extends FieldErrorUserException {
	public ValidationException(Object iInstance, String iFieldName, String iMessage) {
		super(iInstance, iFieldName, iMessage);
	}

	public ValidationException(Object iInstance, String iFieldName, String iMessage, String iRefValue) {
		super(iInstance, iFieldName, iMessage);
		setRefValue(iRefValue);
	}

	public ValidationException(Object iInstance, String iFieldName, String iMessage, String iRefValue, Throwable cause) {
		super(iInstance, iFieldName, iMessage, cause);
		setRefValue(iRefValue);
	}

	public String getRefValue() {
		return refValue;
	}

	private void setRefValue(String iRefValue) {
		refValue = iRefValue;

		if (refValue != null) {
			message.append(" (");
			message.append(refValue);
			message.append(')');
		}
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder(super.toString());
		if (refValue != null) {
			buffer.append(" refValue: ");
			buffer.append(refValue);
		}
		return buffer.toString();
	}

	public Object getObject() {
		return obj;
	}

	protected String						refValue;

	public static final String	VALIDATION_POSTFIX	= ".validation";
}
