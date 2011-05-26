package org.romaframework.core.flow;

import org.romaframework.core.schema.SchemaAction;

/**
 * Schema Action Invoke Listener.
 * 
 */
public interface SchemaActionListener {

	/**
	 * Callback invoked before the execution of any action by the Controller. Events are themselve Actions.
	 * 
	 * @param iContent
	 *          The User Object
	 * @param iAction
	 *          The Schema Action instance
	 * @return true if the execution can proceed, false otherwise
	 */
	public boolean onBeforeAction(Object iContent, SchemaAction iAction);

	/**
	 * Callback invoked after the execution of any action by the Controller. Events are themselve Actions.
	 * 
	 * @param iContent
	 *          The User Object
	 * @param iAction
	 *          The Schema Action instance
	 * @param returnedValue
	 *          the return value of the method invocation
	 */
	public void onAfterAction(Object iContent, SchemaAction iAction, Object returnedValue);

	/**
	 * Callback invoked on failed execution of action by exception. Events are themselve Actions.
	 * 
	 * @param iContent
	 *          The User Object
	 * @param iAction
	 *          The Schema Action instance
	 * @param exception
	 *          The exception thrown by action.
	 */
	public void onExceptionAction(Object iContent, SchemaAction iAction, Exception exception);

}
