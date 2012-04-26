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

public class QueryByExample extends Query {
	public QueryByExample(Class<?> candidateClass) {
		this(candidateClass, null, null);
	}

	public QueryByExample(Object filter) {
		this(filter.getClass(), filter, null);
	}

	public QueryByExample(Class<?> candidateClass, Object filter) {
		this(candidateClass, filter, null);
	}

	public QueryByExample(Object filter, QueryByFilter iAdditionalFilter) {
		this(filter.getClass(), filter, iAdditionalFilter);
	}

	public QueryByExample(Class<?> candidateClass, Object filter, QueryByFilter iAdditionalFilter) {
		this.candidateClass = candidateClass;
		this.filter = filter;
		this.additionalFilter = iAdditionalFilter;
	}

	public QueryByFilter getAdditionalFilter() {
		return additionalFilter;
	}

	public Class<?> getCandidateClass() {
		return candidateClass;
	}

	public Object getFilter() {
		return filter;
	}

	private Class<?>			candidateClass;
	private Object				filter;
	private QueryByFilter	additionalFilter;

	public boolean hasProjection() {
		return false;
	}
}
