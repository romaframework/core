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

package org.romaframework.aspect.persistence;

import java.util.List;

public class Query {
  protected int     totalItems     = -1;
  protected int     rangeFrom      = -1;
  protected int     rangeTo        = -1;
  protected byte    retrievingMode = PersistenceAspect.STRATEGY_DEFAULT;

  protected String  mode;
  protected boolean subClasses     = false;
  protected List    result;

  public byte getStrategy() {
    return retrievingMode;
  }

  public void setStrategy(byte retrievingMode) {
    this.retrievingMode = retrievingMode;
  }

  public List getResult() {
    return result;
  }

  public void setResult(List result) {
    this.result = result;
  }

  public void setRangeFrom(int iRangeFrom, int iRangeTo) {
    rangeFrom = iRangeFrom;
    rangeTo = iRangeTo;
  }

  public int getRangeFrom() {
    return rangeFrom;
  }

  public int getRangeTo() {
    return rangeTo;
  }

  public int getTotalItems() {
    return totalItems;
  }

  public void setTotalItems(int totalItems) {
    this.totalItems = totalItems;
  }

  public boolean isSubClasses() {
    return subClasses;
  }

  public void setSubClasses(boolean subClasses) {
    this.subClasses = subClasses;
  }

  public String getMode() {
    return mode;
  }

  public void setMode(String mode) {
    this.mode = mode;
  }
}
