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
import org.romaframework.aspect.view.SelectionMode;
import org.romaframework.core.binding.Bindable;

/**
 * Annotation for fields and getters to use to declare presentation features.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewField {
	String label() default AnnotationConstants.DEF_VALUE;

	String description() default AnnotationConstants.DEF_VALUE;

	AnnotationConstants visible() default AnnotationConstants.UNSETTED;

	AnnotationConstants enabled() default AnnotationConstants.UNSETTED;

	String render() default AnnotationConstants.DEF_VALUE;

	String position() default AnnotationConstants.DEF_VALUE;

	String style() default AnnotationConstants.DEF_VALUE;

	String selectionField() default AnnotationConstants.DEF_VALUE;

	SelectionMode selectionMode() default SelectionMode.SELECTION_MODE_DEFAULT;

	String format() default AnnotationConstants.DEF_VALUE;

	String[] dependsOn() default AnnotationConstants.DEF_VALUE;

	String[] depends() default AnnotationConstants.DEF_VALUE;

	Class<? extends Bindable> displayWith() default Bindable.class;

	/**
	 * Validation rule to check against it. Use @ValidationField instead.
	 */
	@Deprecated
	String match() default AnnotationConstants.DEF_VALUE;

	/**
	 * Minimum value for numeric fields and minimum length for strings. Use @ValidationField instead.
	 */
	@Deprecated
	int min() default DEF_MIN;

	/**
	 * Maximum value for numeric fields and Maximum length for strings. Use @ValidationField instead.
	 */
	@Deprecated
	int max() default DEF_MAX;

	/**
	 * Use @ValidationField instead
	 */
	@Deprecated
	AnnotationConstants required() default AnnotationConstants.UNSETTED;

	@Deprecated
	public static final int	DEF_MIN	= Integer.MIN_VALUE;
	@Deprecated
	public static final int	DEF_MAX	= Integer.MAX_VALUE;
}
