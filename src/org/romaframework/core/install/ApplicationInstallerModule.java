package org.romaframework.core.install;

import java.util.Map;

import org.romaframework.core.Roma;
import org.romaframework.core.config.RomaApplicationContext;
import org.romaframework.core.config.RomaApplicationListener;
import org.romaframework.core.flow.Controller;
import org.romaframework.core.module.SelfRegistrantModule;

public class ApplicationInstallerModule extends SelfRegistrantModule implements RomaApplicationListener {

	public static final String	MODULE_NAME	= "ApplicationInstaller";

	public ApplicationInstallerModule() {
		Controller.getInstance().registerListener(RomaApplicationListener.class, this);
	}

	public String moduleName() {
		return MODULE_NAME;
	}

	public void startup() throws RuntimeException {
	}

	public void shutdown() throws RuntimeException {
	}

	public void onBeforeStartup() {

	}

	public void onAfterStartup() {
		Roma.context().create();
		try {
			Map<String, ApplicationInstaller> entries = RomaApplicationContext.getInstance().getComponentAspect().getComponentsOfClass(ApplicationInstaller.class);
			for (ApplicationInstaller installer : entries.values()) {
				if (!installer.alreadyInstalled())
					installer.install();
			}
		} finally {
			Roma.context().destroy();
		}
	}

	public void onBeforeShutdown() {

	}

	public void onAfterShutdown() {

	}
}
