/*
 * Copyright 2006 Luca Garulli (luca.garulli--at--assetdata.it)
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

package org.romaframework.core.domain.entity;

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.core.annotation.CoreClass;
import org.romaframework.aspect.core.annotation.CoreClass.LOADING_MODE;
import org.romaframework.aspect.core.annotation.CoreField;
import org.romaframework.aspect.security.Secure;

/**
 * Implements the inheritance-by-composition pattern.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 * 
 * @param <T>
 *          The entity class to be contained and handled as an inheritance.
 */
@CoreClass(loading = LOADING_MODE.EARLY)
public interface ComposedEntity<T> extends Secure {

	public static final String	NAME	= "entity";

	@CoreField(expand = AnnotationConstants.TRUE)
	public T getEntity();

	public void setEntity(T iEntity);
}
