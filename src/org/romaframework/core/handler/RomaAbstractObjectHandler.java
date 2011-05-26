/*
 * Copyright 2006-2009 Luca Garulli (luca.garulli--at--assetdata.it)
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
package org.romaframework.core.handler;

import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.schema.SchemaObject;

/**
 * Basic implementation of ObjectHandler.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 * 
 */
public abstract class RomaAbstractObjectHandler implements RomaObjectHandler {
  protected Object            content;
  protected RomaObjectHandler containerComponent;

  protected SchemaObject      schemaObject;
  protected SchemaField       schemaField;

  public Object getContent() {
    return content;
  }

  public void setContent(Object content) {
    this.content = content;
  }

  public RomaObjectHandler getContainerComponent() {
    return containerComponent;
  }

  public void setContainerComponent(RomaObjectHandler containerComponent) {
    this.containerComponent = containerComponent;
  }

  public SchemaObject getSchemaObject() {
    return schemaObject;
  }

  public void setSchemaObject(SchemaObject schemaObject) {
    this.schemaObject = schemaObject;
  }

  public SchemaField getSchemaField() {
    return schemaField;
  }

  public void setSchemaField(SchemaField schemaField) {
    this.schemaField = schemaField;
  }
}
