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

package org.romaframework.aspect.hook.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.romaframework.aspect.core.annotation.AnnotationConstants;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface HookAction {
	HookScope scope() default HookScope.DEFAULT;

	/**
	 * Call this method in place of the requested action.
	 */
	String hookAroundAction() default AnnotationConstants.DEF_VALUE;

	/**
	 * Call this method just before the requested action.
	 */
	String hookBeforeAction() default AnnotationConstants.DEF_VALUE;

	/**
	 * Call this method just after the requested action is executed.
	 */
	String hookAfterAction() default AnnotationConstants.DEF_VALUE;

	/**
	 * Called in place of the field read.
	 */
	String hookAroundFieldRead() default AnnotationConstants.DEF_VALUE;

	/**
	 * Called just before the reading of the field.
	 */
	String hookBeforeFieldRead() default AnnotationConstants.DEF_VALUE;

	/**
	 * Called just after the reading of the field.
	 */
	String hookAfterFieldRead() default AnnotationConstants.DEF_VALUE;

	/**
	 * Called in place of the field read.
	 */
	String hookAroundFieldWrite() default AnnotationConstants.DEF_VALUE;

	/**
	 * Called just before the reading of the field.
	 */
	String hookBeforeFieldWrite() default AnnotationConstants.DEF_VALUE;

	/**
	 * Called just after the reading of the field.
	 */
	String hookAfterFieldWrite() default AnnotationConstants.DEF_VALUE;

	/**
	 * Call this method in place of the requested event.
	 */
	String hookAroundEvent() default AnnotationConstants.DEF_VALUE;

	/**
	 * Call this method just before the requested action.
	 */
	String hookBeforeEvent() default AnnotationConstants.DEF_VALUE;

	/**
	 * Call this method just after the requested event is executed.
	 */
	String hookAfterEvent() default AnnotationConstants.DEF_VALUE;

}
