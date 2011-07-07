package org.romaframework.core.flow;

import org.romaframework.core.schema.SchemaField;

/**
 * Listener on SchemaField operations.
 * 
 * 
 */
public interface SchemaFieldListener {

	class IgnoredExecution {
	}

	/*
	 * Special object to return when the callback ignore the operation.
	 */
	public static final IgnoredExecution	IGNORED	= new IgnoredExecution();

	/**
	 * Callback invoked before to read a field. Return IGNORED if the callback ignores the method itself.
	 * 
	 * @param iContent
	 *          Object owns the field to be read
	 * @param iField
	 *          Field to be read
	 * @param iCurrentValue
	 *          Current field value
	 * @return If the implementation change the value, returns the modified value, otherwise IGNORED
	 */
	public Object onBeforeFieldRead(Object iContent, SchemaField iField, Object iCurrentValue);

	/**
	 * Callback invoked after have read a field.
	 * 
	 * @param iContent
	 *          Object owns the field to be read
	 * @param iField
	 *          Field to be read
	 * @param iCurrentValue
	 *          Current field value
	 * @return If the implementation change the value, returns the modified value, otherwise the same of iCurrentValue parameter
	 */
	public Object onAfterFieldRead(Object iContent, SchemaField iField, Object iCurrentValue);

	/**
	 * Callback invoked before a field is written.
	 * 
	 * @param iContent
	 *          Object owns the field to be written
	 * @param iField
	 *          Field to be written
	 * @param iCurrentValue
	 *          Value to write in the field
	 * @return If the implementation change the value, returns the modified value, otherwise the same of iCurrentValue parameter
	 */
	public Object onBeforeFieldWrite(Object iContent, SchemaField iField, Object iCurrentValue);

	/**
	 * Callback invoked after a field is written.
	 * 
	 * @param iContent
	 *          Object owns the field to be written
	 * @param iField
	 *          Field to be written
	 * @param iCurrentValue
	 *          Value to write in the field
	 * @return If the implementation change the value, returns the modified value, otherwise the same of iCurrentValue parameter
	 */
	public Object onAfterFieldWrite(Object iContent, SchemaField iField, Object iCurrentValue);

}
