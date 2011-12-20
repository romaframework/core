/*
 *
 * Copyright 2009 Luca Molino (luca.molino--AT--assetdata.it)
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
package org.romaframework.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.romaframework.aspect.persistence.PersistenceAspect;
import org.romaframework.core.config.RomaApplicationContext;
import org.romaframework.core.flow.ContextLifecycleListener;
import org.romaframework.core.flow.Controller;
import org.romaframework.core.flow.ObjectContext;

/**
 * @author molino
 * 
 */
public class RomaContext {
	private static final String	CTX_CREATED	= "_CTX_CREATED_";
	private static final String	CTX_STACK		= "_CTX_STACK_";

	public PersistenceAspect persistence() {
		return ObjectContext.getInstance().getContextComponent(PersistenceAspect.class);
	}

	/**
	 * Create the context or increment a counter if context exist.
	 */
	public void create() {
		Integer counter = ObjectContext.getInstance().getContextComponent(CTX_CREATED);
		if (counter == null) {
			// FIRST ONE: SET TO 0
			ObjectContext.getInstance().setContextComponent(CTX_CREATED, 0);
			List<ContextLifecycleListener> contextListener = Controller.getInstance().getListeners(ContextLifecycleListener.class);
			for (ContextLifecycleListener listener : contextListener) {
				listener.onContextCreate();
			}
		} else
			// NESTED CALL: INCREMENT THE COUNTER TO ASSURE THE ROLLOUT OF THE STACKS OF THE CALLS
			ObjectContext.getInstance().setContextComponent(CTX_CREATED, ++counter);

	}

	/**
	 * Destroy the context if the counter is 0 otherwise decrement the counter.
	 */
	public void destroy() {
		// REMOVE THE TX BEGIN
		Integer counter = ObjectContext.getInstance().getContextComponent(CTX_CREATED);
		if (counter == 0) {
			// REMOVE THE COMPONENT
			List<ContextLifecycleListener> contextListener = Controller.getInstance().getListeners(ContextLifecycleListener.class);
			for (ContextLifecycleListener listener : contextListener) {
				listener.onContextDestroy();
			}
			ObjectContext.getInstance().setContextComponent(CTX_CREATED, null);
		} else
			// DECREMENT THE COUNTER
			ObjectContext.getInstance().setContextComponent(CTX_CREATED, --counter);

	}

	public boolean isCreated() {
		return ObjectContext.getInstance().getContextComponent(CTX_CREATED) != null;
	}

	public <T> T component(Class<T> iComponentClass) {
		return (T) ObjectContext.getInstance().getContextComponent(iComponentClass);
	}

	@SuppressWarnings("unchecked")
	public <T> T component(String iName) {
		return (T) ObjectContext.getInstance().getContextComponent(iName);
	}

	public void setComponent(String iComponentName, Object iValue) {
		ObjectContext.getInstance().setContextComponent(iComponentName, iValue);
	}

	public void setComponent(Class<? extends Object> iClass, Object iValue) {
		ObjectContext.getInstance().setContextComponent(iClass, iValue);
	}

	/**
	 * Check if a component was configured in the IoC system.
	 * 
	 * @param iClass
	 *          Interface of component implementation
	 * @return true if was configured, otherwise null
	 */
	public boolean existComponent(Class<? extends Object> iClass) {
		return existComponent(Utility.getClassName(iClass));
	}

	/**
	 * Check if a component was configured in the IoC system.
	 * 
	 * @param iComponentName
	 *          Name of component to search
	 * @return true if was configured, otherwise null
	 */
	public boolean existComponent(String iComponentName) {
		return RomaApplicationContext.getInstance().getComponentAspect().existComponent(iComponentName);
	}

	public void push() {
		Stack<Map<String, Object>> stack = ObjectContext.getInstance().getContextComponent(CTX_STACK);
		if (stack == null) {
			stack = new Stack<Map<String, Object>>();
			ObjectContext.getInstance().setContextComponent(CTX_STACK, stack);
		}
		Map<String, Object> current = new HashMap<String, Object>();
		Integer counter = ObjectContext.getInstance().getContextComponent(CTX_CREATED);
		current.put(CTX_STACK, counter);
		stack.push(current);
		ObjectContext.getInstance().setContextComponent(CTX_CREATED, null);
		List<ContextLifecycleListener> contextListener = Controller.getInstance().getListeners(ContextLifecycleListener.class);
		for (ContextLifecycleListener listener : contextListener) {
			listener.onContextPush(current);
		}
		create();
	}

	public void pop() {
		Stack<Map<String, Object>> stack = ObjectContext.getInstance().getContextComponent(CTX_STACK);
		if (stack != null) {
			Integer counter = null;
			do {
				destroy();
				counter = ObjectContext.getInstance().getContextComponent(CTX_CREATED);
			} while (counter != null);
			Map<String, Object> current = stack.pop();
			if (stack.isEmpty()) {
				ObjectContext.getInstance().setContextComponent(CTX_STACK, null);
			}
			ObjectContext.getInstance().setContextComponent(CTX_CREATED, current.get(CTX_CREATED));
			List<ContextLifecycleListener> contextListener = Controller.getInstance().getListeners(ContextLifecycleListener.class);
			for (ContextLifecycleListener listener : contextListener) {
				listener.onContextPop(current);
			}
		}
	}
}
