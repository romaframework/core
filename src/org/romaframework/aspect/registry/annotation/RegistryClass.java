/*
 * Copyright 2009 Luca Acquaviva (lacquaviva:at:imolinfo.it)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.romaframework.aspect.registry.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RegistryClass {

	public enum ServiceType {
		WADL, WSDL
	}

	String serviceDesc() default "";

	String registryURI();

	String username() default "";

	String password() default "";

	String organizationPackage() default "";

	String author() default "";

	String wsdlAddress() default "";

	String wadlAddress() default "";

	ServiceType type() default ServiceType.WSDL;

	// this Annotation set up the service icon path
	String iconPath() default "";
}
