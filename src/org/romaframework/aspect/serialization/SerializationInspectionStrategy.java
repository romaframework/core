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

/**
 * 
 * @author Emanuele Tagliaferri (emanuele.tagliaferri--at--assetdata.it)
 * 
 */
public interface SerializationInspectionStrategy {

	/**
	 * Retrieve the name of Inspection Strategy.
	 * 
	 * @return the name of inspection strategy.
	 */
	public String getName();

	/**
	 * Inspect an object and extract the data to store.
	 * 
	 * @param toInspect
	 *          object ot inspect
	 * @return
	 */
	public SerializationData inspect(Object toInspect);

	/**
	 * Create an object from SerializationData.
	 * 
	 * @param data
	 *          to read for fill.
	 * @return object created from data.
	 */
	public Object create(SerializationData data);

	/**
	 * Fill an object with the SerializationData.
	 * 
	 * @param toFill
	 *          object to fill.
	 * @param data
	 *          to read for fill.
	 */
	public void fill(Object toFill, SerializationData data);

}
