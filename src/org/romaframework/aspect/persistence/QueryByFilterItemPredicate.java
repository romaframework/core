/*
 * Copyright 2006 Luca Garulli (luca.garulli--at--assetdata.it)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.romaframework.aspect.persistence;

public class QueryByFilterItemPredicate implements QueryByFilterItem {
	private String	fieldName;
	private String	fieldOperator;
	private Object	fieldValue;

	// @SpringConstructor(constructorParamsGetters = { "getFieldName", "getFieldOperator", "getFieldValue" })
	public QueryByFilterItemPredicate(String fieldName, String operator, Object value) {
		this.fieldName = fieldName;
		this.fieldOperator = operator;
		this.fieldValue = value;
	}

	public String getFieldName() {
		return fieldName;
	}

	public String getFieldOperator() {
		return fieldOperator;
	}

	public Object getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(Object fieldValue) {
		this.fieldValue = fieldValue;
	}

	@Override
	public String toString() {
		if (fieldName == null)
			return "";
		StringBuilder buffer = new StringBuilder();

		buffer.append(fieldName);
		buffer.append(' ');
		buffer.append(fieldOperator);
		buffer.append(' ');
		buffer.append(fieldValue);

		return buffer.toString();
	}
}
