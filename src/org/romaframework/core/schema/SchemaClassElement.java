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
	protected String fullName;

	public SchemaClassElement(SchemaClassDefinition iEntity, FeatureType featureType) {
		this(iEntity, null, featureType);
	}

	public SchemaClassElement(SchemaClassDefinition iEntity, String iName, FeatureType featureType) {
		super(iName, featureType);
		entity = iEntity;
	}

	public SchemaClassDefinition getEntity() {
		return entity;
	}

	public String getFullName() {
		if (fullName == null) {
			fullName = getEntity().getName() + "." + getName();
		}
		return fullName;
	}

	@Override
	public String toString() {
		return (entity != null ? entity.getName() : "?(null)") + ".";
	}

	protected void setEntity(SchemaClassDefinition entity) {
		fullName = null;
		this.entity = entity;
	}
}
