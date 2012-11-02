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

import java.math.BigDecimal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.romaframework.core.Roma;
import org.romaframework.core.domain.entity.ComposedEntity;
import org.romaframework.core.schema.virtual.VirtualObject;

/**
 * Represent a base element for an entity.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public abstract class SchemaClassElement extends SchemaElement {

	private static final long				serialVersionUID	= 7431417848849742385L;
	private static final Log				log								= LogFactory.getLog(SchemaClassElement.class);
	protected SchemaClassDefinition	entity;
	protected String								fullName;

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
			fullName = getEntity().getSchemaClass().getName() + "." + getName();
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected Object convertValue(Object iFieldValue, SchemaClassDefinition expectedType) {
		if (expectedType == null || expectedType.getSchemaClass().isArray())
			return iFieldValue;

		SchemaClass typeClass = expectedType.getSchemaClass();
		if (typeClass.equals(Roma.schema().getSchemaClass(iFieldValue)))
			return iFieldValue;

		String textValue = null;
		if (iFieldValue instanceof String) {
			textValue = (String) iFieldValue;
		} else if (iFieldValue != null) {
			textValue = iFieldValue.toString();
		}

		Object value = null;

		if (textValue != null) {
			// TRY A SOFT CONVERSION
			if (typeClass.isOfType(Integer.class) || typeClass.isOfType(Integer.TYPE)) {
				try {
					value = textValue.equals("") ? null : Integer.parseInt(textValue);
				} catch (Exception e) {
					value = textValue.equals("") ? null : Double.valueOf(textValue).intValue();
				}
			} else if (typeClass.isOfType(Long.class) || typeClass.isOfType(Long.TYPE)) {
				value = textValue.equals("") ? null : Long.parseLong(textValue);
			} else if (typeClass.isOfType(Short.class) || typeClass.isOfType(Short.TYPE)) {
				value = textValue.equals("") ? null : Short.parseShort(textValue);
			} else if (typeClass.isOfType(Byte.class) || typeClass.isOfType(Byte.TYPE)) {
				value = textValue.equals("") ? null : Byte.parseByte(textValue);
			} else if (typeClass.isOfType(Character.class) || typeClass.isOfType(Character.TYPE)) {
				if (textValue.length() > 0) {
					value = new Character(textValue.charAt(0));
				}
			} else if (typeClass.isOfType(Float.class) || typeClass.isOfType(Float.TYPE)) {
				value = textValue.equals("") ? null : Float.parseFloat(textValue);
			} else if (typeClass.isOfType(Double.class) || typeClass.isOfType(Double.TYPE)) {
				value = textValue.equals("") ? null : Double.parseDouble(textValue);
			} else if (typeClass.isOfType(BigDecimal.class)) {
				value = textValue.equals("") ? null : new BigDecimal(textValue);
			} else if (iFieldValue != null && !typeClass.isArray() && iFieldValue.getClass().isArray()) {
				// DESTINATION VALUE IS NOT AN ARRAY: ASSIGN THE FIRST ONE ELEMENT
				value = ((Object[]) iFieldValue)[0];
			} else if (typeClass.isEnum()) {
				value = Enum.valueOf((Class) typeClass.getLanguageType(), textValue.toUpperCase());
			} else {
				value = iFieldValue;
			}
		}

		if (value != null) {
			// TODO is this the right place to do this...?
			Class<?> valueClass = value.getClass();
			// SUCH A MONSTER!!! MOVE THIS LOGIC IN SchemaClass.isAssignableFrom...
			if (value instanceof VirtualObject
					&& !(typeClass.getLanguageType() instanceof Class<?> && ((Class<?>) typeClass.getLanguageType())
							.isAssignableFrom(VirtualObject.class)) && ((VirtualObject) value).getSuperClassObject() != null) {
				if (ComposedEntity.class.isAssignableFrom(((VirtualObject) value).getSuperClassObject().getClass())) {
					value = ((VirtualObject) value).getSuperClassObject();
					valueClass = value.getClass();
				}
			}

			if (value instanceof ComposedEntity<?> && !typeClass.isAssignableFrom(valueClass)) {
				value = ((ComposedEntity<?>) value).getEntity();
			}
		}

		if (value == null && typeClass.isPrimitive()) {
			log.warn("Cannot set the field value to null for primitive types! Field: " + getEntity() + "." + name + " of class "
					+ expectedType.getName() + ". Setting value to 0.");
			// SET THE VALUE TO 0
			value = SchemaHelper.assignDefaultValueToLiteral(typeClass);
		}
		return value;
	}

	@Override
	public <T> boolean isRuntimeSet(Feature<T> feature) {
		if (getEntity() instanceof SchemaObject && hasFeature(feature))
			return true;
		return false;
	}

}
