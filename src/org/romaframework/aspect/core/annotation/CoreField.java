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

package org.romaframework.aspect.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface CoreField {

	/**
	 * Embedded type class. If the field is a collection, this attribute expresses the field contained in. Specifying this attribute
	 * will override the default type self-detected using reflection.
	 * 
	 * @return Embedded class instance, if any
	 */
	String embeddedType() default "Object";

	/**
	 * Defines if the linked instance is embedded to the object. For complex types (not primitive types) default is false. If is false
	 * means the linked object is linked.
	 * 
	 * @return AnnotationConstants.TRUE, AnnotationConstants.FALSE or AnnotationConstants.UNSETTED
	 */
	AnnotationConstants embedded() default AnnotationConstants.UNSETTED;

	/**
	 * Tell to use or not the runtime type found. By default is false, meaning the type found using the reflection is taken. If is
	 * setted to true then the runtime type is taken. Useful if you want to display the real implementation object fields and actions
	 * and not that defined in the interface or abstract class.
	 * 
	 * @return AnnotationConstants.TRUE, AnnotationConstants.FALSE or AnnotationConstants.UNSETTED
	 */
	AnnotationConstants useRuntimeType() default AnnotationConstants.UNSETTED;

	/**
	 * Tell if expand the fields of current field to the parent, the default value is false,
	 * 
	 * @return AnnotationConstants.TRUE, AnnotationConstants.FALSE or AnnotationConstants.UNSETTED
	 */
	AnnotationConstants expand() default AnnotationConstants.UNSETTED;
}
