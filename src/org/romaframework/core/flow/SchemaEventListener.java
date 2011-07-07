/*
 *
 * Copyright 2011 Luca Molino (luca.molino--AT--assetdata.it)
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
package org.romaframework.core.flow;

import org.romaframework.core.schema.SchemaEvent;

/**
 * @author luca.molino
 * 
 */
public interface SchemaEventListener {

	/**
	 * Callback invoked before the execution of any action by the Controller. Events are themselve Events.
	 * 
	 * @param iContent
	 *          The User Object
	 * @param iEvent
	 *          The Schema Event instance
	 * @return true if the execution can proceed, false otherwise
	 */
	public boolean onBeforeEvent(Object iContent, SchemaEvent iEvent);

	/**
	 * Callback invoked after the execution of any action by the Controller. Events are themselve Events.
	 * 
	 * @param iContent
	 *          The User Object
	 * @param iEvent
	 *          The Schema Event instance
	 * @param returnedValue
	 *          the return value of the method invocation
	 */
	public void onAfterEvent(Object iContent, SchemaEvent iEvent, Object returnedValue);

	/**
	 * Callback invoked on failed execution of action by exception. Events are themselve Events.
	 * 
	 * @param iContent
	 *          The User Object
	 * @param iEvent
	 *          The Schema Event instance
	 * @param exception
	 *          The exception thrown by action.
	 */
	public void onExceptionEvent(Object iContent, SchemaEvent iEvent, Exception exception);

}
