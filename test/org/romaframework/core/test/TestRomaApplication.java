package org.romaframework.core.test;

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

import java.io.File;

import org.romaframework.core.config.RomaApplicationContext;

/**
 * Base JUnit class to make tests in application based on Roma Meta Framework.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 * @see TestRomaPersistentApplication
 */
public abstract class TestRomaApplication extends org.springframework.test.AbstractDependencyInjectionSpringContextTests {
	private boolean	started	= false;

	public TestRomaApplication() {
		// SET CURRENT APP'S PATH
		String appPath = new File(".").getAbsolutePath();
		RomaApplicationContext.setApplicationPath(appPath.substring(0, appPath.length() - 1));
	}

	protected void start() {
		if (!started) {
			started = true;
			RomaApplicationContext.getInstance().startup();
		}
	}

	public boolean isStarted() {
		return started;
	}
}
