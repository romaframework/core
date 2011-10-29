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

package org.romaframework.aspect.persistence;

import java.util.List;

import org.romaframework.core.aspect.Aspect;
import org.romaframework.core.schema.SchemaField;

/**
 * Persistence aspect. Manages application objects in datastore. It's a generic implementation of Repository concept in DDD.
 * Repositories should use this class to make interactions with real repositories.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public interface PersistenceAspect extends Aspect {

	public static final String	ASPECT_NAME						= "persistence";

	/**
	 * Define the full mode when to load full object properties.
	 */
	public static final String	DEFAULT_MODE_LOADING	= "default";
	public static final String	FULL_MODE_LOADING			= "full";

	public static final byte		STRATEGY_DEFAULT			= 0;
	public static final byte		STRATEGY_STANDARD			= 1;
	public static final byte		STRATEGY_DETACHING		= 2;
	public static final byte		STRATEGY_TRANSIENT		= 3;

	public static final byte		TX_PESSIMISTIC				= 0;
	public static final byte		TX_OPTISTIC						= 1;

	/**
	 * Return the persistent OID of the object passed.
	 * 
	 * @param iObject
	 *          Object to know the OID
	 * @return The String representation of OID
	 * @throws PersistenceException
	 *           If the object is not persistent.
	 */
	public String getOID(Object iObject) throws PersistenceException;

	/**
	 * Load an object from repository using the mode passed as argument.
	 * 
	 * @param iObject
	 *          The object to reload
	 * @param iMode
	 *          FULL_MODE_LOADING or custom modes.
	 * @return The object reloaded
	 * @throws PersistenceException
	 */
	public <T> T loadObject(T iObject, String iMode) throws PersistenceException;

	/**
	 * Load an object from repository using a strategy different by the PersistenceAspect's default.
	 * 
	 * @param iObject
	 *          The object to reload
	 * @param iMode
	 *          FULL_MODE_LOADING or custom modes.
	 * @param iStrategy
	 *          STRATEGY_DEFAULT, STRATEGY_STANDARD, STRATEGY_DETACHING or STRATEGY_TRANSIENT
	 * @return The object reloaded
	 * @throws PersistenceException
	 */
	public <T> T loadObject(T iObject, String iMode, byte iStrategy) throws PersistenceException;

	/**
	 * Load an object from repository by its OID using the mode passed as argument.
	 * 
	 * @param iOID
	 *          The object OID
	 * @param iMode
	 *          FULL_MODE_LOADING or custom modes.
	 * @return The object loaded
	 * @throws PersistenceException
	 */
	public <T> T loadObjectByOID(String iOID, String iMode) throws PersistenceException;

	/**
	 * Load an object from repository by its OID using a strategy different by the PersistenceAspect's default.
	 * 
	 * @param iOID
	 *          The object OID
	 * @param iMode
	 *          FULL_MODE_LOADING or custom modes.
	 * @param iStrategy
	 *          STRATEGY_DEFAULT, STRATEGY_STANDARD, STRATEGY_DETACHING or STRATEGY_TRANSIENT
	 * @return The object loaded
	 * @throws PersistenceException
	 */
	public <T> T loadObjectByOID(String iOID, String iMode, byte iStrategy) throws PersistenceException;

	/**
	 * Create an object in the repository. Prior of this call the object exists as transient.
	 * 
	 * @param iObject
	 *          The object to make persistent
	 * @return The persistent object (or the same one passed in some implementations)
	 * @throws PersistenceException
	 */
	public <T> T createObject(T iObject) throws PersistenceException;

	/**
	 * Create an object in the repository. Prior of this call the object exists as transient. It use a strategy different by the
	 * PersistenceAspect's default.
	 * 
	 * @param iObject
	 *          The object to make persistent
	 * @param iStrategy
	 *          STRATEGY_DEFAULT, STRATEGY_STANDARD, STRATEGY_DETACHING or STRATEGY_TRANSIENT
	 * @return The persistent object (or the same one passed in some implementations)
	 * @throws PersistenceException
	 */
	public <T> T createObject(T iObject, byte iStrategy) throws PersistenceException;

	/**
	 * Update a persistent object in the repository.
	 * 
	 * @param iObject
	 *          The object to update persistently
	 * @return The persistent object (or the same one passed in some implementations)
	 * @throws PersistenceException
	 */
	public <T> T updateObject(T iObject) throws PersistenceException;

	/**
	 * Update a persistent object in the repository using a strategy different by the PersistenceAspect's default.
	 * 
	 * @param iObject
	 *          The object to update persistently
	 * @param iStrategy
	 *          STRATEGY_DEFAULT, STRATEGY_STANDARD, STRATEGY_DETACHING or STRATEGY_TRANSIENT
	 * @return The persistent object (or the same one passed in some implementations)
	 * @throws PersistenceException
	 */
	public <T> T updateObject(T iObject, byte iStrategy) throws PersistenceException;

	/**
	 * Update more persistent objects in one shot.
	 * 
	 * @param iObjects
	 *          The object array to update persistently
	 * @throws PersistenceException
	 */
	public Object[] updateObjects(Object[] iObjects) throws PersistenceException;

	/**
	 * Update more persistent objects in one shot using a strategy different by the PersistenceAspect's default.
	 * 
	 * @param iObjects
	 *          The object array to update persistently
	 * @param iStrategy
	 *          STRATEGY_DEFAULT, STRATEGY_STANDARD, STRATEGY_DETACHING or STRATEGY_TRANSIENT
	 * @throws PersistenceException
	 */
	public Object[] updateObjects(Object[] iObjects, byte iStrategy) throws PersistenceException;

	/**
	 * Delete a persistent object in the repository.
	 * 
	 * @param iObject
	 *          The object to delete persistently
	 * @throws PersistenceException
	 */
	public void deleteObject(Object iObject) throws PersistenceException;

	/**
	 * Delete more persistent objects in one shot.
	 * 
	 * @param iObjects
	 *          The object array to delete persistently
	 * @throws PersistenceException
	 */
	public void deleteObjects(Object[] iObjects) throws PersistenceException;

	/**
	 * Execute a query against the repository.
	 * 
	 * @param iQuery
	 *          An implementation of the Query interface: QueryByFilter, QueryByExample, QueryByText or others.
	 * @return The List of objects returned as query result.
	 * @throws PersistenceException
	 */
	public <T> List<T> query(Query iQuery) throws PersistenceException;

	/**
	 * Execute a query against the repository and get the first element returned
	 * 
	 * @param iQuery
	 *          An implementation of the Query interface: QueryByFilter, QueryByExample, QueryByText or others.
	 * @return The first one object returned as query result.
	 * @throws PersistenceException
	 */
	public <T> T queryOne(Query iQuery) throws PersistenceException;

	/**
	 * Execute a count query against the repository and get return the count of elements that match query conditions.
	 * 
	 * @param iQuery
	 *          An implementation of the Query interface: QueryByFilter, QueryByExample, QueryByText or others.
	 * @return The count of element that match query conditions.
	 * @throws PersistenceException
	 */
	public long queryCount(Query iQuery) throws PersistenceException;

	/**
	 * Tell if the object is locally modified. Many implementation call the object "dirty" in this case.
	 * 
	 * @param iObject
	 *          The object to check
	 * @return true if is modified, otherwise false
	 * @throws PersistenceException
	 */
	public boolean isObjectLocallyModified(Object iObject) throws PersistenceException;

	/**
	 * Tell if the object is persistent or not.
	 * 
	 * @param iObject
	 *          The object to check
	 * @return true if is persistent, otherwise false
	 * @throws PersistenceException
	 */
	public boolean isObjectPersistent(Object iObject) throws PersistenceException;

	/**
	 * Tell if the class is persistent or not.
	 * 
	 * @param iClass
	 *          The class to check
	 * @return true if iClass is persistent, false otherwise
	 */
	public boolean isClassPersistent(Class<?> iClass);

	/**
	 * Tell if the field of the specified class is persistent or not
	 * 
	 * @param iClass
	 *          The class containing the field
	 * @param iFieldName
	 *          The field to check
	 * @return true if the field is persistent, false otherwise
	 */
	public boolean isFieldPersistent(Class<?> iClass, String iFieldName);

	/**
	 * Tell if the field of the specified class is persistent or not
	 * 
	 * @param iField
	 *          The field to check
	 * 
	 * @return true if the field is persistent, false otherwise
	 */
	public boolean isFieldPersistent(SchemaField iField);

	/**
	 * Set a field as dirty
	 * 
	 * @param iObject
	 *          Object where the field is dirty
	 * @param iFieldName
	 *          Field name
	 */
	public void setObjectDirty(Object iObject, String iFieldName);

	/**
	 * Return the current strategy used.
	 * 
	 * @return current strategy used between STRATEGY_DEFAULT, STRATEGY_STANDARD, STRATEGY_DETACHING and STRATEGY_TRANSIENT
	 */
	public byte getStrategy();

	/**
	 * Returns the transaction strategy used: pessimistic (default) or optimistic.
	 * 
	 * @return TX_PESSIMISTIC or TX_OPTIMISTIC
	 */
	public byte getTxMode();

	/**
	 * Change the transaction mode.
	 * 
	 * @param txMode
	 *          TX_PESSIMISTIC or TX_OPTIMISTIC
	 */
	public void setTxMode(byte txMode);

	/**
	 * Tell if the transaction is currently active.
	 * 
	 * @return true if is active, otherwise false
	 */
	public boolean isActive();

	/**
	 * Commit the transaction making all changes persistent. The behavior changes between implementations.
	 */
	public void commit();

	/**
	 * Rollback the transaction (if any) and revert all local changes. The behavior changes between implementations.
	 */
	public void rollback();

	/**
	 * Close the current transaction (if any). The behavior changes between implementations.
	 */
	public void close();
}
