/*
 * Copyright 2006 Luca Garulli (luca.garulli--at--assetdata.it)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.romaframework.core.resource;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.romaframework.core.config.Serviceable;

/**
 * Check for file chamges and propagates update to all registered listeners for that file.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public class AutoReloadManager implements Serviceable {

  private Map<String, ResourceInfo> resources;
  private int                       checkDelay;
  private PollerThread              poller;

  private static Log                log = LogFactory.getLog(AutoReloadManager.class);

  /**
   * Construct the object using a delay time.
   * 
   * @param iCheckDelay
   *          Millisecond to wait between polls. 0 or less to disable auto reload.
   */
  public AutoReloadManager(int iCheckDelay) {
    checkDelay = iCheckDelay;
    resources = new HashMap<String, ResourceInfo>();

    startup();
  }

  /**
   * Add a resource to be monitoring by a listener
   * 
   * @param iFile
   *          File to monitor
   * @param iListener
   *          Listener to be wakeup on file change
   */
  public synchronized void addResource(File iFile, AutoReloadListener iListener) {
    ResourceInfo resource = getResourceInfo(iFile);

    // REGISTER THE LISTENER
    resource.listeners.add(iListener);
  }

  /**
   * Add a resource to be monitoring by multiple listeners
   * 
   * @param iFile
   *          File to monitor
   * @param iListeners
   *          The Set of Listeners to be wakeup on file change
   */
  public synchronized void addResource(File iFile, Set<AutoReloadListener> iListeners) {
    ResourceInfo resource = getResourceInfo(iFile);

    // REGISTER THE LISTENERS
    resource.listeners = iListeners;
  }

  /**
   * Get the ResourceInfo object for the file to be monitored
   * 
   * @param iFile
   *          File to monitor
   */
  protected ResourceInfo getResourceInfo(File iFile) {
    String absolutePath = iFile.getAbsolutePath();
    ResourceInfo resource = resources.get(absolutePath);
    if (resource == null) {
      resource = new ResourceInfo(iFile);
      resources.put(absolutePath, resource);
    }
    return resource;
  }

  protected synchronized void checkResources() {
    ResourceInfo currentResource;
    long lastModified;

    for (Iterator<ResourceInfo> it = resources.values().iterator(); it.hasNext();) {
      currentResource = it.next();
      lastModified = currentResource.file.lastModified();

      if (lastModified > currentResource.lastModified) {
        // SIGNAL ALL REGISTERED LISTENERS
        for (AutoReloadListener listener : currentResource.listeners) {
          try {
            listener.signalUpdatedFile( currentResource.file);
            currentResource.lastModified = lastModified;
          } catch (Exception e) {
            log.error("[AutoReloadManager.checkResources] Error on reloading resource: " + currentResource.file, e);
          }
        }
      }
    }
  }

  public String getStatus() {
    return null;
  }

  public void startup() throws RuntimeException {
    if (checkDelay > 0)
      poller = new PollerThread(this, checkDelay);
  }

  public void shutdown() throws RuntimeException {
    if (poller != null)
      poller.sendShutdown();
  }
}
