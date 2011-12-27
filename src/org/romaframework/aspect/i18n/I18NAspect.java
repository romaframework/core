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

package org.romaframework.aspect.i18n;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Set;

import org.romaframework.core.aspect.Aspect;
import org.romaframework.core.schema.SchemaClassDefinition;

/**
 * I18N Aspect behavior interface.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public interface I18NAspect extends Aspect {
	public static final String	ASPECT_NAME				= "i18n";
	public static final String	VARNAME_PREFIX		= "$";
	public static final String	CONTEXT_SEPARATOR	= ".";
	public static final String	DEFAULT_VALUE_KEY	= "$content";

	public enum Type {
		LABEL, HINT, FORMAT, CONFIRM, EXCEPTION,CONTENT
	}

	/**
	 * Retrieve the available locale for current.
	 * 
	 * @return
	 */
	public Set<Locale> getAvailableLanguages();

	/**
	 * Retrieve the date format for current locale.
	 * 
	 * @return the date format.
	 */
	public DateFormat getDateFormat();

	/**
	 * Retrieve the date time format for current locale.
	 * 
	 * @return the format date time format.
	 */
	public DateFormat getDateTimeFormat();

	/**
	 * Retrieve the date time format for specified locale.
	 * 
	 * @param iLocale
	 *          the locale used to find format.
	 * @return format of date time with specified locale.
	 */
	public DateFormat getDateTimeFormat(Locale iLocale);

	/**
	 * Retrieve the time format for default locale.
	 * 
	 * @return the the time format.
	 */
	public DateFormat getTimeFormat();

	/**
	 * Retrieve the time format for specified locale.
	 * 
	 * @return the the time format.
	 */

	public DateFormat getTimeFormat(Locale iLocale);

	/**
	 * Retrieve the date format for specified locale.
	 * 
	 * @return the date format.
	 */
	public DateFormat getDateFormat(Locale iLocale);

	/**
	 * Retrieve the number format for current locale.
	 * 
	 * @return the number format.
	 */
	public NumberFormat getNumberFormat();

	/**
	 * Retrieve the number format for specified locale.
	 * 
	 * @return the number format.
	 */
	public NumberFormat getNumberFormat(Locale iLocale);

	/**
	 * Resolve the string with specified key.
	 * 
	 * @param string
	 *          the string to find.
	 * @param iArgs
	 *          arguments for fill resolved strings.
	 * @return resolved string.
	 */
	public String get(String string, Object... iArgs);

	/**
	 * Retrieve a string from i18n using current locale for the specified object.
	 * 
	 * @param obj
	 *          the object to find string.
	 * @param type
	 *          the type of resolved string.
	 * @param iArgs
	 *          the arguments used for fill string.
	 * @return resolved string.
	 */
	public String get(Object obj, Type type, Object... iArgs);

	/**
	 * Resolve a string from i18n using current locale for the specified object and element.
	 * 
	 * @param obj
	 *          target of key.
	 * @param key
	 *          to resolve.
	 * @param type
	 *          of key to resolve.
	 * @param iArgs
	 *          arguments used to fill resolved string.
	 * @return the resolved string
	 */
	public String get(Object obj, String key, Type type, Object... iArgs);

	public String resolve(SchemaClassDefinition iObjectClass, String iText, Object... iArgs);

	public String resolve(Object obj, String iText, Type type, Object... iArgs);

	public String resolve(String iText, Object... iArgs);

}
