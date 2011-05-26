package org.romaframework.core.schema.config;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class Loader implementation of Schema class configuration. It loads the schema using Class Loader object located anywhere in the
 * file system.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
@Deprecated
public class ClassPathSchemaConfiguration extends SchemaConfiguration {
	protected static Log	log	= LogFactory.getLog(ClassPathSchemaConfiguration.class);
	protected InputStream	inputStream;

	public ClassPathSchemaConfiguration(InputStream iInputStream) throws IOException {
		inputStream = iInputStream;
		load();
	}

	@Override
	public void load() throws IOException {
		// try {
		// XmlClassAnnotation annotation = XmlAnnotationParser.parseClass(inputStream);
		// setType(annotation);
		// } catch(Exception e){
		// log.error("invalid xml annotation ", e);
		// }finally {
		// inputStream.close();
		// }
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
}
