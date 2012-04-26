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

import java.util.List;

import org.romaframework.core.flow.Controller;
import org.romaframework.core.flow.SchemaActionListener;
import org.romaframework.core.flow.SchemaFieldListener;
import org.romaframework.core.schema.SchemaAction;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.util.ListenerManager;

/**
 * The base implementation of the logging aspect
 * 
 * @author Giordano Maestro (giordano.maestro--at--assetdata.it)
 * 
 */
public class LoggingCommonAspect extends LoggingAspectAbstract implements SchemaActionListener, SchemaFieldListener {

	protected ListenerManager<String>	loggers	= new ListenerManager<String>();

	public LoggingCommonAspect() {
		Controller.getInstance().registerListener(SchemaActionListener.class, this);
		Controller.getInstance().registerListener(SchemaFieldListener.class, this);

	}

	public void onAfterAction(Object iContent, SchemaAction iAction, Object returnedValue) {
		LoggingHelper.managePostAction(iContent, iAction, returnedValue);
	}

	public boolean onBeforeAction(Object iContent, SchemaAction iAction) {
		LoggingHelper.managePreAction(iContent, iAction);
		return true;
	}

	public void onExceptionAction(Object iContent, SchemaAction iAction, Exception exception) {
		LoggingHelper.manageException(iContent, iAction, exception);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.romaframework.core.flow.UserObjectEventListener#onAfterFieldRead(java.lang.Object,
	 * org.romaframework.core.schema.SchemaField, java.lang.Object)
	 */
	public Object onAfterFieldRead(Object content, SchemaField field, Object currentValue) {
		return currentValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.romaframework.core.flow.UserObjectEventListener#onAfterFieldWrite(java.lang.Object,
	 * org.romaframework.core.schema.SchemaField, java.lang.Object)
	 */
	public Object onAfterFieldWrite(Object content, SchemaField field, Object currentValue) {
		LoggingHelper.manageAfterFieldWrite(field, currentValue);
		return currentValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.romaframework.core.flow.UserObjectEventListener#onBeforeFieldRead(java.lang.Object,
	 * org.romaframework.core.schema.SchemaField, java.lang.Object)
	 */
	public Object onBeforeFieldRead(Object content, SchemaField field, Object currentValue) {
		return IGNORED;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.romaframework.core.flow.UserObjectEventListener#onBeforeFieldWrite(java.lang.Object,
	 * org.romaframework.core.schema.SchemaField, java.lang.Object)
	 */
	public Object onBeforeFieldWrite(Object content, SchemaField field, Object currentValue) {
		return IGNORED;
	}

	@Override
	public void shutdown() {
		super.shutdown();
	}

	@Override
	public void startup() {
		super.startup();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.romaframework.aspect.logging.LoggingAspect#registerLogger(org.romaframework.aspect.logging.Logger)
	 */
	public void registerLogger(Logger logger) {
		String[] types = logger.getModes();
		if (types != null) {
			for (String type : types) {
				loggers.registerListener(type, logger);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.romaframework.aspect.logging.LoggingAspect#removeLogger(org.romaframework.aspect.logging.Logger)
	 */
	public void removeLogger(Logger logger) {
		String[] types = logger.getModes();
		if (types != null) {
			for (String type : types) {
				loggers.unregisterListener(type, logger);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.romaframework.aspect.logging.LoggingAspect#log(int, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void log(int level, String category, String mode, String message) {
		List<Logger> modeLoggers = loggers.getListeners(mode);
		if (modeLoggers != null) {
			for (Logger logger : modeLoggers) {
				logger.print(level, category, message);
			}
		}
	}

	public Object getUnderlyingComponent() {
		return null;
	}
}
