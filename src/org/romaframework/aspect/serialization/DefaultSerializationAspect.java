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
package org.romaframework.aspect.serialization;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import org.romaframework.aspect.serialization.exception.SerializationException;
import org.romaframework.aspect.serialization.feature.SerializationClassFeatures;
import org.romaframework.aspect.serialization.feature.SerializationFieldFeatures;
import org.romaframework.core.module.SelfRegistrantConfigurableModule;
import org.romaframework.core.schema.SchemaClassDefinition;
import org.romaframework.core.schema.SchemaClassElement;
import org.romaframework.core.schema.SchemaEvent;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.schema.reflection.SchemaClassReflection;
import org.romaframework.core.schema.reflection.SchemaFieldReflection;
import org.romaframework.core.schema.xmlannotations.XmlActionAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlAspectAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlClassAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlEventAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlFieldAnnotation;

/**
 * @author Emanuele Tagliaferri (emanuele.tagliaferri--at--assetdata.it)
 */
public class DefaultSerializationAspect extends SelfRegistrantConfigurableModule<String> implements SerializationAspect {

	protected Map<String, SerializationInspectionStrategy>	inspectionStrategies;
	protected Map<String, SerializationFormatStrategy>			formatStrategies;

	public DefaultSerializationAspect() {
		inspectionStrategies = new HashMap<String, SerializationInspectionStrategy>();
		formatStrategies = new HashMap<String, SerializationFormatStrategy>();
	}

	public byte[] serialize(Object toSerialize) {
		return serialize(toSerialize, SerializationConstants.DEFAULT_INSPECTION, SerializationConstants.DEFAULT_FORAMT);
	}

	public byte[] serialize(Object toSerialize, String inspectionStrategy) {
		return serialize(toSerialize, inspectionStrategy, SerializationConstants.DEFAULT_FORAMT);
	}

	public void serialize(Object toSerialize, OutputStream outputStream) {
		serialize(toSerialize, outputStream, SerializationConstants.DEFAULT_INSPECTION, SerializationConstants.DEFAULT_FORAMT);
	}

	public byte[] serialize(Object toSerialize, String inspectionStrategy, String formatStrategy) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		serialize(toSerialize, outputStream, inspectionStrategy, formatStrategy);
		return outputStream.toByteArray();
	}

	public void serialize(Object toSerialize, OutputStream outputStream, String inspectionStrategy, String formatStrategy) {

		SerializationInspectionStrategy inspectionStrategyInstance = getInspectionStrategy(inspectionStrategy);
		SerializationData data = inspectionStrategyInstance.inspect(toSerialize);

		SerializationFormatStrategy formatStrategyInstance = getFormatStrategy(formatStrategy);
		formatStrategyInstance.marshall(data, outputStream);
	}

	public void deserialize(Object toFill, byte[] data) {
		deserialize(toFill, data, SerializationConstants.DEFAULT_INSPECTION, SerializationConstants.DEFAULT_FORAMT);
	}

	public void deserialize(Object toFill, InputStream inputStream) {
		deserialize(toFill, inputStream, SerializationConstants.DEFAULT_INSPECTION, SerializationConstants.DEFAULT_FORAMT);
	}

	public void deserialize(Object toFill, byte[] data, String inspectionStrategy, String formatStrategy) {
		InputStream inputStream = new ByteArrayInputStream(data);
		deserialize(toFill, inputStream, inspectionStrategy, formatStrategy);
	}

	public void deserialize(Object toFill, InputStream inputStream, String inspectionStrategy, String formatStrategy) {
		SerializationData serializationData = getFormatStrategy(formatStrategy).unmarshall(inputStream);
		getInspectionStrategy(inspectionStrategy).fill(toFill, serializationData);
	}

	public Object deserialize(byte[] data) {
		return deserialize(data, SerializationConstants.DEFAULT_INSPECTION, SerializationConstants.DEFAULT_FORAMT);
	}

	public Object deserialize(InputStream inputStream) {
		return deserialize(inputStream, SerializationConstants.DEFAULT_INSPECTION, SerializationConstants.DEFAULT_FORAMT);
	}

	public Object deserialize(byte[] data, String inspectionStrategy, String formatStrategy) {
		InputStream inputStream = new ByteArrayInputStream(data);
		return deserialize(inputStream, inspectionStrategy, formatStrategy);
	}

	public Object deserialize(InputStream inputStream, String inspectionStrategy, String formatStrategy) {
		SerializationData serializationData = getFormatStrategy(formatStrategy).unmarshall(inputStream);
		return getInspectionStrategy(inspectionStrategy).create(serializationData);
	}

	protected SerializationInspectionStrategy getInspectionStrategy(String name) {
		SerializationInspectionStrategy found = inspectionStrategies.get(name);
		if (found == null)
			throw new SerializationException("Not found inspection strategy for name:" + name);
		return found;
	}

	protected SerializationFormatStrategy getFormatStrategy(String name) {
		SerializationFormatStrategy found = formatStrategies.get(name);
		if (found == null)
			throw new SerializationException("Not found format strategy for name:" + name);
		return found;
	}

	public void addInspectionStrategy(SerializationInspectionStrategy strategy) {
		inspectionStrategies.put(strategy.getName(), strategy);
	}

	public void addFormatStrategy(SerializationFormatStrategy strategy) {
		formatStrategies.put(strategy.getName(), strategy);
	}

	public String aspectName() {
		return ASPECT_NAME;
	}

	public Object getUnderlyingComponent() {
		// TODO Auto-generated method stub
		return null;
	}

	public void beginConfigClass(SchemaClassDefinition iClass) {
	}

	public void endConfigClass(SchemaClassDefinition iClass) {
	}

	public void configAction(SchemaClassElement iAction, Annotation iActionAnnotation, Annotation iGenericAnnotation, XmlActionAnnotation iNode) {
		// TODO Auto-generated method stub

	}

	public void configClass(SchemaClassDefinition iClass, Annotation iAnnotation, XmlClassAnnotation iNode) {
		if (iClass instanceof SchemaClassReflection) {
			String name;
			Class<?> clazz = ((SchemaClassReflection) iClass).getLanguageType();
			XmlRootElement xmlRootElement = clazz.getAnnotation(XmlRootElement.class);
			if (xmlRootElement != null)
				name = xmlRootElement.name();
			else
				name = clazz.getSimpleName();
			iClass.setFeature(SerializationClassFeatures.ROOT_ELEMENT_NAME, name);
		}
	}

	public void configEvent(SchemaEvent iEvent, Annotation iEventAnnotation, Annotation iGenericAnnotation, XmlEventAnnotation iNode) {
		// TODO Auto-generated method stub

	}

	public void configField(SchemaField iField, Annotation iFieldAnnotation, Annotation iGenericAnnotation, Annotation iGetterAnnotation, XmlFieldAnnotation iNode) {

		if (iField instanceof SchemaFieldReflection) {
			Field field = ((SchemaFieldReflection) iField).getField();
			if (field != null) {
				Boolean trans = Modifier.isTransient(field.getModifiers());
				iField.setFeature(SerializationFieldFeatures.TRANSIENT, trans);
			}
		}

		if (iNode != null) {
			XmlAspectAnnotation descriptor = iNode.aspect(SerializationAspect.ASPECT_NAME);

			if (descriptor != null) {
				String trans = descriptor.getAttribute(SerializationFieldFeatures.TRANSIENT.getName());
				if (trans != null) {
					iField.setFeature(SerializationFieldFeatures.TRANSIENT, Boolean.parseBoolean(trans));
				}
			}
		}
	}
}
