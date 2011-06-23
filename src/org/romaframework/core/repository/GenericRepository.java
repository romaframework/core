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

import java.util.List;

import org.romaframework.aspect.persistence.Query;

/**
 * Generic Repository interface. Repository is a fundamental part of DDD. <br/>
 * Each Repository had to implements this interface in order to be used by Roma components in transparent way.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 * 
 * @param <T>
 *          Concrete class to manage.
 */
public interface GenericRepository<T> {
	public static final String	DEF_SUFFIX	= "Repository";

	public T create(T iObject);

	public T create(T iObject, byte iStrategy);

	public void delete(Object[] array);

	public void delete(T iObject);

	public long countByCriteria(Query iCriteria);

	public List<T> findByCriteria(Query iCriteria);

	public List<T> findByExample(T iExample);

	public T findFirstByCriteria(Query iCriteria);

	public T loadObjectByOID(Object OID, String iMode, byte iStrategy);

	public String getOID(Object iObject);

	public T load(T iEntity, String iFullModeLoading, byte iStrategyDetaching);

	public T update(T iObject);

	public T update(T iObject, byte iStrategy);

	public List<T> search(String key);

	public List<T> getAll();
}
