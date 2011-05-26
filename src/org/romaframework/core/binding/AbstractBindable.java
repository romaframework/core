/*
 * Copyright 2011 Luigi Dell'Aquila (luigi.dellaquila--at--assetdata.it)
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
package org.romaframework.core.binding;

import org.romaframework.core.Roma;
import org.romaframework.core.schema.SchemaClass;
import org.romaframework.core.schema.SchemaField;

/**
 * abstract implementation of Bindable interface
 * @author Luigi Dell'Aquila
 */
public abstract class AbstractBindable implements Bindable {
	
	protected Object			sourceObject;
	protected SchemaField	sourceField;

	@Override
	public void setSource(Object iSourceObject, String iSourceFieldName) {
		this.sourceObject = iSourceObject;
		SchemaClass cls = Roma.schema().getSchemaClass(this.sourceObject.getClass());
		sourceField = cls.getField(iSourceFieldName);

		if (sourceField == null)
			throw new RuntimeException("Cannot find field name " + this.sourceObject.getClass().getSimpleName() + "."
					+ iSourceFieldName + ". Check class definition");
		init();
	}

	protected void bind(Object value) {
		sourceField.setValue(sourceObject, value);
	}

	@Override
	public Object getSourceObject() {
		return sourceObject;
	}

	@Override
	public SchemaField getSourceField() {
		return sourceField;
	}

	/**
	 * automatically invoked after setSource()
	 */
	protected abstract void init();
}
