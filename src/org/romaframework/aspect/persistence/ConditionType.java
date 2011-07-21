package org.romaframework.aspect.persistence;

public enum ConditionType {
	
	FIELD_LIKE("LIKE"), FIELD_NOT_EQUALS("<>"), FIELD_MAJOR_EQUALS(">="), FIELD_MINOR_EQUALS("<="), FIELD_MAJOR(">"), FIELD_MINOR("<"), FIELD_EQUALS("="), FIELD_CONTAINS("contains"), FIELD_IN(
			"in"), FIELD_NOT_IN("not in");

	private String	conditionValue;

	private ConditionType(String conditionValue) {
		this.conditionValue = conditionValue;
	}

	public String getConditionValue() {
		return conditionValue;
	}
}