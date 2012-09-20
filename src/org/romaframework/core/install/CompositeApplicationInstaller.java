package org.romaframework.core.install;

import java.util.List;

public class CompositeApplicationInstaller extends AbstractApplicationInstaller {

	private List<ApplicationInstaller>	installers;

	public void install() {
		for (ApplicationInstaller applicationInstaller : installers) {
			if (applicationInstaller.alreadyInstalled()) {
				return;
			}
		}
		for (ApplicationInstaller applicationInstaller : installers) {
			applicationInstaller.install();
		}
	}

	public boolean alreadyInstalled() {
		return false;
	}

	public List<ApplicationInstaller> getInstallers() {
		return installers;
	}

	public void setInstallers(List<ApplicationInstaller> installers) {
		this.installers = installers;
	}

}
