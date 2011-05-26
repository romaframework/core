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
package org.romaframework.core.repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.romaframework.aspect.persistence.PersistenceAspect;
import org.romaframework.aspect.persistence.Query;
import org.romaframework.aspect.persistence.QueryByExample;
import org.romaframework.aspect.persistence.QueryByFilter;
import org.romaframework.aspect.persistence.QueryByFilterItem;
import org.romaframework.aspect.persistence.QueryByFilterItemGroup;
import org.romaframework.core.Roma;
import org.romaframework.core.factory.GenericFactory;
import org.romaframework.core.schema.SchemaClass;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.schema.SchemaHelper;

/**
 * GenericRepository implementation delegates to Persistence Aspect the execution of the generic ones.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 * 
 * @param <T>
 *          Concrete class to manage.
 */
@SuppressWarnings("unchecked")
public abstract class PersistenceAspectRepository<T> implements GenericRepository<T> {

	public T create(T object) {
		return create(Roma.context().persistence(), object);
	}

	public T create(PersistenceAspect db, T object) {
		return (T) db.createObject(object);
	}

	public T create(T object, byte iStrategy) {
		return (T) create(Roma.context().persistence(), object, iStrategy);
	}

	public T create(PersistenceAspect db, T object, byte iStrategy) {
		return (T) db.createObject(object, iStrategy);
	}

	public void delete(Object[] iObjects) {
		delete(Roma.context().persistence(), iObjects);
	}

	public void delete(PersistenceAspect db, Object[] iObjects) {
		db.deleteObjects(iObjects);
	}

	public void delete(T object) {
		delete(Roma.context().persistence(), object);
	}

	public void delete(PersistenceAspect db, T object) {
		db.deleteObject(object);
	}

	public List<T> findByCriteria(Query iCriteria) {
		return findByCriteria(Roma.context().persistence(), iCriteria);
	}

	public List<T> findByCriteria(PersistenceAspect db, Query iCriteria) {
		return db.query(iCriteria);
	}

	public List<T> findByExample(T iExample) {
		return findByExample(Roma.context().persistence(), iExample, null, null);
	}

	public List<T> findByExample(T iExample, Byte iStrategy, String iMode) {
		return findByExample(Roma.context().persistence(), iExample, iStrategy, iMode);
	}

	public List<T> findByExample(PersistenceAspect db, T iExample, Byte iStrategy, String iMode) {
		QueryByExample query = new QueryByExample(iExample);
		if (iStrategy != null) {
			query.setStrategy(iStrategy);
		}
		if (iMode != null) {
			query.setMode(iMode);
		}
		return db.query(query);
	}

	public T findFirstByCriteria(Query iCriteria) {
		return (T) findFirstByCriteria(Roma.context().persistence(), iCriteria);
	}

	public T findFirstByCriteria(PersistenceAspect db, Query iCriteria) {
		return (T) db.queryOne(iCriteria);
	}

	public T findFirstByExample(T iExample) {
		return (T) findFirstByExample(Roma.context().persistence(), iExample, null, null);
	}

	public T findFirstByExample(PersistenceAspect db, T iExample, Byte iStrategy, String iMode) {
		QueryByExample query = new QueryByExample(iExample);
		if (iStrategy != null) {
			query.setStrategy(iStrategy);
		}
		if (iMode != null) {
			query.setMode(iMode);
		}
		return (T) db.queryOne(query);
	}

	public List<T> getAll() {
		Class<? extends T> cl = (Class<? extends T>) SchemaHelper.getGenericClass(getClass().getGenericSuperclass());
		if (cl == null) {
			// TODO: throw an exception.
		}
		return getAll(Roma.context().persistence(), cl, PersistenceAspect.STRATEGY_DETACHING, null);
	}

	public List<T> getAll(Class<? extends T> iClass) {
		return getAll(Roma.context().persistence(), iClass, PersistenceAspect.STRATEGY_DETACHING, null);
	}

	public List<T> getAll(PersistenceAspect db, Class<? extends T> iClass, Byte iStrategy, String iMode) {
		iClass = getEntityClass(iClass);
		QueryByExample query = new QueryByExample(iClass);
		if (iStrategy != null) {
			query.setStrategy(iStrategy);
		}
		if (iMode != null) {
			query.setMode(iMode);
		}
		return db.query(query);
	}

	protected Class<? extends T> getEntityClass(Class<? extends T> iClass) {
		GenericFactory<T> factory = (GenericFactory<T>) Roma.factory(iClass);
		if (factory != null) {
			Class<? extends T> newClass = factory.getEntityClass();
			if (newClass != null) {
				iClass = newClass;
			}
		}
		return iClass;
	}

	protected Class<? extends T> getEntityClass() {
		Class<? extends T> clazz = (Class<? extends T>) SchemaHelper.getGenericClass(getClass().getGenericSuperclass());
		if (clazz != null) {
			return getEntityClass(clazz);
		}
		return null;
	}

	public List<T> getAll(PersistenceAspect db, Byte iStrategy, String iMode) {
		Class<? extends T> cl = (Class<? extends T>) SchemaHelper.getSuperclassGenericType(getClass()).getLanguageType();
		if (cl == null) {
			// TODO: throw an exception.
		}
		return getAll(db, cl, iStrategy, iMode);
	}

	public String getOID(Object object) {
		return Roma.context().persistence().getOID(object);
	}

	public T load(T entity, String fullModeLoading, byte strategyDetaching) {
		return load(Roma.context().persistence(), entity, fullModeLoading, strategyDetaching);
	}

	public T load(PersistenceAspect db, T entity, String fullModeLoading, byte strategyDetaching) {
		return db.loadObject(entity, fullModeLoading, strategyDetaching);
	}

	public T loadObjectByOID(Object OID, String iMode, byte iStrategy) {
		return (T) loadObjectByOID(Roma.context().persistence(), (String) OID, iMode, iStrategy);
	}

	public T loadObjectByOID(PersistenceAspect db, Object OID, String iMode, byte iStrategy) {
		return (T) db.loadObjectByOID((String) OID, iMode, iStrategy);
	}

	public T update(T object) {
		return (T) update(Roma.context().persistence(), object);
	}

	public T update(PersistenceAspect db, T object) {
		return (T) db.updateObject(object);
	}

	public T update(T object, byte iStrategy) {
		return (T) update(Roma.context().persistence(), object, iStrategy);
	}

	public T update(PersistenceAspect db, T object, byte iStrategy) {
		return (T) db.updateObject(object, iStrategy);
	}

	public List<T> search(String key) {
		return search(key, null, null, null);
	}

	public List<T> search(String key, String[] fields, String[] orders, QueryByFilterItem[] additionalFilters) {
		Class<?> cl = SchemaHelper.getGenericClass(getClass().getGenericSuperclass());
		SchemaClass sc = Roma.schema().getSchemaClass(cl);

		if (fields == null || fields.length == 0) {

			List<String> allowedFields = new ArrayList<String>();
			Iterator<SchemaField> fi = sc.getFieldIterator();
			while (fi.hasNext()) {
				SchemaField field = fi.next();
				if (Roma.context().persistence().isFieldPersistent(field)) {
					allowedFields.add(field.getName());
				}
			}
			fields = allowedFields.toArray(new String[0]);
		}

		QueryByFilter query = new QueryByFilter(cl);
		query.setStrategy(PersistenceAspect.STRATEGY_DETACHING);
		String searchFilter = key;
		if (searchFilter == null) {
			searchFilter = ""; // check
		}
		String[] splittato = searchFilter.split(" ");
		for (String token : splittato) {
			QueryByFilterItemGroup subfilter = new QueryByFilterItemGroup(QueryByFilter.PREDICATE_OR);
			for (String field : fields) {
				if (token.length() > 0) {
					subfilter.addItem(field, QueryByFilter.FIELD_LIKE, token + "*");
				}
			}
			if (!subfilter.getItems().isEmpty()) {
				query.addItem(subfilter);
			}
		}
		if (additionalFilters != null) {
			for (QueryByFilterItem item : additionalFilters) {
				query.addItem(item);
			}
		}
		if (orders != null) {
			for (String order : orders) {
				query.addOrder(order);
			}
		}
		query.setRangeFrom(0, 10);
		return findByCriteria(query);
	}
}
