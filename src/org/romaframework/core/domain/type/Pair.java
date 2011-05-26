/*
 * Copyright 2006-2007 Luca Garulli (luca.garulli--at--assetdata.it)
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
package org.romaframework.core.domain.type;

import java.util.Map;
import java.util.Map.Entry;

import org.romaframework.aspect.core.annotation.CoreClass;

/**
 * Utility class to handle a pair of values.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 * 
 * @param <K>
 *          key type
 * @param <V>
 *          value type
 */
@CoreClass(orderFields = "key value")
public class Pair<K, V> implements Map.Entry<K, V> {
	protected K	key;
	protected V	value;

	public Pair() {
	}

	public Pair(K iKey, V iValue) {
		key = iKey;
		value = iValue;
	}

	public Pair(Entry<K, V> iEntry) {
		key = iEntry.getKey();
		value = iEntry.getValue();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Pair<?,?>))
			return false;

		Pair<?,?> other = (Pair<?,?>) obj;
		if (key != null && value != null && other.getKey() != null && other.getValue() != null)
			if (key.equals(other.getKey()) && value.equals(other.getValue()))
				return true;
		return false;
	}

	@Override
	public int hashCode() {
		return key != null ? key.hashCode() : -1;
	}

	@Override
	public String toString() {
		return (key != null ? key.toString() : "") + "=" + (value != null ? value.toString() : "");
	}

	public K getKey() {
		return key;
	}

	public V getValue() {
		return value;
	}

	public void setKey(K iKey) {
		key = iKey;
	}

	public V setValue(V iValue) {
		value = iValue;
		return value;
	}
}
