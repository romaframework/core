package org.romaframework.core.schema;

import java.util.Iterator;

public class SchemaObjectFieldIterator implements Iterator<SchemaField> {
	private Iterator<SchemaField>	parent;

	public SchemaObjectFieldIterator(Iterator<SchemaField> parent) {
		this.parent = parent;
	}

	@Override
	public boolean hasNext() {
		return parent.hasNext();
	}

	@Override
	public SchemaField next() {
		SchemaField field = parent.next();
		if (field.getType() != null && !(field.getType() instanceof SchemaObject)) {
			synchronized (field) {
				if (field.getType() != null)
					// COMPLEX CLASS: REPLACE IT WITH A SCHEMA OBJECT INSTANCE
					field.setType(new SchemaObject(field.getType().getSchemaClass()));
			}
		}
		return field;
	}

	@Override
	public void remove() {
		parent.remove();
	}
}
