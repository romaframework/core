/*
 * Copyright 2006 Luca Garulli (luca.garulli--at--assetdata.it)
 *  Giordano Maestro (giordano.maestro--at--assetdata.it)
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

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CoreAction {
	/***************** ACTION *****************/
	/**
	 * Call this action in place of the requested action.
	 */
	String callbackOnAction() default AnnotationConstants.DEF_VALUE;

	/**
	 * Call this action just before the requested action.
	 */
	String callbackBeforeAction() default AnnotationConstants.DEF_VALUE;

	/**
	 * Call this action just after the requested action.
	 */
	String callbackAfterAction() default AnnotationConstants.DEF_VALUE;

	/*************** FIELD READ *****************/

	/**
	 * Called in place of the field read.
	 */
	String callbackOnFieldRead() default AnnotationConstants.DEF_VALUE;

	/**
	 * Called just before the reading of the field.
	 */
	String callbackBeforeFieldRead() default AnnotationConstants.DEF_VALUE;

	/**
	 * Called just afgter the reading of the field.
	 */
	String callbackAfterFieldRead() default AnnotationConstants.DEF_VALUE;

	/**************** FIELD WRITE ****************/

	/**
	 * Called in place of the field read.
	 */
	String callbackOnFieldWrite() default AnnotationConstants.DEF_VALUE;

	/**
	 * Called just before the reading of the field.
	 */
	String callbackBeforeFieldWrite() default AnnotationConstants.DEF_VALUE;

	/**
	 * Called just afgter the reading of the field.
	 */
	String callbackAfterFieldWrite() default AnnotationConstants.DEF_VALUE;
}
