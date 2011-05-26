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

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import org.romaframework.aspect.serialization.SerializationConstants;
import org.romaframework.aspect.serialization.SerializationData;
import org.romaframework.aspect.serialization.exception.SerializationException;

/**
 * 
 * @author Emanuele Tagliaferri (emanuele.tagliaferri--at--assetdata.it)
 * 
 */
public class NativeSerializationFormatStrategy extends AbstractSerializationFormatStrategy {

	public String getName() {
		return SerializationConstants.FORMAT_NATIVE;
	}

	public SerializationData unmarshall(InputStream inputStream) {
		try {
			return (SerializationData) new ObjectInputStream(inputStream).readObject();
		} catch (Exception e) {
			throw new SerializationException("Error on object deserialization", e);
		}
	}

	public void marshall(SerializationData data, OutputStream outputStream) {
		try {
			new ObjectOutputStream(outputStream).writeObject(data);
		} catch (IOException e) {
			throw new SerializationException("Error on object serialization", e);
		}
	}

}
