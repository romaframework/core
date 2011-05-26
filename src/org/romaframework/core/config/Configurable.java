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

package org.romaframework.core.config;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import org.romaframework.core.GlobalConstants;

public class Configurable<T> {

  protected Map<String, T>   configuration  = new LinkedHashMap<String, T>();

  public static final String DEFAULT_CONFIG = GlobalConstants.ROOT_CLASS;

  public Configurable() {
    this(false);
  }

  public Configurable(boolean iSorted) {
    if (iSorted)
      configuration = new TreeMap<String, T>();
    else
      configuration = new LinkedHashMap<String, T>();
  }

  public Collection<T> getConfigurationValues() {
    return configuration.values();
  }

  public T getConfiguration(String iObjectName) {
    return configuration.get(iObjectName);
  }

  /**
   * Add an object registering it with key equals to toString() result.
   * 
   * @param iObjectName
   * @param iObject
   */
  public void addConfiguration(T iObject) {
    configuration.put(iObject.toString(), iObject);
  }

  /**
   * Add an onbject
   * 
   * @param iObjectName
   * @param iObject
   */
  public void addConfiguration(String iObjectName, T iObject) {
    configuration.put(iObjectName, iObject);
  }

  /**
   * Set multiple objects in one shot. This method it's callable by spring to set multiple references.
   * 
   * @param iConfig
   */
  public void setConfiguration(Map<String, T> iConfig) {
    configuration.putAll(iConfig);
  }
}
