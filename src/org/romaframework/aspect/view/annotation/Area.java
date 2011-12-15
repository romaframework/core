package org.romaframework.aspect.view.annotation;

import org.romaframework.aspect.core.annotation.AnnotationConstants;

public @interface Area {

	/**
	 * The type of form area placeholder,column,row,grid,form
	 * 
	 * @return
	 */
	public String type() default "placeholder";

	/**
	 * the full path name of area. example:"main/fields" example:"main/fields/user"
	 * 
	 * @return
	 */
	public String fullName() default AnnotationConstants.DEF_VALUE;

	/**
	 * The size of area used by same type of areas.
	 * 
	 * @return
	 */
	public int size() default 2;
}
