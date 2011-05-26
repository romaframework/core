package org.romaframework.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CollectionHelper {
	public static <T> List<T> arrayToList(T[] iArray) {
		List<T> result = new ArrayList<T>();
		for (T element : iArray) {
			result.add(element);
		}
		return result;
	}

	public static <T> void arrayToCollection(T[] a, Collection<T> c) {
		for (T o : a) {
			c.add(o);
		}
	}
}
