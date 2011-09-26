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

package org.romaframework.aspect.flow.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.core.schema.FeatureNotSet;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface FlowAction {
	/**
	 * Next class where to instance an object to show after the method is executed without exceptions.
	 * 
	 * @return
	 */
	Class<?> next() default FeatureNotSet.class;

	/**
	 * Position where to render the next object. If not specified default desktop position is taken.
	 * 
	 * @return
	 */
	String position() default AnnotationConstants.DEF_VALUE;

	/**
	 * Error class where to instance an object if the method throws uncaught exception.
	 * 
	 * @return
	 */
	String error() default AnnotationConstants.DEF_VALUE;

	/**
	 * If defined return back after the execution of action.
	 * 
	 * @return
	 */
	AnnotationConstants back() default AnnotationConstants.UNSETTED;

	/**
	 * If set to true the aspect will display a message popup requiring a confirm from user
	 * 
	 * @return
	 */
	AnnotationConstants confirmRequired() default AnnotationConstants.UNSETTED;

	/**
	 * confirm message or i18n key to be used for confirm message, if not set the message will be retrieved from i18n with the
	 * following key: $<class-name>.<action-name>.confirmMessage
	 * 
	 * @return
	 */
	String confirmMessage() default AnnotationConstants.DEF_VALUE;
}
