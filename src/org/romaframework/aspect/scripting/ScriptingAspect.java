/*
 * Copyright 2008 Luigi Dell'Aquila (luigi.dellaquila@assetdata.it)
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
package org.romaframework.aspect.scripting;

import java.io.Reader;
import java.util.List;
import java.util.Map;

import org.romaframework.aspect.scripting.exception.ScriptEvaluationException;
import org.romaframework.aspect.scripting.exception.ScriptingException;
import org.romaframework.aspect.scripting.exception.UnsupportedLanguageException;
import org.romaframework.core.aspect.Aspect;

public interface ScriptingAspect extends Aspect {
	public static final String	OUT_READER_PAR	= "_outReader";
	public static final String	OUT_WRITER_PAR	= "_outWriter";

	public static final String	ASPECT_NAME			= "scripting";

	public Object evaluate(String language, Reader script, Map<String, Object> context) throws ScriptingException;

	/**
	 * evaluates a script
	 * 
	 * @param language
	 *          the language of the script
	 * @param script
	 *          the script to be executed
	 * @param context
	 *          the input/output context
	 * @return the return value of the script
	 * @throws ScriptEvaluationException
	 *           if an exception occurs during the execution
	 * @throws UnsupportedLanguageException
	 *           if the language is not supported
	 */
	public Object evaluate(String language, String script, Map<String, Object> context) throws ScriptingException;

	/**
	 * evaluates a script in the default language of the current ScriptingAspect implementation
	 * 
	 * @param script
	 *          the script to be executed
	 * @param context
	 *          the input/output context
	 * @return the return value of the script
	 * @throws ScriptEvaluationException
	 *           if an exception occurs during the execution
	 */
	public Object evaluate(String script, Map<String, Object> context) throws ScriptingException;

	/**
	 * returns the list of supported languages
	 * 
	 * @return the list of supported languages
	 */
	public List<String> getSupportedLanguages();

	/**
	 * Returns the default Scripting language.
	 * 
	 * @return
	 */
	public String getDefaultLanguage();
}