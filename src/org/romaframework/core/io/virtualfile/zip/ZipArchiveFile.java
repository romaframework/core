/*
 * Copyright 2006-2007 Luca Garulli (luca.garulli--at--assetdata.it)
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
package org.romaframework.core.io.virtualfile.zip;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.romaframework.core.io.virtualfile.VirtualFile;

public class ZipArchiveFile implements VirtualFile {
	public static final String	PREFIX	= "jar:";

  protected ZipFile file;

  public ZipArchiveFile(URL url) throws IOException {
    JarURLConnection jarConnection = (JarURLConnection) url.openConnection();
    file = jarConnection.getJarFile();
  }

  public ZipArchiveFile(JarURLConnection jarConnection) throws IOException {
    file = jarConnection.getJarFile();
  }

  public boolean exists() {
    return true;
  }

  public VirtualFile[] listFiles() {
    Enumeration<? extends ZipEntry> e = file.entries();
    ZipEntry entry;
    Set<VirtualFile> files = new HashSet<VirtualFile>();
    while (e.hasMoreElements()) {
      entry = e.nextElement();
      files.add(new ZipEntryArchiveFile(entry, file));
    }

    VirtualFile[] vFile = new VirtualFile[files.size()];
    files.toArray(vFile);
    return vFile;
  }

  public boolean isDirectory() {
    return true;
  }

  public String getName() {
    return file.getName();
  }

  public InputStream getInputStream() {
    return null;
  }

  public OutputStream getOutputStream() {
  	return null;
  }
  
  public long length() {
    return file.size();
  }

  @Override
  public String toString() {
    return file.getName();
  }

  public String getAbsolutePath() {
    return file.getName();
  }
}
