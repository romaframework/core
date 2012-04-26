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

package org.romaframework.aspect.authentication;

import org.romaframework.core.schema.SchemaAction;
import org.romaframework.core.schema.SchemaClass;
import org.romaframework.core.schema.SchemaEvent;
import org.romaframework.core.schema.SchemaField;

/**
 * Listener interface to handle permissions. Aspects and modules can implement this interface to handle permissions.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public interface UserObjectPermissionListener {

	/**
	 * Check if an user is allowed to access to specified class
	 * 
	 * @param iClass
	 *          to check.
	 * @return true if allowed else false.
	 */
	public boolean allowClass(SchemaClass iClass);

	/**
	 * Check if an user is allowed to access to specified field
	 * 
	 * @param iField
	 *          to check.
	 * @return true if allowed else false.
	 */
	public boolean allowField(SchemaField iField);

	/**
	 * Check if an user is allowed to access to specified action
	 * 
	 * @param iAction
	 *          to check.
	 * @return true if allowed else false.
	 */
	public boolean allowAction(SchemaAction iAction);

	/**
	 * Check if an user is allowed to access to specified event
	 * 
	 * @param iEvent
	 *          to check.
	 * @return true if allowed else false.
	 */
	public boolean allowEvent(SchemaEvent iEvent);
}
