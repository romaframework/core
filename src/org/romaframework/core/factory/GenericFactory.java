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
package org.romaframework.core.factory;

import org.romaframework.core.schema.SchemaClass;

/**
 * Generic Factory interface. Factory is a fundamental part of DDD.<br/>
 * Each Factory had to implements this interface in order to be used by Roma components in transparent way.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 * 
 * @param <T>
 *          Concrete class to manage.
 */
public interface GenericFactory<T> {
	public static final String	DEF_SUFFIX	= "Factory";

	/**
	 * Create the object of type 'T' invoking the empty constructor.
	 * 
	 * @return The Object created
	 */
	public T create();

	/**
	 * Create the object of type 'T' passing arguments 'iArgs' to the constructor.
	 * 
	 * @param iArgs
	 *          Variable arguments to pass to the constructor.
	 * @return The Object created
	 */
	public T createInstance(Object... iArgs);

	/**
	 * returns the {@link Class} of the objects that are instantiated by this factory
	 * 
	 * @return the {@link Class} of the objects that are instantiated by this factory
	 */
	public <Z extends T> Class<Z> getEntityClass();

	/**
	 * returns the {@link SchemaClass} of the objects that are instantiated by this factory
	 * 
	 * @return the {@link SchemaClass} of the objects that are instantiated by this factory
	 */
	public SchemaClass getEntitySchemaClass();
}
