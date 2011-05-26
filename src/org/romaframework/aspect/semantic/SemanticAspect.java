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

import org.romaframework.aspect.persistence.Query;
import org.romaframework.aspect.semantic.exception.SemanticQueryException;
import org.romaframework.core.aspect.Aspect;

/**
 * This aspect allows to use POJOs to generate semantic-web representations
 * of data.
 * 
 * <br>
 * IMPORTANT: PLEASE DO NOT RELY ON THIS RESOURCE, IT IS UNDER DEFINITION AND 
 * HEAVY DEVELOPMENT
 *  
 * @author Luigi Dell'Aquila (luigi.dellaquila@assetdata.it)
 * 
 */
public interface SemanticAspect extends Aspect{

  public static final String    ASPECT_NAME = "semantic";
  
  /**
   * returns the semantic representation of an object
   * @param obj the object to be translated in semantic-web representation
   * @return the semantic representation of an object
   */
  public String getSemanticRepresentation(Object obj);
  
  /**
   * returns the semantic representation of an object
   * @param obj the object to be translated in semantic-web representation
   * @param format the representation format
   * @return the semantic representation of an object
   */
  public String getSemanticRepresentation(Object obj, String format);
  
  /**
   * Returns an ontology representation given an object 
   * @param obj thte object that has to be converted in an ontology (it may also be a collection of objects)
   * @return an ontology representation of the given object
   * @throws UnsupportedOperationException if this implementation of the aspect does not support this operation 
   */
  public String getOntology(Object obj) throws UnsupportedOperationException;
  
  /**
   * creates a new semantic model
   * @return a new semantic model
   */
  public SemanticModel createSemanticModel();
  
  /**
   * creates a new ontology model
   * @return a new ontology model
   */
  public OntologyModel createOntologyModel();
  
  /**
   * returns the supported semantic-web formats supported by this implementation
   * @return the supported semantic-web formats supported by this implementation
   */
  public String[] getSupportedFormats();
  
  /**
   * translates a semantic query in a {@link Query}
   * @param query the semantic query
   * @return the corresponding {@link Query}
   * @throws UnsupportedOperationException if this implementation of the aspect does not support this operation
   * @throws SemanticQueryException if this query cannot be translated in a Query
   */
  public Query toQuery(String query) throws UnsupportedOperationException, SemanticQueryException;
  
  /**
   * translates a semantic query in a {@link Query}
   * @param query the semantic query
   * @param language the semantic query language
   * @return the corresponding {@link Query}
   * @throws UnsupportedOperationException if this implementation of the aspect does not support this operation
   * @throws SemanticQueryException if this query cannot be translated in a Query
   */
  public Query toQuery(String query, String language) throws UnsupportedOperationException;
 
  /**
   * returns the supported semantic query languages supported by this implememtation. 
   * The first element of this array (if it is not empty) has to be the
   * default query language.
   * @return the supported semantic query languages supported by this implememtation 
   * (an empty array if {@link #toQuery(String)} and {@link #toQuery(String, String)} 
   * are not supported). 
   */
  public String[] getSupportedQueryLanguages();
}
