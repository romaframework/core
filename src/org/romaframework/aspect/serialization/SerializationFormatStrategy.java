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

/**
 * 
 * @author Emanuele Tagliaferri (emanuele.tagliaferri--at--assetdata.it)
 * 
 */
public interface SerializationFormatStrategy {

	/**
	 * Return the name of store strategy.
	 * 
	 * @return the name of store strategy. 
	 */
	public String getName();
	
	/**
	 * Write a data to a data Mainteiner.
	 * 
	 * @param data
	 *          to store.
	 * @return serialized data. 
	 */
	public void marshall(SerializationData data,OutputStream outputStream);

	/**
	 * Read ad an serialized data.
	 * 
	 * @param data to read.
	 * @return the SerializationData data deserialized .
	 */
	public SerializationData unmarshall(InputStream inputStream);
	
}
