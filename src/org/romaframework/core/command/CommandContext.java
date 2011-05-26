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
package org.romaframework.core.command;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/*
 * Context for the Command execution. It contains the Context ID and multiple parameters using an HashMap<String,Object>.
 */
public class CommandContext implements Serializable {

	private static final long		serialVersionUID				= 1100738502299744053L;

	public final static String	GLOBAL_WORKFLOW_CONTEXT	= "__global_workflow_context";

	private long								id											= -1;
	private Map<String, Object>	parameters;

	public CommandContext() {
		this(-1);
	}

	public CommandContext(long iId) {
		this(new HashMap<String, Object>());
		id = iId;
	}

	public CommandContext(Map<String, Object> parameters) {
		this.parameters = parameters;
	}

	public void clear() {
		id = -1;
		parameters.clear();
	}

	public Object getParameter(String iName) {
		return parameters.get(iName);
	}

	public void setParameter(String iName, Object iValue) {
		if (parameters == null) {
			parameters = new HashMap<String, Object>();
		}
		if (iValue == null)
			parameters.remove(iName);
		else
			parameters.put(iName, iValue);
	}

	public Map<String, Object> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Integer getInt(String iName) {
		return (Integer) parameters.get(iName);
	}

	public String getString(String iName) {
		return (String) parameters.get(iName);
	}

	public Short getShort(String iName) {
		return (Short) parameters.get(iName);
	}

	public Long getLong(String iName) {
		return (Long) parameters.get(iName);
	}

	public Float getFloat(String iName) {
		return (Float) parameters.get(iName);
	}

	public Double getDouble(Long iName) {
		return (Double) parameters.get(iName);
	}

	public Byte getByte(Long iName) {
		return (Byte) parameters.get(iName);
	}

	@Override
	public String toString() {
		return "ID #" + id + " - parameters: " + parameters;
	}
}
