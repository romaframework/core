/*
 * Copyright 2006-2007 Luca Garulli (luca.garulli--at--assetdata.it)
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
package org.romaframework.core.util;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Helper class to read/write any object using Java5 Xml serializer.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public class XmlSerializerHelper {
	public static byte[] write(Object iObject) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		XMLEncoder encoder = new XMLEncoder(stream);
		try {
			encoder.writeObject(iObject);
		} finally {
			encoder.close();
		}
		return stream.toByteArray();
	}

	public static Object read(byte[] iBuffer) {
		XMLDecoder decoder = new XMLDecoder(new ByteArrayInputStream(iBuffer));
		Object o = null;
		try {
			o = decoder.readObject();
		} finally {
			decoder.close();
		}
		return o;
	}
}
