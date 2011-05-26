/*
 * Copyright 2009 Luigi Dell'Aquila (luigi.dellaquila@assetdata.it)
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
package org.romaframework.aspect.security;

import org.romaframework.core.aspect.Aspect;
import org.romaframework.core.schema.SchemaClassElement;
import org.romaframework.core.schema.SchemaField;

public interface SecurityAspect extends Aspect {

	public static final String    ASPECT_NAME = "security";
	
	/**
	 * checks whether the currently logged user can read this field 
	 * @param obj the object
	 * @param fieldName the field name
	 * @return true if the currently logged user can read this field
	 */
	public boolean canRead(Object obj, SchemaField iSchemaField);
	
	/**
	 * checks whether the currently logged user can write this field 
	 * @param obj the object
	 * @param fieldName the field name
	 * @return true if the currently logged user can write this field
	 */
	public boolean canWrite(Object obj, SchemaField iSchemaField);
	
	/**
	 * checks whether the currently logged user can execute this action 
	 * @param obj the object
	 * @param actionName the action
	 * @return true if the currently logged user can execute this action
	 */
	public boolean canExecute(Object obj, SchemaClassElement iSchemaElement);
	
	public Object encrypt(Object obj, String fieldName) throws UnsupportedOperationException;
	
	public Object decrypt(Object obj, String fieldName) throws UnsupportedOperationException;
	
}
