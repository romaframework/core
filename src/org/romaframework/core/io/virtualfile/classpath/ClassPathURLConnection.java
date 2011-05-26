package org.romaframework.core.io.virtualfile.classpath;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class ClassPathURLConnection extends URLConnection {

	protected ClassPathURLConnection(URL url) {
		super(url);
	}

	@Override
	public void connect() throws IOException {
	}
}
