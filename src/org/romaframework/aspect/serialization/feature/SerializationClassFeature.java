package org.romaframework.aspect.serialization.feature;

import org.romaframework.core.util.DynaBean;

public class SerializationClassFeature extends DynaBean {

	private static final long	serialVersionUID	= 2088003531302502412L;
	
	public static final String ROOT_ELEMENT_NAME = "rootElementName";
	
	public SerializationClassFeature() {
		defineAttribute(ROOT_ELEMENT_NAME, null);
	}

}
