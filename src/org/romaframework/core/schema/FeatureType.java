package org.romaframework.core.schema;

/**
 * Enum that represents the types of entities that can be managed by roma
 * 
 * <ul>
 * <li>CLASS</li>
 * <li>FIELD</li>
 * <li>ACTION</li>
 * <li>EVENT</li>
 * <li>PARAMETER</li>
 * </ul>
 *
 */
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
