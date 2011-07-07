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

import java.util.List;

public interface OntologyModel extends SemanticModel {
	/**
	 * adds a class (and hierarchy) to the ontology
	 * 
	 * @param clazz
	 *          the class to be added
	 */
	public void addClass(Class<?> clazz);

	/**
	 * retuns a list containing all the instances of a class in the current model
	 * 
	 * @param <T>
	 * @param clazz
	 *          the class of the POJOs
	 * @return a list containing all the instances of a class in the current model
	 * @throws Exception
	 */
	public <T> List<T> getInstancesOf(Class<T> clazz) throws Exception;
}
