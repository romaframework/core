package org.romaframework.aspect.persistence;

import org.romaframework.aspect.persistence.QueryByFilter;
import org.romaframework.aspect.persistence.QueryByFilterItem;
import org.romaframework.aspect.persistence.QueryOperator;

/**
 * An item that allow to manage inverse connection as direct connection. It contains a subQuery for rebuild the original query.
 * <p>
 * 
 * @author Dario Albani
 */
public class QueryByFilterItemReverse implements QueryByFilterItem {
	private QueryByFilter queryByFilter;
	private String field;
	private QueryOperator operator;

	public QueryByFilterItemReverse(QueryByFilter queryByFilter, String field, QueryOperator operator) {
		if(field == null || field.isEmpty())
			throw new NullPointerException("Cannot add a reverse item with reverse field empty");
		if(queryByFilter == null )
			throw new NullPointerException("Cannot add a reverse item with reverse QueryByFilter empty");

		this.setQueryByFilter(queryByFilter);
		this.setField(field);
		this.setOperator(operator);
	}

	public void setQueryByFilter(QueryByFilter queryByFilter) {
		this.queryByFilter = queryByFilter;
	}

	public QueryByFilter getQueryByFilter() {
		return queryByFilter;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getField() {
		return field;
	}

	public void setOperator(QueryOperator operator) {
		this.operator = operator;
	}

	public QueryOperator getOperator() {
		return this.operator;
	}

}
