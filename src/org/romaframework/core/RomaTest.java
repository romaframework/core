package org.romaframework.core;

import java.io.File;

import org.romaframework.core.component.SpringComponentEngine;
import org.romaframework.core.config.RomaApplicationContext;

public class RomaTest {

	public static void init(String... additionalApplicationContext) {
		initEnvironment(null, additionalApplicationContext);
	}

	public static void initEnvironment(String environment, String... additionalApplicationContext) {
		String appPath = new File("WebContent").getAbsolutePath();
		SpringComponentEngine sce = new SpringComponentEngine();
		sce.setBasePath(environment);
		sce.setAdditionalPaths(additionalApplicationContext);
		RomaApplicationContext.getInstance().setComponentAspect(sce);
		RomaApplicationContext.setApplicationPath(appPath.substring(0, appPath.length() - 1));
		RomaApplicationContext.getInstance().startup();
	}

}
