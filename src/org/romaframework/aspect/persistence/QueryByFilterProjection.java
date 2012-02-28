package org.romaframework.aspect.persistence;

public class QueryByFilterProjection {

	public enum ProjectionOperator {
		PLAIN, COUNT, MIN, MAX, AVG
	}

	private String							field;
	private ProjectionOperator	operator;

	public QueryByFilterProjection(String field) {
		this(field, ProjectionOperator.PLAIN);
	}

	public QueryByFilterProjection(String field, ProjectionOperator operator) {
		this.field = field;
		this.operator = operator;
	}

	public String getField() {
		return field;
	}

	public ProjectionOperator getOperator() {
		return operator;
	}

}
