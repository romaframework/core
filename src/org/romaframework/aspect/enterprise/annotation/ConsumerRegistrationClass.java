package org.romaframework.aspect.enterprise.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConsumerRegistrationClass {

	String registryURI();

	String username();

	String password();

	String organizationPackage();

	String serviceDesc() default "";
	
	String author() default "";

	//this Annotation set up the service icon path 
	String iconPath() default ""; 
	
	//Consumer Service Name
	String serviceName();

}
