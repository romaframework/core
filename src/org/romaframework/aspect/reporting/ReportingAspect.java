/*
 * Copyright 2006 Giordano Maestro (giordano.maestro--at--assetdata.it)
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

package org.romaframework.aspect.reporting;

import java.io.OutputStream;
import java.util.Collection;

import org.romaframework.core.aspect.Aspect;
import org.romaframework.core.schema.SchemaObject;

/**
 * Reporting Aspect behavior interface.
 * 
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public interface ReportingAspect extends Aspect {

	public static final String	ASPECT_NAME	= "reporting";

	/**
	 * Return the supported documents types
	 * 
	 * @return
	 */
	public String[] getSupportedTypes();

	/**
	 * Create the template of the given Object
	 * 
	 * @param iExample
	 * @throws ReportingException
	 */
	public void createTemplate(Object iExample) throws ReportingException;

	/**
	 * Create a template of the given object, it also create a class based template for dynamic report component as row set rendering.
	 * 
	 * @param iExample
	 * @throws ReportingException
	 */
	public void createDynaTemplate(Object iExample) throws ReportingException;

	/**
	 * Get the mimetype of a document from the type extension
	 * 
	 * @param renderType
	 * @return
	 */
	public String getDocumentType(String renderType);

	/**
	 * Generate the report of the Object
	 * 
	 * @param iToRender
	 *          The object to render
	 * @param iRenderType
	 *          The document type
	 * @param iUserSchema
	 *          The personalized schema for the rendering
	 * @return
	 */
	public byte[] render(Object iToRender, String iRenderType, SchemaObject iUserSchema);

	/**
	 * Generate the report of the Object
	 * 
	 * @param iToRender
	 *          The object to render
	 * @param iRenderType
	 *          The document type
	 * @param iUserSchema
	 *          The personalized schema for the rendering
	 * @param outputStream
	 *          the output stream where write the report.
	 */
	public void render(Object iToRender, String iRenderType, SchemaObject iUserSchema, OutputStream outputStream);

	/**
	 * Generate the report of a Collection
	 * 
	 * @param collection
	 * @param iRenderType
	 * @param iUserSchema
	 * @return
	 */
	public byte[] renderCollection(Collection<?> collection, String iRenderType, SchemaObject iUserSchema);

	/**
	 * Generate the report of a Collection
	 * 
	 * @param collection
	 * @param iRenderType
	 * @param iUserSchema
	 * @param outputStream
	 */
	public void renderCollection(Collection<?> collection, String iRenderType, SchemaObject iUserSchema, OutputStream outputStream);
}
