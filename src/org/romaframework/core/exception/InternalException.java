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

import org.romaframework.aspect.i18n.I18NAspect;
import org.romaframework.aspect.i18n.I18NAspect.Type;
import org.romaframework.core.Roma;

public class InternalException extends RuntimeException {

	public InternalException(Object iObject, String iMessage) {
		init(iObject, iMessage);
	}

	public InternalException(Object iObject, String iMessage, Throwable cause) {
		super(cause);
		init(iObject, iMessage);
	}

	public InternalException(String iObject, Throwable t) {
		super(iObject, t);
	}

	@Override
	public String getMessage() {
		return message.toString();
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder(super.toString());
		buffer.append(" - MessageText: ");
		buffer.append(message);
		return buffer.toString();
	}

	private void init(Object iObject, String iText) {
		message = new StringBuilder();

		String i18NMessage = Roma.component(I18NAspect.class).resolve(iObject, iText, Type.EXCEPTION);

		if (i18NMessage != null)
			message.append(i18NMessage);
		else
			message.append(iText);
	}

	protected StringBuilder	message	= new StringBuilder();
}
