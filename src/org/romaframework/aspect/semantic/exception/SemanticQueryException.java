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
package org.romaframework.aspect.semantic.exception;

/**
 * Represents an exception in elaborating a semantic query
 * <br>
 * IMPORTANT: PLEASE DO NOT RELY ON THIS RESOURCE, IT IS UNDER DEFINITION AND 
 * HEAVY DEVELOPMENT 
 * @author Luigi Dell'Aquila (luigi.dellaquila@assetdata.it)
 */
public class SemanticQueryException extends Exception{

  public SemanticQueryException() {
    super();
    // TODO Auto-generated constructor stub
  }

  public SemanticQueryException(String message, Throwable cause) {
    super(message, cause);
    // TODO Auto-generated constructor stub
  }

  public SemanticQueryException(String message) {
    super(message);
    // TODO Auto-generated constructor stub
  }

  public SemanticQueryException(Throwable cause) {
    super(cause);
    // TODO Auto-generated constructor stub
  }

}
