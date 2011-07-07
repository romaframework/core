/*
 * Copyright 2006-2007 Giordano Maestro (giordano.maestro--at--assetdata.it)
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
package org.romaframework.aspect.logging;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.LogFactory;
import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.i18n.I18NHelper;
import org.romaframework.aspect.logging.feature.LoggingActionFeatures;
import org.romaframework.aspect.logging.feature.LoggingClassFeatures;
import org.romaframework.aspect.logging.feature.LoggingElementFeatures;
import org.romaframework.aspect.logging.feature.LoggingFieldFeatures;
import org.romaframework.aspect.session.SessionInfo;
import org.romaframework.core.Roma;
import org.romaframework.core.Utility;
import org.romaframework.core.exception.ConfigurationException;
import org.romaframework.core.schema.SchemaAction;
import org.romaframework.core.schema.SchemaClassDefinition;
import org.romaframework.core.schema.SchemaClassElement;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.schema.SchemaHelper;
import org.romaframework.core.util.parser.VariableParser;

public class LoggingHelper {

	private static final String	AFTER_FIELD_WRITE					= "[After Field Write]";
	private static final String	DEFAULT_EXCEPTION_MESSAGE	= LoggingConstants.DEFAULT_MESSAGE + "@{exception}";
	private static final String	CLOSE											= "]";
	private static final String	OPEN											= "[";
	private static final String	$													= "$";
	private static final String	BEFORE_METHOD_EXCECUTION	= "[Before method excecution]";
	private static final String	PRE												= "pre";
	private static final String	I18N											= "@{i18n}";
	private static final String	DATE											= "date";
	private static final String	USER											= "user";
	private static final String	WHERE											= "where";
	private static final String	WHO												= "who";
	private static final String	NOT_FINDED								= "notFinded";
	private static final String	EXCEPTION									= "exception";
	private static final String	EXCEPTION_WHO							= OPEN + EXCEPTION + CLOSE;
	private static final String	RETURNED									= "returned";
	private static final String	ME												= "me";
	private static final String	END												= "}";
	private static final String	BEGIN											= "@{";

	private static final String	END_											= "\\}";
	private static final String	BEGIN_										= "@\\{";

	/**
	 * Manage an after method invocation log message
	 * 
	 * @param content
	 *          the object where the action is invoked
	 * @param action
	 *          the action invoked
	 * @param returnedValue
	 *          the returned value of the invocation
	 */
	public static void managePostAction(Object content, SchemaClassElement action, Object returnedValue) {
		Boolean enabled = isEnabled((SchemaAction) action);

		if (!enabled) {
			return;
		}

		String post = (String) getPost(action);
		if (post == null || post.equals(AnnotationConstants.DEF_VALUE)) {
			return;
		} else if (post.equals(I18N)) {
			post = I18NHelper.getLabel(action, $ + action.getName(), "post");
		}

		Integer level = getLevel(action);
		String category = getCategory(action);
		String mode = (String) getMode(action);

		String where = OPEN + action.getEntity().getSchemaClass().getName() + "." + action.getName() + "] ";
		String who = "[After Method Excecution]";
		String messageToPrint = getMessageToPrint(post, content, returnedValue, null, who, where);
		Roma.aspect(LoggingAspect.class).log(level, category, mode, messageToPrint);

	}

	/**
	 * Replace the log special parameters (@{}) with the value
	 * 
	 * @param messageTemplate
	 *          the template of the message
	 * @param me
	 *          the object used by @{me} convention
	 * @param returnedValue
	 *          the object used by the @{returned} convention
	 * @param exception
	 *          the exception used by the @{exception} convention
	 * @param who
	 *          the type of event
	 * @param where
	 *          the object that generated the log
	 * @param args
	 * @return
	 */
	private static String getMessageToPrint(String messageTemplate, Object me, Object returnedValue, Throwable exception, String who, String where, Object... args) {
		if (messageTemplate == null) {
			return "";
		}

		VariablesListener listener = new VariablesListener();
		VariableParser.resolveVariables(messageTemplate, BEGIN, END, listener);
		List<String> resolvedVariables = listener.getVariables();
		for (String resolved : resolvedVariables) {
			if (resolved.startsWith(ME)) {
				messageTemplate = replaceMe(me, resolved, messageTemplate);
			} else if (resolved.startsWith(RETURNED)) {
				messageTemplate = replaceReturned(returnedValue, resolved, messageTemplate);
			} else if (resolved.startsWith(EXCEPTION)) {
				messageTemplate = replaceException(exception, resolved, messageTemplate);
			} else if (resolved.startsWith(WHO)) {
				messageTemplate = replaceWho(who, resolved, messageTemplate);
			} else if (resolved.startsWith(WHERE)) {
				messageTemplate = replaceWhere(where, resolved, messageTemplate);
			} else if (resolved.startsWith(USER)) {
				messageTemplate = replaceUser(resolved, messageTemplate);
			} else if (resolved.startsWith(DATE)) {
				messageTemplate = replaceDate(resolved, messageTemplate);
			}
		}
		return messageTemplate;
	}

	private static String replaceDate(String resolved, String messageTemplate) {
		String toReplace = BEGIN_ + resolved + END_;
		return messageTemplate.replaceAll(toReplace, new Date().toString());
	}

	private static String replaceUser(String resolved, String messageTemplate) {
		Object account = null;

		SessionInfo sess = Roma.session().getActiveSessionInfo();
		if (sess != null) {
			account = sess.getAccount();
		}

		if (account == null) {
			String toReplace = BEGIN_ + resolved + END_;
			return messageTemplate.replaceAll(toReplace, "No user connected");
		} else {
			String toReplace = BEGIN_ + resolved + END_;
			return messageTemplate.replaceAll(toReplace, account.toString());
		}
	}

	private static String replaceWho(String who, String resolved, String messageTemplate) {
		String toReplace = BEGIN_ + resolved + END_;
		return messageTemplate.replaceAll(toReplace, who);
	}

	private static String replaceWhere(String where, String resolved, String messageTemplate) {
		String toReplace = BEGIN_ + resolved + END_;
		return messageTemplate.replaceAll(toReplace, where);
	}

	private static String replaceMe(Object me, String resolved, String messageTemplate) {

		return replaceTemplate(me, resolved, messageTemplate, ME);

	}

	private static String replaceTemplate(Object me, String resolved, String messageTemplate, String prefix) {
		String toReplace = BEGIN_ + resolved + END_;
		if (me == null) {
			messageTemplate = messageTemplate.replaceAll(toReplace, "null");
		}

		Object result = null;
		try {
			if (resolved.equals(prefix)) {
				result = SchemaHelper.getFieldObject(me, "");
			} else {
				result = SchemaHelper.getFieldObject(me, resolved.substring(prefix.length() + 1) + ".");
			}

			if (result == null) {
				messageTemplate = messageTemplate.replaceAll(toReplace, "null");
			} else {
				messageTemplate = messageTemplate.replaceAll(toReplace, result.toString());
			}
		} catch (Exception e) {
			messageTemplate = messageTemplate.replaceAll(toReplace, NOT_FINDED);
		}
		return messageTemplate;
	}

	private static String replaceReturned(Object returnedValue, String resolved, String messageTemplate) {

		return replaceTemplate(returnedValue, resolved, messageTemplate, RETURNED);

	}

	private static String replaceException(Throwable exception, String resolved, String messageTemplate) {
		messageTemplate = replaceTemplate(exception, resolved, messageTemplate, EXCEPTION);
		return messageTemplate;
	}

	private static Object getPre(SchemaClassElement action) {
		Object feature = action.getFeature(LoggingActionFeatures.PRE);
		if (feature == null || feature.equals(AnnotationConstants.DEF_VALUE)) {
			return null;
		} else {
			return feature;
		}

	}

	private static Object getPost(SchemaClassElement action) {
		Object feature = action.getFeature(LoggingActionFeatures.POST);
		if (feature == null || feature.equals(AnnotationConstants.DEF_VALUE)) {
			return LoggingConstants.DEFAULT_MESSAGE;
		} else {
			return feature;
		}
	}

	private static Object getMode(SchemaClassElement action) {
		Object feature = action.getFeature(LoggingActionFeatures.MODE);
		if (feature == null || feature.equals(AnnotationConstants.DEF_VALUE)) {
			return getMode(action.getEntity());
		} else {
			return feature;
		}
	}

	private static Object getMode(SchemaClassDefinition action) {
		Object feature = action.getFeature(LoggingClassFeatures.MODE);
		if (feature == null || feature.equals(AnnotationConstants.DEF_VALUE)) {
			return LoggingConstants.MODE_CONSOLE;
		} else {
			return feature;
		}
	}

	private static String getCategory(SchemaClassElement action) {
		String feature = (String) action.getFeature(LoggingActionFeatures.CATEGORY);
		if (feature == null || feature.equals(AnnotationConstants.DEF_VALUE)) {
			return LoggingConstants.DEFAULT_CATEGORY;
		} else {
			return feature;
		}
	}

	private static Boolean isEnabled(SchemaClassElement action) {
		Boolean elementEnabled = (Boolean) action.getFeature(LoggingActionFeatures.ENABLED);
		if (elementEnabled == null) {
			return isEnabled(action.getEntity());
		} else {
			return elementEnabled;
		}
	}

	private static Boolean isEnabled(SchemaField action) {
		Boolean elementEnabled = (Boolean) action.getFeature(LoggingFieldFeatures.ENABLED);
		if (elementEnabled == null) {
			return isEnabled(action.getEntity());
		} else {
			return elementEnabled;
		}
	}

	private static Boolean isEnabled(SchemaClassDefinition iSchema) {
		Boolean classEnabled = iSchema.getFeature(LoggingClassFeatures.ENABLED);
		if (classEnabled == null) {
			return Boolean.FALSE;
		} else {
			return classEnabled;
		}
	}

	private static Integer getLevel(SchemaClassElement action) {
		Integer feature = (Integer) action.getFeature(LoggingElementFeatures.LEVEL);
		if (feature == null || feature.equals(Integer.MIN_VALUE)) {
			return LoggingConstants.LEVEL_INFO;
		} else {
			return feature;
		}
	}

	/**
	 * Manage a before method invocation log message
	 * 
	 * @param content
	 *          The object where the action is going to be invoked
	 * @param action
	 *          The action that is going to be invoked
	 */
	public static void managePreAction(Object content, SchemaAction action) {
		Boolean enabled = isEnabled((SchemaAction) action);
		if (!enabled) {
			return;
		}

		String pre = (String) getPre(action);
		if (pre == null || pre.equals(AnnotationConstants.DEF_VALUE)) {
			return;
		} else if (pre.equals(I18N)) {
			pre = I18NHelper.getLabel(action, $ + action.getName(), PRE);
		}

		Integer level = getLevel(action);

		String category = getCategory(action);
		String mode = (String) getMode(action);

		String who = BEFORE_METHOD_EXCECUTION;
		String where = OPEN + action.getEntity().getSchemaClass().getName() + "." + action.getName() + "] ";

		String messageToPrint = getMessageToPrint(pre, content, null, null, who, where);
		Roma.aspect(LoggingAspect.class).log(level, category, mode, messageToPrint);

	}

	/**
	 * Manage a message generated by an exception
	 * 
	 * @param content
	 *          The object that generated the exception
	 * @param action
	 *          the action or field that generate it
	 * @param e
	 *          The exception generated
	 */
	public static void manageException(Object content, SchemaClassElement action, Throwable e) {
		Boolean enabled = isEnabled(action);
		if (!enabled) {
			return;
		}

		String exception = getException(action);
		if (exception == null || exception.equals(AnnotationConstants.DEF_VALUE)) {
			return;
		} else if (exception.equals(I18N)) {
			exception = I18NHelper.getLabel(action, $ + action.getName(), EXCEPTION);
		}

		Integer level = getLevel(action);

		String category = getCategory(action);
		String mode = (String) getMode(action);

		String who = EXCEPTION_WHO;
		String where = OPEN + action.getEntity().getSchemaClass().getName() + "." + action.getName() + CLOSE;

		String messageToPrint = getMessageToPrint(exception, content, null, e, who, where);
		Roma.aspect(LoggingAspect.class).log(level, category, mode, messageToPrint);

	}

	private static String getException(SchemaClassElement action) {
		String feature = (String) action.getFeature(LoggingActionFeatures.EXCEPTION);
		if (feature == null || feature.equals(AnnotationConstants.DEF_VALUE)) {
			return DEFAULT_EXCEPTION_MESSAGE;
		} else {
			return feature;
		}
	}

	protected static Class<?>[] getExceptionsToLog(SchemaClassElement action) {
		Class<?>[] feature = (Class[]) action.getFeature(LoggingActionFeatures.EXCEPTIONS_TO_LOG);
		return feature;
	}

	/**
	 * Manage an after field write log message
	 * 
	 * @param field
	 *          The written field schemaField
	 * @param currentValue
	 *          the current value of the field
	 */
	public static void manageAfterFieldWrite(SchemaField field, Object currentValue) {
		Boolean enabled = isEnabled(field);

		if (!enabled) {
			return;
		}

		String post = (String) getPost(field);
		if (post == null || post.equals(AnnotationConstants.DEF_VALUE)) {
			return;
		}

		Integer level = getLevel(field);
		String category = getCategory(field);
		String mode = (String) getMode(field);

		String where = OPEN + field.getEntity().getSchemaClass().getName() + "." + field.getName() + CLOSE;
		String who = AFTER_FIELD_WRITE;
		String messageToPrint = getMessageToPrint(post, currentValue, null, null, who, where);
		Roma.aspect(LoggingAspect.class).log(level, category, mode, messageToPrint);
	}

	public static void raiseCfgException(Class<?> iClass, String msg) throws ConfigurationException {
		StringBuilder text = new StringBuilder();
		text.append("[").append(Utility.getClassName(iClass)).append("] ").append(msg);
		LogFactory.getLog(iClass).error(text.toString());
		throw new ConfigurationException(msg);
	}
}
