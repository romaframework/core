package org.romaframework.aspect.persistence.test;

import org.junit.Assert;
import org.junit.Test;
import org.romaframework.aspect.persistence.QueryByFilter;
import org.romaframework.aspect.persistence.QueryByFilterItemGroup;
import org.romaframework.aspect.persistence.QueryByFilterItemPredicate;
import org.romaframework.aspect.persistence.QueryByFilterItemReverse;
import org.romaframework.aspect.persistence.QueryOperator;
import org.romaframework.core.Roma;

public class QueryByFilterTest {

	@Test
	public void testMergeSimple() {
		QueryByFilter filter = new QueryByFilter(Roma.class);
		filter.addItem("aa", QueryOperator.EQUALS, new Object());

		QueryByFilter qbf = new QueryByFilter(Roma.class);
		qbf.addProjection("bb");
		qbf.addOrder("bb");
		qbf.addItem("bb", QueryOperator.EQUALS, new Object());
		filter.addReverseItem(qbf, "rev");

		QueryByFilter qbf1 = new QueryByFilter(Roma.class);
		qbf1.addProjection("cc");
		qbf1.addOrder("cc");
		qbf1.addItem("cc", QueryOperator.EQUALS, new Object());
		filter.addReverseItem(qbf1, "rev");

		Assert.assertEquals(2, filter.getItems().size());
		QueryByFilter ff = ((QueryByFilterItemReverse) filter.getItems().get(1)).getQueryByFilter();
		Assert.assertEquals(2, ff.getItems().size());
		Assert.assertEquals("bb", ((QueryByFilterItemPredicate) ff.getItems().get(0)).getFieldName());
		Assert.assertEquals("cc", ((QueryByFilterItemPredicate) ff.getItems().get(1)).getFieldName());
		Assert.assertEquals(2, ff.getProjections().size());
		Assert.assertEquals("bb", ff.getProjections().get(0).getField());
		Assert.assertEquals("cc", ff.getProjections().get(1).getField());

		Assert.assertEquals(2, ff.getOrders().size());
		Assert.assertEquals("bb", ff.getOrders().get(0).getFieldName());
		Assert.assertEquals("cc", ff.getOrders().get(1).getFieldName());

	}

	@Test
	public void testMergeGroup() {
		QueryByFilter filter = new QueryByFilter(Roma.class);
		filter.addItem("aa", QueryOperator.EQUALS, new Object());

		QueryByFilter qbf = new QueryByFilter(Roma.class);
		qbf.addProjection("bb");
		qbf.addOrder("bb");
		qbf.addItem("bb", QueryOperator.EQUALS, new Object());
		filter.addReverseItem(qbf, "rev");

		QueryByFilterItemGroup g = new QueryByFilterItemGroup(QueryByFilterItemGroup.PREDICATE_AND);
		g.addItem("aa", QueryOperator.EQUALS, new Object());

		QueryByFilter qbf1 = new QueryByFilter(Roma.class);
		qbf1.addProjection("cc");
		qbf1.addOrder("cc");
		qbf1.addItem("cc", QueryOperator.EQUALS, new Object());
		g.addReverseItem(qbf1, "rev");
		filter.addItem(g);

		Assert.assertEquals(3, filter.getItems().size());
		Assert.assertTrue((filter.getItems().get(2)) instanceof QueryByFilterItemGroup);
		QueryByFilter ff = ((QueryByFilterItemReverse) filter.getItems().get(1)).getQueryByFilter();
		Assert.assertEquals(2, ff.getItems().size());
		Assert.assertEquals("bb", ((QueryByFilterItemPredicate) ff.getItems().get(0)).getFieldName());
		Assert.assertEquals("cc", ((QueryByFilterItemPredicate) ff.getItems().get(1)).getFieldName());
		Assert.assertEquals(2, ff.getProjections().size());
		Assert.assertEquals("bb", ff.getProjections().get(0).getField());
		Assert.assertEquals("cc", ff.getProjections().get(1).getField());

		Assert.assertEquals(2, ff.getOrders().size());
		Assert.assertEquals("bb", ff.getOrders().get(0).getFieldName());
		Assert.assertEquals("cc", ff.getOrders().get(1).getFieldName());

	}

}
