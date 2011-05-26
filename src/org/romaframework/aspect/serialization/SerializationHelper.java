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

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.romaframework.aspect.core.CoreAspect;
import org.romaframework.aspect.core.feature.CoreClassFeatures;
import org.romaframework.aspect.core.feature.CoreFieldFeatures;
import org.romaframework.aspect.serialization.exception.SerializationException;
import org.romaframework.core.Roma;
import org.romaframework.core.schema.SchemaClass;
import org.romaframework.core.schema.SchemaFeatures;
import org.romaframework.core.util.DynaBean;

/**
 * @author Emanuele Tagliaferri (emanuele.tagliaferri--at--assetdata.it)
 */
public class SerializationHelper {

	/**
	 * Transform the features to make serializable
	 * 
	 * @param features
	 */
	public static void allowSerializeFeatures(SchemaFeatures features) {
		DynaBean core = features.getFeatures(CoreAspect.ASPECT_NAME);
		if (core != null && core.existAttribute(CoreFieldFeatures.EMBEDDED_TYPE)) {
			SchemaClass schemaClass = (SchemaClass) core.getAttribute(CoreFieldFeatures.EMBEDDED_TYPE);
			if (schemaClass != null) {
				core.setAttribute(CoreFieldFeatures.EMBEDDED_TYPE, schemaClass.getName());
			}
		}
		// DynaBean view = features.getFeatures("view");
		if (core != null && core.existAttribute(CoreClassFeatures.ENTITY)) {
			SchemaClass clazz = (SchemaClass) core.getAttribute(CoreClassFeatures.ENTITY);
			if (clazz != null)
				core.setAttribute(CoreClassFeatures.ENTITY, clazz.getName());
		}
	}

	/**
	 * Realline the features on deserialization.
	 * 
	 * @param features
	 */
	public static void reallineFeature(SchemaFeatures features) {
		DynaBean core = features.getFeatures(CoreAspect.ASPECT_NAME);
		if (core != null && core.existAttribute(CoreFieldFeatures.EMBEDDED_TYPE)) {
			String schemaClassName = (String) core.getAttribute(CoreFieldFeatures.EMBEDDED_TYPE);
			if (schemaClassName != null)
				core.setAttribute(CoreFieldFeatures.EMBEDDED_TYPE, Roma.schema().getSchemaClass(schemaClassName));
		}
		if (core != null && core.existAttribute(CoreClassFeatures.ENTITY)) {
			String clazz = (String) core.getAttribute(CoreClassFeatures.ENTITY);
			if (clazz != null)
				core.setAttribute(CoreClassFeatures.ENTITY, Roma.schema().getSchemaClass(clazz));
		}
	}

	/**
	 * Brief check if a dynaBean have a attribute with the specifed value.
	 * 
	 * @param bean
	 *          where find attribute.
	 * @param name
	 *          name of attribute.
	 * @param value
	 *          to confront.
	 * @return true if dynaBean have an attribute with param value, otherwise false.
	 */
	public static boolean checkAttributeValue(DynaBean bean, String name, Object value) {
		if (bean != null && bean.existAttribute(name)) {
			return value == null ? bean.getAttribute(name) == null : value.equals(bean.getAttribute(name));
		}
		return false;
	}

	/**
	 * Transform an input stream to a string.
	 * 
	 * @param is
	 *          input to read.
	 * @return result String
	 */
	public static String inputStreamToString(java.io.InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuffer sb = new StringBuffer();
		try {
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (Exception ex) {
			throw new SerializationException("Error on Input Stream read.", ex);
		} finally {
			try {
				is.close();
			} catch (Exception ex) {
			}
		}
		return sb.toString();
	}

}
