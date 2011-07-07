package org.romaframework.aspect.serialization.feature;

import org.romaframework.aspect.serialization.SerializationAspect;
import org.romaframework.core.schema.Feature;
import org.romaframework.core.schema.FeatureType;

public class SerializationClassFeatures {

	public static final Feature<String>	ROOT_ELEMENT_NAME	= new Feature<String>(SerializationAspect.ASPECT_NAME, "rootElementName", FeatureType.CLASS, String.class);

}
