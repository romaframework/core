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

package org.romaframework.aspect.etl;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Set;

import org.romaframework.core.Roma;
import org.romaframework.core.Utility;
import org.romaframework.core.module.SelfRegistrantConfigurableModule;
import org.romaframework.core.schema.SchemaClassResolver;

public abstract class EtlAspect extends SelfRegistrantConfigurableModule<String> {

  @Deprecated
  public abstract int doImport(Object iRootObject, String iMappingName, InputStream iStream, ImportFactory iFactory)
      throws EtlException;

  public abstract int doExport(String iMappingName, OutputStream iStream) throws EtlException;

  /**
   * launches an ETL procedure given its name
   * 
   * @param name
   *          the symbolic name of an ETL definition
   * @return the number of records imported
   * @throws EtlException
   */
  public abstract int doImport(String name) throws EtlException;

  /**
   * launches an ETL procedure given its name
   * 
   * @param name
   *          the symbolic name of an ETL definition
   * @param input
   *          input values for the procedure
   * @return the number of records imported
   * @throws EtlException
   */
  public abstract int doImport(String name, Map<String, Object> input) throws EtlException;

  public String aspectName() {
    return ASPECT_NAME;
  }

  /**
   * @return the names of all available ETL procedure
   */
  public abstract Set<String> getEtlDefinitionNames();

  /**
   * given an etl definition name, returns its description
   * 
   * @param etlName
   *          the name of an etl definition
   * @return the descriprion of the etl procedure
   */
  public abstract String getEtlDescription(String etlName);

  @Override
  public void startup() {
    // REGISTER THE VIEW DOMAIN TO SCHEMA CLASS RESOLVER
    Roma.component(SchemaClassResolver.class).addDomainPackage(Utility.getApplicationAspectPackage(aspectName()));
  }

  public static final String ASPECT_NAME = "etl";
}
