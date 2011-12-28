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

import java.util.Locale;

import org.romaframework.aspect.i18n.I18NAspect;
import org.romaframework.core.Roma;
import org.romaframework.core.util.parser.PositionalVariableResolver;

/**
 * Localized version of runtime exception. It load the message using Roma's I18N aspect.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public class LocalizedRuntimeException extends RuntimeException {
	private static final long	serialVersionUID	= 6884108380664291733L;

	protected Object[]				args;

	public LocalizedRuntimeException() {
		super();
	}

	public LocalizedRuntimeException(Throwable cause) {
		super(cause);
	}

	public LocalizedRuntimeException(String arg0, Throwable arg1, Object... iArgs) {
		super(arg0, arg1);
		args = iArgs;
	}

	public LocalizedRuntimeException(String arg0, Object... iArgs) {
		super(arg0);
		args = iArgs;
	}

	@Override
	public String getLocalizedMessage() {
		return getLocalizedMessage((Locale) null);
	}

	public String getLocalizedMessage(Locale iLocale) {
		String msg = getMessage();
		if (msg.startsWith(I18NAspect.VARNAME_PREFIX))
			return Roma.component(I18NAspect.class).resolve(msg, iLocale);

		if (args != null && args.length > 0)
			return new PositionalVariableResolver(msg).resolve(args);

		return msg;
	}
}
