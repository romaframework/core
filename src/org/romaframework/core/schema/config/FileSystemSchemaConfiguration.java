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
package org.romaframework.core.schema.config;

import java.io.File;
import java.io.IOException;

/**
 * File System implementation of Schema class configuration. It loads the schema from File object located anywhere in the file
 * system.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public class FileSystemSchemaConfiguration extends SchemaConfiguration {

	protected File		fileContrainer;
	protected String	fileName;

	public FileSystemSchemaConfiguration(File iFileContainer, String iInputFile) throws IOException {
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
		// InputStream input = getClass().getResourceAsStream(inputFile);
		// try {
		// XmlConfigClassDocument doc = XmlConfigClassDocument.Factory.parse(input);
		// //setType(doc.getClass1()); //TODO DOES NOT COMPILE ANYMORE: THIS CLASS SHOULD BE REMOVED!!!
		// } finally {
		// input.close();
		// }
	}

	//
	// public void save(XmlConfigClassDocument toSave) throws IOException {
	// XmlOptions options = new XmlOptions();
	// options.setCharacterEncoding("UTF-8");
	// options.setUseDefaultNamespace();
	// options.setSavePrettyPrint();
	//
	// toSave.save(new File(getClass().getResource(inputFile).getFile()), options);
	// //setType(toSave.getClass1());//TODO DOES NOT COMPILE ANYMORE: THIS CLASS SHOULD BE REMOVED!!!
	// }

	@Override
	public File getFile() {
		return fileContrainer;
	}
}
