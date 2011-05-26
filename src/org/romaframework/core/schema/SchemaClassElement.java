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

/**
 * Represent a base element for an entity.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public abstract class SchemaClassElement extends SchemaElement {
	private static final long				serialVersionUID	= 7431417848849742385L;

	protected SchemaClassDefinition	entity;

	public SchemaClassElement(SchemaClassDefinition iEntity) {
		this(iEntity, null);
	}

	public SchemaClassElement(SchemaClassDefinition iEntity, String iName) {
		super(iName);
		entity = iEntity;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		SchemaClassElement copy = (SchemaClassElement) super.clone();
		copy.entity = entity;

		return copy;
	}

	public SchemaClassDefinition getEntity() {
		return entity;
	}

	@Override
	public String toString() {
		return (entity != null ? entity.getName() : "?(null)") + ".";
	}
}
