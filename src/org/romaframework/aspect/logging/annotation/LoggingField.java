/*
 * Copyright 2006-2007 Giordano Maestro (giordano.maestro--at--assetdata.it)
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
package org.romaframework.aspect.logging.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.romaframework.aspect.core.annotation.AnnotationConstants;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface LoggingField {

	byte enabled() default AnnotationConstants.UNSETTED;

	/**
	 * Level of logging.
	 */
	int level() default Integer.MIN_VALUE;

	/**
	 * Category of logging.
	 */
	String category() default AnnotationConstants.DEF_VALUE;

	/**
	 * The log output mode, it can be database file or others
	 * 
	 * @return
	 */
	String mode() default AnnotationConstants.DEF_VALUE;

	/**
	 * The message to print when the value change
	 * 
	 * @return
	 */
	String post() default AnnotationConstants.DEF_VALUE;

	/**
	 * The message to print if an exception is thrown
	 * 
	 * @return
	 */
	String exception() default AnnotationConstants.DEF_VALUE;

	/**
	 * The list of exceptions that generate the log print
	 * 
	 * @return
	 */
	Class<?>[] exceptionsToLog() default { Throwable.class };
}
