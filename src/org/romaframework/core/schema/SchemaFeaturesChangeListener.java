/*
 * Copyright 20062-007 Luca Garulli (luca.garulli--at--assetdata.it)
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
 */package org.romaframework.core.schema;

/**
 * Interface to track changes for schema class, field and action.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 * 
 */
public interface SchemaFeaturesChangeListener {
	/**
	 * Notify the change of the features of a field
	 * 
	 * @param iUserObject
	 *          the object that contains the field
	 * @param iFieldName
	 *          The name of the field where the feature is changed
	 * @param iFeature
	 *          The name of the changed feature
	 * @param iOldValue
	 *          The old value of the feature
	 * @param iFeatureValue
	 *          The new value of the feature
	 */
	public <T> void signalChangeField(Object iUserObject, String iFieldName, Feature<T> iFeature, T iOldValue, T iFeatureValue);

	/**
	 * Notify the change of the features of an action
	 * 
	 * @param iUserObject
	 *          the object that contains the action
	 * @param iActionName
	 *          The name of the action where the feature is changed
	 * @param iFeature
	 *          The name of the changed feature
	 * @param iOldValue
	 *          The old value of the feature
	 * @param iFeatureValue
	 *          The new value of the feature
	 */
	public <T> void signalChangeAction(Object iUserObject, String iActionName, Feature<T> iFeature, T iOldValue, T iFeatureValue);

	/**
	 * Notify the change of the features of a class
	 * 
	 * @param iUserObject
	 *          the object that contains the action
	 * @param iFeature
	 *          The name of the changed feature
	 * @param iOldValue
	 *          The old value of the feature
	 * @param iFeatureValue
	 *          The new value of the feature
	 */
	public <T> void signalChangeClass(Object iUserObject, Feature<T> iFeature, T iOldValue, T iFeatureValue);
}
