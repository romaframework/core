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

package org.romaframework.core.resource;

import org.romaframework.core.util.thread.SoftThread;

public class PollerThread extends SoftThread {

  private AutoReloadManager manager;
  private int               delay;

  public PollerThread(AutoReloadManager iManager, int iMilliseconds) {
    super("AutoReload");
    
    manager = iManager;
    delay = iMilliseconds;

    start();
  }

  @Override
  protected void execute() {
    manager.checkResources();

    try {
      Thread.sleep(delay);
    } catch (InterruptedException e) {
    }
  }
}
