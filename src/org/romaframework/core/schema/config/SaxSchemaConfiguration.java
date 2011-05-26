/*
 * Copyright 2006-2009 Luigi Dell'Aquila (luigi.dellaquila--at--assetdata.it)
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
package org.romaframework.core.schema.config;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.romaframework.core.schema.xmlannotations.XmlAnnotationParser;
import org.romaframework.core.schema.xmlannotations.XmlClassAnnotation;

/**
 * File System implementation of Schema class configuration based on SAX. It loads the schema from File object located anywhere in
 * the file system.
 * 
 * @author Luigi Dell'Aquila (luigi.dellaquila--at--assetdata.it)
 */
public class SaxSchemaConfiguration extends SchemaConfiguration {

	protected static Log	log	= LogFactory.getLog(SaxSchemaConfiguration.class);

	protected File				fileContrainer;
	protected String			fileName;

	public SaxSchemaConfiguration(File iFileContainer, String iInputFile) throws IOException {
		fileContrainer = iFileContainer;
		fileName = iInputFile;
		load();
	}

	@Override
	public String getFilePath() {
		return fileName;
	}

	@Override
	public void load() throws IOException {
		InputStream input = getClass().getResourceAsStream(fileName);

		if (input == null)
			return;

		try {
			XmlClassAnnotation annotation = XmlAnnotationParser.parseClass(input);
			setType(annotation);
		} catch (Exception e) {
			log.error("invalid xml annotation " + fileName, e);
		} finally {
			input.close();
		}
	}

	public void save(XmlClassAnnotation toSave) throws IOException {
		String result = XmlAnnotationParser.print(toSave);
		FileWriter writer = new FileWriter(new File(getClass().getResource(fileName).getFile()));
		writer.write(result);
		writer.flush();
		writer.close();
	}

	@Override
	public File getFile() {
		return fileContrainer;
	}
}
