package org.romaframework.aspect.serialization.feature;

import org.romaframework.core.util.DynaBean;

public class SerializationFieldFeature extends DynaBean {

	private static final long		serialVersionUID	= 3240405485393151256L;

	public static final String	TRANSIENT					= "transient";

	public SerializationFieldFeature() {
		defineAttribute(TRANSIENT, Boolean.FALSE);
	}

}
