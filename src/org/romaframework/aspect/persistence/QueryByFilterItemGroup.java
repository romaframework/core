package org.romaframework.aspect.persistence;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class QueryByFilterItemGroup implements QueryByFilterItem {

	public static final String			PREDICATE_OR	= QueryByFilter.PREDICATE_OR;
	public static final String			PREDICATE_AND	= QueryByFilter.PREDICATE_AND;

	private String									predicate;
	private List<QueryByFilterItem>	items					= new ArrayList<QueryByFilterItem>();

	public QueryByFilterItemGroup(String predicate) {
		this.predicate = predicate;
	}

	public void addItem(QueryByFilterItem item) {
		items.add(item);
	}

	public String getPredicate() {
		return predicate;
	}

	public List<QueryByFilterItem> getItems() {
		return items;
	}

	public void addItem(String iName, QueryOperator iOperator, Object iValue) {
		addItem(new QueryByFilterItemPredicate(iName, iOperator, iValue));
	}

	public void addItem(String iCondition) {
		addItem(new QueryByFilterItemText(iCondition));
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append(" ( ");
		Iterator<QueryByFilterItem> iter = items.iterator();
		if (iter.hasNext()) {
			buffer.append(iter.next());
			while (iter.hasNext()) {
				buffer.append(" ").append(predicate).append(" ");
				buffer.append(iter.next());
			}
		}
		buffer.append(" ) ");
		return buffer.toString();
	}
}
