package org.romaframework.aspect.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SecurityEvent {
	/**
	 * the account names, profile names and group names that can execute this event (regular expression)
	 */
	String[] roles();
}
