package org.romaframework.aspect.security.feature;

public class SecurityEventFeatures extends SecurityBaseFeatures {

	public SecurityEventFeatures() {
		super();
		defineAttribute(ROLES, null);
	}

	public static final String	ROLES	= "roles";
}
