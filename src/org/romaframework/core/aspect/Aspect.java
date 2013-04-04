/*
 * Copyright 2006-2009 Luca Garulli (luca.garulli--at--assetdata.it)
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

package org.romaframework.core.aspect;

import org.romaframework.core.schema.SchemaAction;
import org.romaframework.core.schema.SchemaClassDefinition;
import org.romaframework.core.schema.SchemaEvent;
import org.romaframework.core.schema.SchemaField;

/**
 * Interface to define an Aspect. An Aspect is a Java interface that define a generic behavior.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public interface Aspect {
	
	/**
	 * returns the name of the aspect
	 * 
	 * @return
	 */
	public String aspectName();

	/**
	 * 
	 * @param iClass
	 */
	public void beginConfigClass(SchemaClassDefinition iClass);

	/**
	 * 
	 * @param iClass
	 */
	public void configClass(SchemaClassDefinition iClass);

	/**
	 * 
	 * @param iField
	 */
	public void configField(SchemaField iField);

	/**
	 * 
	 * @param iAction
	 */
	public void configAction(SchemaAction iAction);

	/**
	 * 
	 * @param iEvent
	 */
	public void configEvent(SchemaEvent iEvent);

	
	/**
	 * 
	 * @param iClass
	 */
	public void endConfigClass(SchemaClassDefinition iClass);

	/**
	 * Allow to access to the underlying component bypassing the Aspect concept. This allow to make things dirty but sometimes you
	 * need it. Remember that the code will be dependent by the implementation and could need changes if the implementation change.
	 * 
	 * @return The underlying component as Object. Cast it to the known implementation to use it.
	 */
	public Object getUnderlyingComponent();
}
