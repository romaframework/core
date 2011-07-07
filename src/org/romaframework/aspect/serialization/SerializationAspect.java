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

import java.io.InputStream;
import java.io.OutputStream;

import org.romaframework.core.aspect.Aspect;

/**
 * 
 * @author Emanuele Tagliaferri (emanuele.tagliaferri--at--assetdata.it)
 * 
 */
public interface SerializationAspect extends Aspect {

	public static final String	ASPECT_NAME	= "serialization";

	/**
	 * Serialize and store an object.
	 * 
	 * @param toSerialize
	 *          object to serialize.
	 * @return the serialized data.
	 */
	public byte[] serialize(Object toSerialize);

	/**
	 * Serialize and store an object.
	 * 
	 * @param toSerialize
	 *          object to serialize.
	 * @param outputStream
	 *          where write.
	 */
	public void serialize(Object toSerialize, OutputStream outputStream);

	/**
	 * Serialize and store an object.
	 * 
	 * @param toSerialize
	 *          object to serialize.
	 * @param inspectionStrategy
	 *          the strategy of object inspection.
	 * @param formatStrategy
	 *          the strategy of formatting.
	 * @return the serialized data.
	 */
	public byte[] serialize(Object toSerialize, String inspectionStrategy, String formatStrategy);

	/**
	 * Serialize and store an object.
	 * 
	 * @param toSerialize
	 *          object to serialize.
	 * @param inspectionStrategy
	 *          the strategy of object inspection.
	 * @param formatStrategy
	 *          the strategy of formatting.
	 * @param outputStream
	 *          where write.
	 */
	public void serialize(Object toSerialize, OutputStream outputStream, String inspectionStrategy, String formatStrategy);

	/**
	 * Deserialize an object.
	 * 
	 * @param data
	 *          to deserialize.
	 * @return object create from deserialization.
	 */
	public Object deserialize(byte[] data);

	/**
	 * Deserialize an object.
	 * 
	 * @param inputStream
	 *          to deserialize.
	 * @return object create from deserialization.
	 */
	public Object deserialize(InputStream inputStream);

	/**
	 * Deserialize an object
	 * 
	 * @param data
	 *          to deserialize.
	 * @param inspection
	 *          Strategy for object inspection.
	 * @param formatStrategy
	 *          for serialization.
	 * @return object create from deserialization.
	 */
	public Object deserialize(byte[] data, String inspectionStrategy, String formatStrategy);

	/**
	 * Deserialize an object
	 * 
	 * @param inputStream
	 *          to deserialize.
	 * @param inspection
	 *          Strategy for object inspection.
	 * @param formatStrategy
	 *          for serialization.
	 * @return object create from deserialization.
	 */
	public Object deserialize(InputStream inputStream, String inspectionStrategy, String formatStrategy);

	/**
	 * Deserialize an object.
	 * 
	 * @param toFill
	 *          object to fill with deserialized data.
	 * @param data
	 *          to deserialize.
	 */
	public void deserialize(Object toFill, byte[] data);

	/**
	 * Deserialize an object.
	 * 
	 * @param toFill
	 *          object to fill with deserialized data.
	 * @param inputStream
	 *          to deserialize.
	 */
	public void deserialize(Object toFill, InputStream inputStream);

	/**
	 * Deserialize an object
	 * 
	 * @param data
	 *          to deserialize.
	 * @param inspection
	 *          Strategy for object inspection.
	 * @param formatStrategy
	 *          for serialization.
	 * @return toFill object to fill with deserialized data.
	 */
	public void deserialize(Object toFill, byte[] data, String inspectionStrategy, String formatStrategy);

	/**
	 * Deserialize an object
	 * 
	 * @param inputStream
	 *          to deserialize.
	 * @param inspection
	 *          Strategy for object inspection.
	 * @param formatStrategy
	 *          for serialization.
	 * @return toFill object to fill with deserialized data.
	 */
	public void deserialize(Object toFill, InputStream inputStream, String inspectionStrategy, String formatStrategy);

	/**
	 * Add an inspection strategy to the aspect.
	 * 
	 * @param strategy
	 *          to add.
	 */
	public void addInspectionStrategy(SerializationInspectionStrategy strategy);

	/**
	 * Brief add a store strategy to the aspect
	 * 
	 * @param strategy
	 *          to add.
	 */
	public void addFormatStrategy(SerializationFormatStrategy strategy);
}
