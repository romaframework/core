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

import java.util.List;

import org.romaframework.aspect.persistence.PersistenceAspect;
import org.romaframework.core.flow.ContextLifecycleListener;
import org.romaframework.core.flow.Controller;
import org.romaframework.core.flow.ObjectContext;

/**
 * @author molino
 * 
 */
public class RomaContext {
	private static final String	CTX_CREATED	= "_CTX_CREATED_";

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
}
