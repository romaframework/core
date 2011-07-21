/*
 * Copyright 2009 Emanuele Tagliaferri (emanuele.tagliaferri--at--assetdata.it)
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
package org.romaframework.aspect.serialization.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.romaframework.aspect.core.feature.CoreFieldFeatures;
import org.romaframework.aspect.serialization.SerializationConstants;
import org.romaframework.aspect.serialization.SerializationData;
import org.romaframework.aspect.serialization.SerializationElement;
import org.romaframework.aspect.serialization.SerializationHelper;
import org.romaframework.aspect.serialization.exception.SerializationException;
import org.romaframework.aspect.serialization.feature.SerializationClassFeatures;
import org.romaframework.aspect.serialization.feature.SerializationFieldFeatures;
import org.romaframework.core.Roma;
import org.romaframework.core.entity.EntityHelper;
import org.romaframework.core.exception.ConfigurationNotFoundException;
import org.romaframework.core.schema.Feature;
import org.romaframework.core.schema.FeatureRegistry;
import org.romaframework.core.schema.FeatureType;
import org.romaframework.core.schema.SchemaAction;
import org.romaframework.core.schema.SchemaClassDefinition;
import org.romaframework.core.schema.SchemaClassElement;
import org.romaframework.core.schema.SchemaEvent;
import org.romaframework.core.schema.SchemaFeatures;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.schema.SchemaHelper;

/**
 * 
 * @author Emanuele Tagliaferri (emanuele.tagliaferri--at--assetdata.it)
 * 
 */
public class SchemaSerializationInspectionStrategy extends AbstractSerializationInspectionStrategy {

	protected static boolean		modeInspect	= false;
	protected static boolean		modeFill		= true;
	protected static final int	TYPE_EVENT	= 1;
	protected static final int	TYPE_ACTION	= 2;

	public String getName() {
		return SerializationConstants.INSPECTION_FULL;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.romaframework.aspect.serialization.SerializationInspectionStrategy#inspect(java.lang.Object)
	 */
	public SerializationData inspect(Object toInspect) {
		Stack<Object> inspected = new Stack<Object>();
		SchemaClassDefinition definition = Roma.schema().getSchemaClass(toInspect);
		if (definition == null) {
			definition = Roma.schema().getSchemaClass(toInspect.getClass());
		}
		SerializationData data = inspect(toInspect, definition, null, inspected);
		data.setName((String) definition.getFeature(SerializationClassFeatures.ROOT_ELEMENT_NAME));
		return data;
	}

	/**
	 * Inspect an object. if toInspect is an simple value (es. int,long,char,String) return a SerializationData with simple value. if
	 * toInspect is an Map/Collection return a SerializationData with a collection of SerializationData. if toInspect is an Object
	 * return a SerializationData with a list of SerializationElement in the 'fields' field.
	 * 
	 * @param toInspect
	 *          object to inspect
	 * @param definition
	 *          relative object schema definition.
	 * @return the data relative of current object.
	 */
	protected SerializationData inspect(Object toInspect, SchemaClassDefinition definition, SchemaClassDefinition embeddedType, Stack<Object> inspected) {
		if (toInspect == null || definition == null || inspected.contains(toInspect)) {
			return null;
		} else {
			inspected.push(toInspect);
		}
		SerializationData data = new SerializationData();

		if (SchemaHelper.isMultiValueObject(toInspect)) {
			// COLLECTION, ARRAY, MAP
			Object[] values = null;
			if (toInspect instanceof Map<?, ?>)
				values = ((Map<?, ?>) toInspect).entrySet().toArray();
			else
				values = SchemaHelper.getObjectArrayForMultiValueObject(toInspect);
			List<SerializationData> serializadCollection = new ArrayList<SerializationData>();

			for (Object o : values) {
				serializadCollection.add(inspect(o, getObjectType(o, embeddedType, true), null, inspected));
			}

			data.setCollection(serializadCollection);
		} else if (SchemaHelper.getClassForJavaTypes(toInspect.getClass().getSimpleName()) != null) {

			// SIMPLE TYPE
			data.setSimpleValue(toInspect);

		} else {

			// COMPLEX OBJECT
			data.setName(definition.getName());
			copyFeatures(definition, data, modeInspect);
			List<SerializationElement> fields = new ArrayList<SerializationElement>();
			for (Map.Entry<String, SchemaField> field : definition.getFields().entrySet()) {
				SchemaField schemaField = field.getValue();
				if (Boolean.TRUE.equals(schemaField.getFeature(SerializationFieldFeatures.TRANSIENT)))
					continue;
				SerializationElement element = inspectField(schemaField.getValue(toInspect), schemaField, inspected);
				if (element != null) {
					fields.add(element);
				}
			}
			data.setFields(fields);
			data.setActions(inspectSchemaElement(definition.getActionIterator()));
			data.setEvents(inspectSchemaElement(definition.getEventIterator()));
		}
		inspected.pop();
		return data;
	}

	protected SchemaClassDefinition getObjectType(Object o, SchemaClassDefinition def, Boolean runtimeType) {
		try {
			return runtimeType && o != null ? Roma.schema().getSchemaClass(o) : def;
		} catch (ConfigurationNotFoundException e) {
			return def;
		}
	}

	/**
	 * Serialize an field Create an SerializationElement that contains the features of field, the name of field, and a
	 * SerializationData that rappresent the value.
	 * 
	 * @param fieldValue
	 *          the value of field.
	 * @param schemaField
	 *          the schema of field.
	 * @param inspected
	 *          the stack of inspected.
	 * @return the SerializationElement correspondent to field.
	 */
	protected SerializationElement inspectField(Object fieldValue, SchemaField schemaField, Stack<Object> inspected) {
		SerializationElement serializationElement = new SerializationElement();
		copyFeatures(schemaField, serializationElement, modeInspect);
		serializationElement.setName(schemaField.getName());

		Boolean runtimeType = (Boolean) schemaField.getFeature(CoreFieldFeatures.USE_RUNTIME_TYPE);

		SerializationData data = inspect(fieldValue, getObjectType(fieldValue, schemaField.getType(), runtimeType), schemaField.getEmbeddedType(), inspected);
		serializationElement.setData(data);
		serializationElement.setEvents(inspectSchemaElement(schemaField.getEventIterator()));
		return serializationElement;
	}

	/**
	 * Create a list of SerializationElement from a iterator of SchemaElement
	 * 
	 * @param toSerialize
	 *          elements to serialize
	 * @return the correspondent serializationElement.
	 */
	protected List<SerializationElement> inspectSchemaElement(Iterator<? extends SchemaClassElement> toSerialize) {
		List<SerializationElement> elements = new ArrayList<SerializationElement>();
		while (toSerialize.hasNext()) {
			SchemaClassElement schemaElement = toSerialize.next();
			SerializationElement serializationElement = new SerializationElement();
			serializationElement.setName(schemaElement.getName());
			copyFeatures(schemaElement, serializationElement, modeInspect);
			elements.add(serializationElement);
		}
		return elements;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.romaframework.aspect.serialization.SerializationInspectionStrategy#create(org.romaframework.aspect.serialization.
	 * SerializationData)
	 */
	public Object create(SerializationData data) {
		return create(data, null);
	}

	/**
	 * Create an object from the data and the definition.
	 * 
	 * @param data
	 *          the serialized data of object.
	 * @param definition
	 *          the definition of object.
	 * @return the instance correspondent.
	 */
	protected Object create(SerializationData data, SchemaClassDefinition definition) {
		Object instaceToFill = null;
		if (data.containSimpleValue())
			return data.getSimpleValue();

		if (definition == null) {
			if (data.getName() == null) {
				try {
					definition = Roma.schema().getSchemaClass(data.getName());
				} catch (ConfigurationNotFoundException e) {
				}
			}
			if (definition == null)
				throw new SerializationException("Cannot determine the type of serialized object");
		}
		try {
			instaceToFill = EntityHelper.createObject(null, definition.getSchemaClass());
			fill(instaceToFill, data, definition, false);
		} catch (Exception e) {
			throw new SerializationException("Error on object instantiation:" + e.getMessage(), e);
		}
		return instaceToFill;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.romaframework.aspect.serialization.SerializationInspectionStrategy#fill(java.lang.Object,
	 * org.romaframework.aspect.serialization.SerializationData)
	 */
	public void fill(Object toFill, SerializationData data) {
		boolean copyFeatures = true;
		SchemaClassDefinition definition = Roma.session().getSchemaObject(toFill);
		if (definition == null) {
			copyFeatures = false;
			definition = Roma.schema().getSchemaClass(toFill.getClass());
		}
		if (data.getName() != null && !definition.getName().equals(data.getName())) {
			throw new SerializationException("Not Same Instance of serialized, have:" + definition.getName() + " expected:" + data.getName());
		}
		fill(toFill, data, definition, copyFeatures);
	}

	/**
	 * Fill an object.
	 * 
	 * @param toFill
	 *          Object to fill.
	 * @param data
	 *          data with fill.
	 * @param definition
	 *          relative object schemaDefinition.
	 * @param copyFeatures
	 *          if true copy the features of object.
	 */
	public void fill(Object toFill, SerializationData data, SchemaClassDefinition definition, boolean copyFeatures) {
		if (copyFeatures) {
			copyFeatures(data, definition, modeFill);
		}
		List<SerializationElement> fields = data.getFields();
		for (SerializationElement serializationField : fields) {
			SchemaField field = definition.getField(serializationField.getName());
			if (Boolean.TRUE.equals(field.getFeature(SerializationFieldFeatures.TRANSIENT)))
				continue;
			fillField(toFill, serializationField, field, copyFeatures);
		}
		if (copyFeatures) {
			fillSchemaElement(data.getEvents(), definition, TYPE_EVENT);
			fillSchemaElement(data.getActions(), definition, TYPE_ACTION);
		}
	}

	/**
	 * Retrieve the value of a field from actual value and serialized data.
	 * 
	 * @param actualFieldValue
	 *          actual value.
	 * @param data
	 *          to deserialize.
	 * @param definition
	 *          tyoe of current object.
	 * @param schemaField
	 *          TODO
	 * @param copyFeatures
	 *          if true copy the features of object.
	 * @return the value of field.
	 */
	protected void fillField(Object toFill, SerializationElement serializationField, SchemaField field, boolean copyFeatures) {
		if (copyFeatures) {
			copyFeatures(serializationField, field, modeFill);
			for (SerializationElement serializationElement : serializationField.getEvents()) {
				SchemaClassElement destElement = field.getEvent(serializationElement.getName());
				copyFeatures(serializationElement, destElement, modeFill);
			}
		}

		SerializationData fieldData = serializationField.getData();
		Object value = getFieldValue(fieldData, field, field.getValue(toFill), copyFeatures);
		try {
			field.setValue(toFill, value);
		} catch (Exception e) {
			throw new SerializationException("Error on field '" + field.getName() + "' fill :" + e.getMessage(), e);
		}
	}

	/**
	 * Generate the field value from the correspondent value.
	 * 
	 * @param fieldData
	 *          the data of field.
	 * @param field
	 * @param currentValue
	 * @param copyFeatures
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Object getFieldValue(SerializationData fieldData, SchemaField field, Object currentValue, boolean copyFeatures) {
		if (fieldData == null) {
			return null;
		} else if (fieldData.containSimpleValue()) {
			return fieldData.getSimpleValue();
		} else if (fieldData.containCollection()) {
			Object dataColl = currentValue;
			if (dataColl instanceof Collection<?>) {
				((Collection<?>) dataColl).clear();

				for (SerializationData serializationData : fieldData.getCollection()) {
					((Collection) dataColl).add(create(serializationData, field.getEmbeddedType()));
				}
			} else if (dataColl instanceof Map<?, ?>) {
				((Map<?, ?>) dataColl).clear();
				for (SerializationData serializationData : fieldData.getCollection()) {
					Object key = null;
					Object entryValue = null;
					for (SerializationElement element : serializationData.getFields()) {
						Object o = create(element.getData(), null);
						if ("key".equals(element.getName())) {
							key = o;
						} else if ("value".equals(element.getName())) {
							entryValue = o;
						}
					}
					((Map) dataColl).put(key, entryValue);
				}
			}
			return dataColl;
		} else {
			// TODO: Manage null field value.
			fill(currentValue, fieldData, field.getType(), copyFeatures);
			return currentValue;
		}
	}

	/**
	 * fill a SchemaClassDefinition with a list of SerializationElement.
	 * 
	 * @param data
	 *          to read.
	 * @param dest
	 *          to fill.
	 * @param type
	 *          of element to fill.
	 */
	protected void fillSchemaElement(List<SerializationElement> data, SchemaClassDefinition dest, int type) {
		for (SerializationElement serializationElement : data) {
			SchemaClassElement destElement = null;
			switch (type) {
			case TYPE_ACTION:
				destElement = dest.getAction(serializationElement.getName());
				break;
			case TYPE_EVENT:
				destElement = dest.getEvent(serializationElement.getName());
			default:
				break;
			}
			copyFeatures(serializationElement, destElement, modeFill);
		}
	}

	/**
	 * Copy the features from a SchemaFeatures to an other.
	 * 
	 * @param source
	 *          of copy.
	 * @param dest
	 *          of copy.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void copyFeatures(SchemaFeatures source, SchemaFeatures dest, boolean mode) {
		if (mode)
			SerializationHelper.reallineFeature(source);
		List<Feature> features = null;
		if (source instanceof SchemaClassDefinition)
			features = FeatureRegistry.getFeatures(FeatureType.CLASS);
		else if (source instanceof SchemaField)
			features = FeatureRegistry.getFeatures(FeatureType.FIELD);
		else if (source instanceof SchemaEvent)
			features = FeatureRegistry.getFeatures(FeatureType.EVENT);
		else if (source instanceof SchemaAction)
			features = FeatureRegistry.getFeatures(FeatureType.ACTION);
		for (Feature feature : features) {
			dest.setFeature(feature, source.getFeature(feature));
		}

		if (!mode)
			SerializationHelper.allowSerializeFeatures(dest);
	}

}
