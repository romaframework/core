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

import org.romaframework.aspect.session.SessionInfo;
import org.romaframework.core.flow.Controller;
import org.romaframework.core.flow.UserObjectEventListener;
import org.romaframework.core.schema.SchemaClassElement;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.util.ListenerManager;

/**
 * The base implementation of the logging aspect
 * 
 * @author Giordano Maestro (giordano.maestro--at--assetdata.it)
 * 
 */
public class LoggingCommonAspect extends LoggingAspectAbstract implements UserObjectEventListener {

  protected ListenerManager<String> loggers = new ListenerManager<String>();

  /*
   * (non-Javadoc)
   * 
   * @see org.romaframework.core.flow.UserObjectEventListener#onAfterActionExecution(java.lang.Object,
   * org.romaframework.core.schema.SchemaElement, java.lang.Object)
   */
  public void onAfterActionExecution(Object content, SchemaClassElement action, Object returnedValue) {
    LoggingHelper.managePostAction(content, action, returnedValue);
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
   * @see org.romaframework.core.flow.UserObjectEventListener#onBeforeActionExecution(java.lang.Object,
   * org.romaframework.core.schema.SchemaElement)
   */
  public boolean onBeforeActionExecution(Object content, SchemaClassElement action) {
    LoggingHelper.managePreAction(content, action);
    return true;
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
    return currentValue;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.romaframework.core.flow.UserObjectEventListener#onFieldRefresh(org.romaframework.aspect.session.SessionInfo,
   * java.lang.Object, org.romaframework.core.schema.SchemaField)
   */
  public void onFieldRefresh(SessionInfo session, Object content, SchemaField field) {

  }

  @Override
  public void shutdown() {
    super.shutdown();
  }

  @Override
  public void startup() {
    super.startup();
    Controller.getInstance().registerListener(UserObjectEventListener.class, this);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.romaframework.core.flow.UserObjectEventListener#onException(java.lang.Object,
   * org.romaframework.core.schema.SchemaElement, java.lang.Throwable)
   */
  public Object onException(Object content, SchemaClassElement element, Throwable throwed) {
    LoggingHelper.manageException(content, element, throwed);
    return null;
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

  public int getPriority() {
    return 0;
  }

  public Object getUnderlyingComponent() {
    return null;
  }
}
