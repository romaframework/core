package org.romaframework.core.install;

import org.romaframework.core.module.SelfRegistrantModule;

public abstract class AbstractApplicationInstaller extends SelfRegistrantModule implements ApplicationInstaller {

	public String moduleName() {
		return MODULE_NAME;
	}

	public void startup() throws RuntimeException {
		if (!alreadyInstalled()) {
			install();
		}
	}

	public void shutdown() throws RuntimeException {
	}
}
