package org.romaframework.aspect.serialization.feature;

import org.romaframework.aspect.serialization.SerializationAspect;
import org.romaframework.core.schema.Feature;
import org.romaframework.core.schema.FeatureType;

public class SerializationFieldFeatures {

	public static final Feature<Boolean>	TRANSIENT	= new Feature<Boolean>(SerializationAspect.ASPECT_NAME, "transient", FeatureType.FIELD, Boolean.class,
																											Boolean.FALSE);

}
