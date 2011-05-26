package org.romaframework.core.repository;

import java.util.List;

import org.romaframework.aspect.persistence.Query;

@SuppressWarnings("unchecked")
public abstract class GenericRepositoryAbstract<T> implements GenericRepository<T> {

	public T create(T iObject) {
		return null;
	}

	public void delete(T iObject) {
	}

	public T create(T iObject, byte iStrategy) {
		return create(iObject);
	}

	public void delete(Object[] array) {
		if (array != null)
			for (Object o : array)
				delete((T) o);
	}

	public List<T> findByCriteria(Query iCriteria) {
		return null;
	}

	public List<T> findByExample(T iExample) {
		return null;
	}

	public T findFirstByCriteria(Query iCriteria) {
		return null;
	}

	public String getOID(Object iObject) {
		return iObject != null ? iObject.toString() : null;
	}

	public T load(T iEntity, String iFullModeLoading, byte iStrategyDetaching) {
		return null;
	}

	public T loadObjectByOID(Object OID, String iMode, byte iStrategy) {
		return null;
	}

	public T update(T iObject) {
		return null;
	}

	public T update(T iObject, byte iStrategy) {
		return update(iObject);
	}
}
