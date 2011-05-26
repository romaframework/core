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

import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.romaframework.aspect.serialization.SerializationConstants;
import org.romaframework.aspect.serialization.SerializationData;
import org.romaframework.aspect.serialization.SerializationElement;
import org.romaframework.aspect.serialization.SerializationHelper;
import org.romaframework.aspect.serialization.exception.SerializationException;
import org.romaframework.core.Roma;
import org.romaframework.core.schema.SchemaHelper;

/**
 * 
 * @author Emanuele Tagliaferri (emanuele.tagliaferri--at--assetdata.it)
 * 
 */
public class JsonSerializationFormatStrategy extends AbstractSerializationFormatStrategy {

	public String getName() {
		return SerializationConstants.FORMAT_JSON;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.romaframework.aspect.serialization.SerializationFormatStrategy#marshall(org.romaframework.aspect.serialization.
	 * SerializationData)
	 */
	public void marshall(SerializationData data, OutputStream outputStream) {
		try {
			JSONObject js = new JSONObject();
			js.element(data.getName(), getJSONValue(data));
			Writer writer = new OutputStreamWriter(outputStream);
			js.write(writer);
			writer.flush();
		} catch (Exception e) {
			throw new SerializationException("Error on json serialization.", e);
		}
	}

	/**
	 * Retrieve the json from the fields.
	 * 
	 * @param fields
	 *          field to jsonize.
	 * @return the correspondent json object.
	 */
	private JSONObject getJSONFields(List<SerializationElement> fields) {
		JSONObject js = new JSONObject();
		for (SerializationElement serializationElement : fields) {
			js.element(serializationElement.getName(), getJSONValue(serializationElement.getData()));
		}
		return js;
	}

	/**
	 * Retrieve the json for a SerializationData.
	 * 
	 * @param data
	 *          to jsonize.
	 * @return the correspondent json value.
	 */
	private Object getJSONValue(SerializationData data) {
		if (data == null)
			return new JSONObject(true);
		Object value = null;
		if (data.containSimpleValue()) {
			value = data.getSimpleValue();
			if (value instanceof Date) {
				value = Roma.i18n().getDateTimeFormat().format((Date)value);
			}
		} else if (data.containCollection()) {
			List<Object> values = new ArrayList<Object>();
			for (SerializationData sdata : data.getCollection()) {
				values.add(getJSONValue(sdata));
			}
			value = JSONArray.fromObject(values);
		} else {
			value = getJSONFields(data.getFields());
		}
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.romaframework.aspect.serialization.SerializationFormatStrategy#unmarshall(byte[])
	 */
	public SerializationData unmarshall(InputStream inputStream) {
		try {
			// TODO: verify.
			JSONObject jsonObject = JSONObject.fromObject(SerializationHelper.inputStreamToString(inputStream));
			SerializationData serializationData = getSerializationData(jsonObject);
			SerializationElement element = serializationData.getFields().get(0);
			serializationData = element.getData();
			serializationData.setName(element.getName());
			return serializationData;
		} catch (JSONException e) {
			throw new SerializationException("Error on json deserialization.", e);
		}
	}

	/**
	 * Create a serialization data from an json object.
	 * 
	 * @param value
	 *          json object to read.
	 * @return correspondent json serialization data.
	 */
	private SerializationData getSerializationData(Object value) {
		if (value == null)
			return null;
		SerializationData data = new SerializationData();

		if (SchemaHelper.getClassForJavaTypes(value.getClass().getSimpleName()) != null) {
			data.setSimpleValue(value);

		} else if (value instanceof JSONArray) {
			JSONArray jsonArray = (JSONArray) value;
			List<SerializationData> elements = new ArrayList<SerializationData>();
			for (Object arrayEntry : jsonArray) {
				elements.add(getSerializationData(arrayEntry));
			}
			data.setCollection(elements);
		} else if (value instanceof JSONObject) {

			JSONObject jsonObject = (JSONObject) value;
			if (jsonObject.isNullObject())
				return null;
			List<SerializationElement> fields = new ArrayList<SerializationElement>();
			for (Object field : jsonObject.entrySet()) {
				Map.Entry<String, Object> fieldEntry = (Map.Entry<String, Object>) field;
				SerializationElement element = new SerializationElement();
				element.setName(fieldEntry.getKey());
				element.setData(getSerializationData(fieldEntry.getValue()));
				fields.add(element);
			}
			data.setFields(fields);
		} else
			return null;

		return data;
	}

}
