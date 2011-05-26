package org.romaframework.core.schema;

public enum FeatureType {

	CLASS("Class"), FIELD("Field"), ACTION("Action"), EVENT("Event"), PARAMETER("Parameter");

	private String	baseName;

	private FeatureType(String baseName) {
		this.baseName = baseName;
	}

	public String getBaseName() {
		return baseName;
	}

}
