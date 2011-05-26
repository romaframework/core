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
	  * @param iAspectName
	  *          The aspect name of the feature to change
	  * @param iFieldName
	  *          The name of the field where the feature is changed
	  * @param iFeatureName
	  *          The name of the changed feature
	  * @param iOldValue
	  *          The old value of the feature
	  * @param iFeatureValue
	  *          The new value of the feature
	  */
	 public void signalChangeField(Object iUserObject, String iAspectName, String iFieldName, String iFeatureName, Object iOldValue,
			 Object iFeatureValue);

	 /**
	  * Notify the change of the features of an action
	  * 
	  * @param iUserObject
	  *          the object that contains the action
	  * @param iAspectName
	  *          The aspect name of the feature to change
	  * @param iActionName
	  *          The name of the action where the feature is changed
	  * @param iFeatureName
	  *          The name of the changed feature
	  * @param iOldValue
	  *          The old value of the feature
	  * @param iFeatureValue
	  *          The new value of the feature
	  */
	 public void signalChangeAction(Object iUserObject, String iAspectName, String iActionName, String iFeatureName, Object iOldValue,
			 Object iFeatureValue);

	 /**
	  * Notify the change of the features of a class
	  * 
	  * @param iUserObject
	  *          the object that contains the action
	  * @param iAspectName
	  *          The aspect name of the feature to change
	  * 
	  * @param iFeatureName
	  *          The name of the changed feature
	  * @param iOldValue
	  *          The old value of the feature
	  * @param iFeatureValue
	  *          The new value of the feature
	  */
	 public void signalChangeClass(Object iUserObject, String iAspectName, String iFeatureName, Object iOldValue, Object iFeatureValue);
 }
