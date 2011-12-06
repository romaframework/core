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

package org.romaframework.aspect.view.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.romaframework.aspect.core.annotation.AnnotationConstants;

/**
 * Annotation for methods to use to describe presentation features.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewAction {
	String label() default AnnotationConstants.DEF_VALUE;

	String description() default AnnotationConstants.DEF_VALUE;

	AnnotationConstants visible() default AnnotationConstants.UNSETTED;

	String type() default AnnotationConstants.DEF_VALUE;

	String render() default AnnotationConstants.DEF_VALUE;

	String position() default AnnotationConstants.DEF_VALUE;

	String style() default AnnotationConstants.DEF_VALUE;

	AnnotationConstants bind() default AnnotationConstants.UNSETTED;

	AnnotationConstants enabled() default AnnotationConstants.UNSETTED;



	/**
	 * Use the action as default submit of the form where is rendered.
	 * 
	 * @return
	 */
	AnnotationConstants submit() default AnnotationConstants.UNSETTED;
}
