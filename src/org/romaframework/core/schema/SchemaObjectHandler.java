package org.romaframework.core.schema;

/**
 * Resolve an SchemaObject instance from an object.
 * 
 */
public interface SchemaObjectHandler {

	/**
	 * Retrieve an SchemaObject from an object instance.
	 * 
	 * @param object
	 *          associated to schema object.
	 * @return the SchemaObject associated to object.
	 */
	public SchemaObject getSchemaObject(Object object);

}
