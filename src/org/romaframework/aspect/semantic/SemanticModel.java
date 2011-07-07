/*
 * Copyright 2008 Luigi Dell'Aquila (luigi.dellaquila@assetdata.it)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.romaframework.aspect.semantic;

import java.io.InputStream;

/**
 * This object represents a semantic model
 * 
 * <br>
 * IMPORTANT: PLEASE DO NOT RELY ON THIS RESOURCE, IT IS UNDER DEFINITION AND HEAVY DEVELOPMENT
 * 
 * @author Luigi Dell'Aquila (luigi.dellaquila@assetdata.it)
 * 
 */
public interface SemanticModel {

	/**
	 * adds an object to this model
	 * 
	 * @param obj
	 *          the object to be added to this model
	 */
	public void addObject(Object obj);

	/**
	 * returns the semantic representation of this model
	 * 
	 * @return the semantic representation of this model
	 */
	public String getRepresentation();

	/**
	 * returns the semantic representation of this model in the given format
	 * 
	 * @param format
	 *          the format (see supported formats in {@link SemanticAspect#getSupportedFormats()})
	 * @return the semantic representation of this model
	 */
	public String getRepresentation(String format);

	/**
	 * returns the implementation-specific model object
	 * 
	 * @return the implementation-specific model object
	 */
	public Object getUnderlyingImplementation();

	/**
	 * reads semantic information as a stream (e.g. RDF/XML). The new information is added to the current model
	 * 
	 * @param inputStream
	 *          the stream containing semantic information
	 * @param format
	 *          the stream format (has to be supported by the SemanticAspect, see {@link SemanticAspect#getSupportedFormats()})
	 */
	public void read(InputStream inputStream, String format);
}
