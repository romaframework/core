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

import java.util.List;

import org.romaframework.core.Utility;

/**
 * Represent an event of a class.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public abstract class SchemaEvent extends SchemaAction {

	private static final long				serialVersionUID						= 1652569176934380370L;

	protected SchemaField						field;
	protected String								eventSignature;
	protected SchemaClassDefinition	eventOwner;

	public static final String			ON_METHOD										= "on";

	public static final String			COLLECTION_VIEW_EVENT				= "view";
	public static final String			COLLECTION_ADD_EVENT				= "add";
	public static final String			COLLECTION_ADD_INLINE_EVENT	= "addInline";
	public static final String			COLLECTION_EDIT_EVENT				= "edit";
	public static final String			COLLECTION_REMOVE_EVENT			= "remove";
	public static final String			DEFAULT_EVENT_NAME					= ".DEFAULT_EVENT";

	public SchemaEvent(SchemaField field, String iName, List<SchemaParameter> iOrderedParameters) {
		super(field.getEntity(), iName, iOrderedParameters);
		this.field = field;
		// TODO:Manage Params in signature
		eventSignature = ON_METHOD + Utility.getCapitalizedString(field.getName()) + Utility.getCapitalizedString(iName);
	}

	public SchemaEvent(SchemaClassDefinition iEntity, String iName, List<SchemaParameter> iOrderedParameters) {
		super(iEntity, iName, iOrderedParameters);
		// TODO:Manage Params in signature
		eventSignature = ON_METHOD + Utility.getCapitalizedString(iName);
	}

	public String getEventSignature() {
		return eventSignature;
	}

	public SchemaClassDefinition getEventOwner() {
		if (eventOwner == null) {
			return getEntity();
		}
		return eventOwner;
	}

	@Override
	public String getFullName() {
		if (field != null)
			return field.getName() + "." + getName();
		return getName();
	}

	protected void setEventOwner(SchemaClassDefinition eventOwner) {
		this.eventOwner = eventOwner;
	}
}
