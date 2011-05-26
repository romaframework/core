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

import java.util.Iterator;

import org.romaframework.aspect.session.SessionInfo;
import org.romaframework.aspect.validation.feature.ValidationActionFeatures;
import org.romaframework.aspect.validation.feature.ValidationFieldFeatures;
import org.romaframework.core.Roma;
import org.romaframework.core.flow.Controller;
import org.romaframework.core.flow.UserObjectEventListener;
import org.romaframework.core.handler.RomaObjectHandler;
import org.romaframework.core.schema.SchemaClassDefinition;
import org.romaframework.core.schema.SchemaClassElement;
import org.romaframework.core.schema.SchemaElement;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.schema.SchemaHelper;

public class BasicValidationModule extends ValidationAspectAbstract implements UserObjectEventListener {

	public BasicValidationModule() {
		Controller.getInstance().registerListener(UserObjectEventListener.class, this);
	}

	public void validate(Object iObject) {
		MultiValidationException exc = validateAndCollectExceptions(iObject);
		if (exc != null && !exc.isEmpty())
			throw exc;
	}

	/**
	 * Validate any pojo.
	 * 
	 * @param pojo
	 * @return a MultiValidationException object with the sum of exceptions, if any
	 */
	public MultiValidationException validateAndCollectExceptions(Object pojo) {
		return validateAndCollectExceptions(Roma.getHandler(pojo), pojo);
	}

	/**
	 * Validate a form.
	 * 
	 * @param form
	 *          RomaObjectHandler instance containing the field where the pojo is rendered
	 * @return a MultiValidationException object with the sum of exceptions, if any
	 */
	public MultiValidationException validateAndCollectExceptions(RomaObjectHandler form) {
		return validateAndCollectExceptions(form, form.getContent());
	}

	/**
	 * Validate a form with a pojo as content.
	 * 
	 * @param form
	 *          RomaObjectHandler instance containing the field where the pojo is rendered
	 * @param pojo
	 *          Content pojo
	 * @return a MultiValidationException object with the sum of exceptions, if any
	 */
	public MultiValidationException validateAndCollectExceptions(RomaObjectHandler form, Object pojo) throws ValidationException,
			MultiValidationException {
		MultiValidationException multiException = new MultiValidationException();
		validate(form, pojo, Roma.schema().getSchemaClass(pojo), multiException);
		return multiException;
	}

	protected void validate(RomaObjectHandler form, Object pojo, SchemaClassDefinition schemaObject,
			MultiValidationException multiException) {
		if (pojo == null)
			return;

		SchemaClassDefinition schemaInstance = form != null ? form.getSchemaObject() : schemaObject;

		if (schemaInstance != null)
			validateFields(form, pojo, multiException, schemaInstance);
		else {
			if (SchemaHelper.isMultiValueObject(pojo)) {
				// VALIDATE EVERY SINGLE ITEM OF COLLECTION/ARRAY/MAP
				for (Object o : SchemaHelper.getObjectArrayForMultiValueObject(pojo)) {
					validate(form, o, schemaInstance, multiException);
				}
			}
		}

		if (pojo != null && CustomValidation.class.isAssignableFrom(pojo.getClass())) {
			try {
				Roma.context().create();
				// CALL CUSTOM VALIDATION ROUTINE
				((CustomValidation) pojo).validate();
			} catch (MultiValidationException me) {
				// HANDLE MULTI VALIDATION EXCEPTION
				for (Iterator<ValidationException> it = me.getDetailIterator(); it.hasNext();) {
					ValidationException ve = it.next();
					Object fieldComponent = null;
					if (form != null)
						fieldComponent = form.getFieldComponent(ve.getFieldName());
					handleValidationException(ve.getObject(), fieldComponent, multiException, ve.getFieldName(), ve.getMessage(),
							ve.getRefValue());
				}
			} catch (ValidationException ve) {
				// HANDLE SINGLE VALIDATION EXCEPTION
				Object fieldComponent = null;
				if (form != null)
					fieldComponent = form.getFieldComponent(ve.getFieldName());
				handleValidationException(ve.getObject(), fieldComponent, multiException, ve.getFieldName(), ve.getMessage(),
						ve.getRefValue());
			} catch (Exception ex) {
				handleValidationException(pojo, null, multiException, null, ex.toString(), null);
			} finally {
				Roma.context().destroy();
			}
		}
	}

	protected void validateFields(RomaObjectHandler form, Object pojo, MultiValidationException multiException,
			SchemaClassDefinition schemaInstance) {
		SchemaField fieldInfo;

		for (Iterator<SchemaField> it = schemaInstance.getFieldIterator(); it.hasNext();) {
			// SET FIELD'S COMPONENT
			fieldInfo = it.next();

			if (!(Boolean) fieldInfo.getFeature(ValidationAspect.ASPECT_NAME, ValidationFieldFeatures.ENABLED))
				continue;

			Object fieldComponent = null;
			if (form != null)
				fieldComponent = form.getFieldComponent(fieldInfo.getName());

			Object fieldValue = SchemaHelper.getFieldValue(fieldInfo, pojo);

			if (SchemaHelper.isMultiValueObject(fieldValue)) {
				validateFieldComponent(pojo, multiException, fieldInfo, fieldComponent, fieldValue);

				// VALIDATE EVERY SINGLE ITEM OF COLLECTION/ARRAY/MAP
				for (Object o : SchemaHelper.getObjectArrayForMultiValueObject(fieldValue)) {
					validate(Roma.getHandler(o), o, Roma.schema().getSchemaClass(o), multiException);
				}
			} else if (fieldValue != null && !SchemaHelper.isJavaType(fieldValue.getClass())) {

				if (fieldComponent != null && fieldComponent instanceof RomaObjectHandler) {
					// EMBEDDED FORM: CALL ITS VALIDATION
					if (fieldValue instanceof CustomValidation) {
						validate(Roma.getHandler(fieldValue), fieldValue, Roma.schema().getSchemaClass(fieldValue), multiException);
					} else {
						validateFields((RomaObjectHandler) fieldComponent, fieldValue, multiException, Roma.schema().getSchemaClass(fieldValue));
					}
				} else
					// EMBEDDED OBJECT: CALL ITS VALIDATION
					validate(Roma.getHandler(fieldValue), fieldValue, Roma.schema().getSchemaClass(fieldValue), multiException);

			} else
				validateFieldComponent(pojo, multiException, fieldInfo, fieldComponent, fieldValue);
		}
	}

	private void validateFieldComponent(Object pojo, MultiValidationException multiException, SchemaField fieldInfo,
			Object fieldComponent, Object fieldValue) {

		String name = fieldInfo.getName();

		final boolean required = (Boolean) fieldInfo.getFeature(ValidationAspect.ASPECT_NAME, ValidationFieldFeatures.REQUIRED);
		final Integer annotationMin = (Integer) fieldInfo.getFeature(ValidationAspect.ASPECT_NAME, ValidationFieldFeatures.MIN);
		final Integer annotationMax = (Integer) fieldInfo.getFeature(ValidationAspect.ASPECT_NAME, ValidationFieldFeatures.MAX);
		Class<?> fieldClass = (Class<?>) fieldInfo.getClassInfo().getLanguageType();

		if (String.class.isAssignableFrom(fieldClass))
			validateString(pojo, fieldComponent, multiException, fieldInfo, (String) fieldValue, name, required, annotationMin,
					annotationMax);
		else if (Number.class.isAssignableFrom(fieldClass))
			validateNumber(pojo, fieldComponent, multiException, (Number) fieldValue, name, required, annotationMin, annotationMax);
		else if (SchemaHelper.isMultiValueObject(fieldValue))
			validateMultiValue(pojo, fieldComponent, multiException, fieldValue, name, required, annotationMin, annotationMax,
					SchemaHelper.getSizeForMultiValueObject(fieldValue), fieldInfo);
	}

	public void validateNumber(Object pojo, Object iFieldComponent, MultiValidationException iMultiException, Number fieldValue,
			String name, boolean required, Integer annotationMin, Integer annotationMax) {
		if (required && fieldValue == null)
			handleValidationException(pojo, iFieldComponent, iMultiException, name, "$validation.required", null);

		if (annotationMin != null && fieldValue != null && fieldValue.intValue() < annotationMin)
			handleValidationException(pojo, iFieldComponent, iMultiException, name, "$validation.minLength",
					String.valueOf(annotationMin));

		if (annotationMax != null && fieldValue != null && fieldValue.intValue() > annotationMax)
			handleValidationException(pojo, iFieldComponent, iMultiException, name, "$validation.maxLength",
					String.valueOf(annotationMax));
	}

	public void validateMultiValue(Object pojo, Object iFieldComponent, MultiValidationException iMultiException, Object fieldValue,
			String name, boolean required, Integer annotationMin, Integer annotationMax, int length, SchemaElement iElement) {
		if (required) {
			// TODO: RESOLVE THIS DEPENDENCY IN MORE FAIR WAY
			final String selectionField = (String) iElement.getFeature("view", "selectionField");

			// CHECK IF THE SELECTION IS NULL
			if (fieldValue == null || selectionField == null || SchemaHelper.getFieldValue(pojo, selectionField) == null)
				handleValidationException(pojo, iFieldComponent, iMultiException, name, "$validation.required", null);
		}

		if (annotationMin != null && length < annotationMin)
			handleValidationException(pojo, iFieldComponent, iMultiException, name, "$validation.minLength",
					String.valueOf(annotationMin));

		if (annotationMax != null && length > annotationMax)
			handleValidationException(pojo, iFieldComponent, iMultiException, name, "$validation.maxLength",
					String.valueOf(annotationMax));
	}

	public void validateString(Object pojo, Object iFieldComponent, MultiValidationException iMultiException, SchemaField fieldInfo,
			Object fieldValue, String name, boolean required, Integer annotationMin, Integer annotationMax) {
		String stringValue = (String) fieldValue;
		if (required && (stringValue == null || stringValue.length() == 0))
			handleValidationException(pojo, iFieldComponent, iMultiException, name, "$validation.required", null);

		if (annotationMin != null && (stringValue == null || stringValue.length() < annotationMin))
			handleValidationException(pojo, iFieldComponent, iMultiException, name, "$validation.minLength",
					String.valueOf(annotationMin));

		if (annotationMax != null && stringValue != null && stringValue.length() > annotationMax)
			handleValidationException(pojo, iFieldComponent, iMultiException, name, "$validation.maxLength",
					String.valueOf(annotationMax));
	}

	protected void handleValidationException(Object pojo, Object iComponent, MultiValidationException multiException,
			String iFieldName, String iRule, String iRefValue) throws ValidationException {
		ValidationException e = new ValidationException(pojo, iFieldName, iRule, iRefValue);
		multiException.addException(e);
		if (iComponent != null)
			e.setComponent(iComponent);
	}

	public boolean onBeforeActionExecution(Object content, SchemaClassElement action) {
		// INVOKE THE ACTION
		Boolean val = (Boolean) action.getFeature(ASPECT_NAME, ValidationActionFeatures.ENABLED);
		if (val != null && val)
			validate(content);

		return true;
	}

	public int getPriority() {
		return 0;
	}

	public void onAfterActionExecution(Object content, SchemaClassElement action, Object returnedValue) {
	}

	public Object onAfterFieldRead(Object content, SchemaField field, Object currentValue) {
		return currentValue;
	}

	public Object onAfterFieldWrite(Object content, SchemaField field, Object currentValue) {
		return currentValue;
	}

	public Object onBeforeFieldRead(Object content, SchemaField field, Object currentValue) {
		return IGNORED;
	}

	public Object onBeforeFieldWrite(Object content, SchemaField field, Object currentValue) {
		return currentValue;
	}

	public Object onException(Object content, SchemaClassElement element, Throwable throwed) {
		return null;
	}

	public void onFieldRefresh(SessionInfo session, Object content, SchemaField field) {
	}
}
