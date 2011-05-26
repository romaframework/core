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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.sf.ezmorph.bean.MorphDynaBean;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertySetStrategy;

import org.apache.commons.beanutils.DynaProperty;
import org.romaframework.aspect.serialization.SerializationConstants;
import org.romaframework.aspect.serialization.SerializationData;
import org.romaframework.aspect.serialization.SerializationElement;
import org.romaframework.aspect.serialization.SerializationHelper;
import org.romaframework.aspect.serialization.exception.SerializationException;
import org.romaframework.core.util.DynaBean;

/**
 * 
 * @author Emanuele Tagliaferri (emanuele.tagliaferri--at--assetdata.it)
 * 
 */
public class JsonFullSerializationFormatStrategy extends AbstractSerializationFormatStrategy {

	public String getName() {
		return SerializationConstants.FORMAT_JSON_FULL;
	}
	
	private class FeaturesPropertySetStrategy extends PropertySetStrategy {

		@Override
		public void setProperty(Object target, String name, Object value) throws JSONException {
			if ("allFeatures".equals(name)) {
				Map<String, DynaBean> newValues = new HashMap<String, DynaBean>();
				Map<String, MorphDynaBean> values = (Map<String, MorphDynaBean>) value;
				for (Map.Entry<String, MorphDynaBean> entry : values.entrySet()) {
					MorphDynaBean bean = entry.getValue();
					DynaBean dynaBean = new DynaBean();
					for (DynaProperty dn : bean.getDynaClass().getDynaProperties()) {
						dynaBean.defineAttribute(dn.getName(), null);
						dynaBean.setAttribute(dn.getName(), bean.get(dn.getName()));
					}
					newValues.put(entry.getKey(), dynaBean);
				}
				value = newValues;
			}
			PropertySetStrategy.DEFAULT.setProperty(target, name, value);
		}

	}

	public void marshall(SerializationData data, OutputStream outputStream) {
		try {
			JSONSerializer.toJSON(data).write(new OutputStreamWriter(outputStream));
		} catch (JSONException e) {
			throw new SerializationException("Error on json serialization.", e);
		}
	}

	public SerializationData unmarshall(InputStream inputStream) {
		try {
			JsonConfig config = new JsonConfig();
			config.setRootClass(SerializationData.class);
			config.setCollectionType(ArrayList.class);
			Map<String, Class<?>> classesMap = new HashMap<String, Class<?>>();
			classesMap.put("collection", SerializationData.class);
			classesMap.put("fields", SerializationElement.class);
			classesMap.put("actions", SerializationElement.class);
			classesMap.put("events", SerializationElement.class);
			classesMap.put("allFeatures", HashMap.class);
			config.setClassMap(classesMap);

			config.setPropertySetStrategy(new FeaturesPropertySetStrategy());

			JSONObject jsonObject = JSONObject.fromObject(SerializationHelper.inputStreamToString(inputStream));
			SerializationData serializationData = (SerializationData) jsonObject.toBean(jsonObject, config);
			return serializationData;
		} catch (JSONException e) {
			throw new SerializationException("Error on json serialization.", e);
		}
	}

}
