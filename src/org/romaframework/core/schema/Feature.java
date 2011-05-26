package org.romaframework.core.schema;

public class Feature<T> {

	private FeatureType	type;
	private String			aspectName;
	private String			name;
	private int					aspectId;
	private int					featureId;
	private Class<T>		valueType;
	private T						defaultValue;

	public Feature(String aspectName, String name, FeatureType type, Class<T> valueType) {
		this(aspectName, name, type, valueType, null);
	}

	public Feature(String aspectName, String name, FeatureType type, Class<T> valueType, T defaultValue) {
		this.type = type;
		this.aspectName = aspectName;
		this.name = name;
		FeatureRegistry.register(this);
		aspectId = FeatureRegistry.aspectNext(aspectName);
		featureId = FeatureRegistry.featureNext(aspectName, type);
		this.valueType = valueType;
		this.defaultValue = defaultValue;
	}

	public String getAspectName() {
		return aspectName;
	}

	public String getName() {
		return name;
	}

	public int getAspectId() {
		return aspectId;
	}

	public int getFeatureId() {
		return featureId;
	}

	public FeatureType getType() {
		return type;
	}

	public Class<T> getValueType() {
		return valueType;
	}

	public T getDefaultValue() {
		return defaultValue;
	}

	@Override
	public String toString() {
		return getAspectName() + "." + getName() + " " + getType().getBaseName();
	}

}
