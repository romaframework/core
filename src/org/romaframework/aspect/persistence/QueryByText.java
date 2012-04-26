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

import java.util.Map;

public class QueryByText extends Query {
	private Class<?>		candidateClass;
	private String			text;
	private String			order;
	Map<String, Object>	parameters;

	public QueryByText(String text) {
		this(null, text);
	}

	public QueryByText(Class<?> candidateClass, String text) {
		this(candidateClass, text, -1, -1, null);
	}

	public QueryByText(String text, Map<String, Object> iParameters) {
		this(null, text, -1, -1, iParameters);
	}

	public QueryByText(Class<?> candidateClass, String text, Map<String, Object> iParameters) {
		this(candidateClass, text, -1, -1, iParameters);
	}

	public QueryByText(Class<?> candidateClass, String text, int iRangeFrom, int iRangeTo) {
		this(candidateClass, text, iRangeFrom, iRangeTo, null);
	}

	public QueryByText(Class<?> candidateClass, String text, int iRangeFrom, int iRangeTo, Map<String, Object> iParameters) {
		setRangeFrom(iRangeFrom, iRangeTo);
		this.parameters = iParameters;
		this.candidateClass = candidateClass;
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Class<?> getCandidateClass() {
		return candidateClass;
	}

	public void setCandidateClass(Class<?> candidateClass) {
		this.candidateClass = candidateClass;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public Map<String, Object> getParameters() {
		return parameters;
	}

	public void setParameters(Map<String, Object> parameters) {
		this.parameters = parameters;
	}

	public boolean hasProjection() {
		return false;
	}

}
