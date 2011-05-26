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

package org.romaframework.core.schema;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.romaframework.core.Roma;
import org.romaframework.core.flow.Controller;
import org.romaframework.core.flow.UserObjectEventListener;

/**
 * Generic abstract class that represents an Action.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public abstract class SchemaAction extends SchemaClassElement {
	private static final long							serialVersionUID	= -4789886810661429988L;

	private static Log										log								= LogFactory.getLog(SchemaAction.class);

	private Map<String, SchemaParameter>	parameters				= new HashMap<String, SchemaParameter>();
	private List<SchemaParameter>					orderedParameters	= new ArrayList<SchemaParameter>();
	private SchemaClass										returnType;

	public SchemaAction(SchemaClassDefinition iEntity, String iName) {
		super(iEntity, iName);
	}

	public SchemaAction(SchemaClassDefinition iEntity, String iName, List<SchemaParameter> iOrderedParameters) {
		super(iEntity, iName);
		orderedParameters = iOrderedParameters;
		if (orderedParameters != null)
			for (SchemaParameter param : orderedParameters)
				parameters.put(param.getName(), param);
	}

	public abstract Object invokeFinal(Object iContent, Object[] params) throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException;

	public Object invoke(Object iContent, Object... params) throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {

		try {
			// CREATE THE CONTEXT BEFORE TO CALL THE ACTION
			Roma.context().create();

			List<UserObjectEventListener> listeners = Controller.getInstance().getListeners(UserObjectEventListener.class);
			boolean result = true;
			for (UserObjectEventListener listener : listeners) {
				result = listener.onBeforeActionExecution(iContent, this);
				if (!result) {
					log.debug("[SchemaAction.invoke] Listener " + listener
							+ " has interrupted the chain of execution before the execution of the action");
					return null;
				}
			}

			Object value = null;
			try {
				return invokeFinal(iContent, params);
			} finally {
				for (UserObjectEventListener listener : listeners) {
					try {
						listener.onAfterActionExecution(iContent, this, value);
					} catch (Throwable t) {
						log.error("[SchemaAction.invoke] Listener " + listener
								+ " has interrupted the chain of execution after the execution of the action", t);
					}
				}
			}
		} finally {
			// ASSURE TO DESTROY THE CONTEXT
			Roma.context().destroy();
		}
	}

	@Override
	public String toString() {
		return super.toString() + getSignature();
	}

	public static boolean ignoreMethod(String iItem, String iMethodName) {
		if (iItem.endsWith("^*")) {
			String trunk = iItem.substring(0, iItem.length() - 2);
			if (iMethodName.startsWith(trunk) && Character.isUpperCase(iMethodName.charAt(trunk.length())))
				return true;
		} else if (iItem.endsWith("*")) {
			if (iMethodName.startsWith(iItem.substring(0, iItem.length() - 1)))
				return true;
		} else if (iItem.startsWith("*")) {
			if (iMethodName.endsWith(iItem.substring(1)))
				return true;
		} else {
			if (iMethodName.equals(iItem))
				return true;
		}

		return false;
	}

	public Iterator<SchemaParameter> getParameterIterator() {
		return orderedParameters.iterator();
	}

	public SchemaParameter getParameter(String iName) {
		return parameters.get(iName);
	}

	public int getParameterNumber() {
		return parameters.size();
	}

	public String getSignature() {
		String[] params = null;
		if (orderedParameters != null) {
			params = new String[orderedParameters.size()];
			for (int i = 0; i < orderedParameters.size(); ++i) {
				params[i] = orderedParameters.get(i).getType().getName();
			}
		}

		return getSignature(name, params);
	}

	public SchemaClass getReturnType() {
		return returnType;
	}

	public void setReturnType(SchemaClass returnType) {
		this.returnType = returnType;
	}

	public static String getSignature(String name, String[] paramNames) {
		StringBuilder methodSignature = new StringBuilder();
		methodSignature.append(name);
		if (paramNames != null && paramNames.length != 0) {
			methodSignature.append("(");
			for (int i = 0; i < paramNames.length; i++) {
				methodSignature.append(paramNames[i]);
				if (i < paramNames.length - 1)
					methodSignature.append(",");
			}
			methodSignature.append(")");
		}
		return methodSignature.toString();
	}
}
