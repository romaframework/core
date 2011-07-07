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
package org.romaframework.aspect.validation;

/**
 * Interface for custom validation. User classes can implement this interface defining custom validation rules.
 * 
 * Before to call the validate method, the framework will bind data in the object to allow to the method to read user input data.
 * 
 * If sub-classes also define the validate method, please assure to call super.validate() as first one call to avoid loss of
 * sub-class validation rules.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public interface CustomValidation {
	/**
	 * Unique method to implement to compute validation.
	 * 
	 * @throws ValidationException
	 *           Exception initialized with fieldName and RuleName. Framework will find the rule name message in the I18N file
	 */
	public void validate() throws ValidationException;
}
