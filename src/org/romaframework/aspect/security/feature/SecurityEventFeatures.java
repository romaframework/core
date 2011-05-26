package org.romaframework.aspect.security.feature;

import org.romaframework.aspect.security.SecurityAspect;
import org.romaframework.core.schema.Feature;
import org.romaframework.core.schema.FeatureType;

public class SecurityEventFeatures {

	public static final Feature<String[]>	ROLES	= new Feature<String[]>(SecurityAspect.ASPECT_NAME, "roles",FeatureType.ACTION,String[].class);
}
