package org.romaframework.aspect.validation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.romaframework.aspect.core.annotation.AnnotationConstants;

/**
 * Annotation for fields and getters to use to express validation constraints. Note that before 2.0 all these annotations was part
 * of View Aspect (@ViewField).
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 * @since 2.0
 */
@Target( { ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidationField {
	byte required() default AnnotationConstants.UNSETTED;

	/**
	 * Validation rule to check against it
	 */
	String match() default AnnotationConstants.DEF_VALUE;

	/**
	 * Minimum value for numeric fields and minimum length for strings
	 */
	int min() default DEF_MIN;

	/**
	 * Maximum value for numeric fields and Maximum length for strings
	 */
	int max() default DEF_MAX;

	public static final int	DEF_MIN	= Integer.MIN_VALUE;
	public static final int	DEF_MAX	= Integer.MAX_VALUE;
}
