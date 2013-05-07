package org.romaframework.core.aspect;

import org.romaframework.core.schema.SchemaAction;
import org.romaframework.core.schema.SchemaClassDefinition;
import org.romaframework.core.schema.SchemaEvent;
import org.romaframework.core.schema.SchemaField;

public interface AspectConfigurator {

	/**
	 * 
	 * @param iClass
	 */
	public void beginConfigClass(SchemaClassDefinition iClass);

	/**
	 * 
	 * @param iClass
	 */
	public void configClass(SchemaClassDefinition iClass);

	/**
	 * 
	 * @param iField
	 */
	public void configField(SchemaField iField);

	/**
	 * 
	 * @param iAction
	 */
	public void configAction(SchemaAction iAction);

	/**
	 * 
	 * @param iEvent
	 */
	public void configEvent(SchemaEvent iEvent);

	/**
	 * 
	 * @param iClass
	 */
	public void endConfigClass(SchemaClassDefinition iClass);

}
