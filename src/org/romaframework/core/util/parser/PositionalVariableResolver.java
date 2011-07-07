/*
 * Copyright 2006-2008 Luca Garulli (luca.garulli--at--assetdata.it)
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
package org.romaframework.core.util.parser;

/**
 * Resolve variables with positional values
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 * 
 */
public class PositionalVariableResolver implements VariableParserListener {
	public static final String	VAR_BEGIN	= "${";
	public static final String	VAR_END		= "}";
	protected String						format;
	protected int								counter;
	protected Object[]					args;

	public PositionalVariableResolver(String iFormat) {
		format = iFormat;
	}

	public String resolve(Object[] iArgs) {
		args = iArgs;
		counter = 0;
		return VariableParser.resolveVariables(format, VAR_BEGIN, VAR_END, this);
	}

	public String resolve(String variable) {
		counter = Integer.parseInt(variable) - 1;

		if (counter >= args.length)
			return null;

		Object arg = args[counter];
		return arg != null ? arg.toString() : null;
	}
}
