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
package org.romaframework.aspect.logging;

import java.util.LinkedList;
import java.util.List;

import org.romaframework.core.util.parser.VariableParserListener;

/**
 * A listener that add to a list any instance founded of a log special parameter
 * 
 * @author Giordano Maestro (giordano.maestro--at--assetdata.it)
 * 
 */
public class VariablesListener implements VariableParserListener {

	public VariablesListener() {
	}

	private LinkedList<String>	variables	= new LinkedList<String>();

	public String resolve(String variable) {
		variables.add(variable);
		return variable;
	}

	public List<String> getVariables() {
		return variables;
	}

}
