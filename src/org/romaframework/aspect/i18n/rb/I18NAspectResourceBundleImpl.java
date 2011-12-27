package org.romaframework.aspect.i18n.rb;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.romaframework.aspect.i18n.I18NAspectAbstract;
import org.romaframework.core.Roma;
import org.romaframework.core.Utility;
import org.romaframework.core.flow.Controller;
import org.romaframework.core.resource.AutoReloadListener;
import org.romaframework.core.resource.AutoReloadManager;
import org.romaframework.core.resource.ResourceResolver;
import org.romaframework.core.resource.ResourceResolverListener;

public class I18NAspectResourceBundleImpl extends I18NAspectAbstract implements AutoReloadListener, ResourceResolverListener {

	private static final String				PROPERTIES_SUFFIX	= ".properties";
	private static Log								log								= LogFactory.getLog(I18NAspectResourceBundleImpl.class);
	private I18nFileManager						fileManager				= new I18nFileManager();
	private List<String>							packages;
	private Map<String, List<String>>	reloadMap					= new HashMap<String, List<String>>();
	private final AutoReloadManager		autoReloadManager;

	public I18NAspectResourceBundleImpl(AutoReloadManager iAutoReloadManager) {
		this.autoReloadManager = iAutoReloadManager;
	}

	@Override
	public Set<Locale> getAvailableLanguages() {
		return fileManager.getAvailableLanguages();
	}


	@Override
	public void addResource(File iFile, String iName, String iPackagePrefix, String iStartingPackage) {
		if (packages == null)
			return;
		boolean skip = true;
		for (String loadPackage : packages) {
			if (iPackagePrefix.startsWith(loadPackage)) {
				skip = false;
				break;
			}
		}
		if (skip)
			return;
		if (iName == null || !iName.endsWith(PROPERTIES_SUFFIX))
			return;

		log.info("Loading bundle: " + iPackagePrefix + iName);

		String fileName = iPackagePrefix.replace('.', '/') + iName;
		try {
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
			if (inputStream == null) {
				log.error("File Not Found:" + fileName);
			}
			fileManager.load(iName, inputStream);
		} catch (Exception e) {
			log.error("Error to loading i18n file:" + iName, e);
		}
		autoReloadManager.addResource(iFile, this);
		List<String> lreload = reloadMap.get(iFile.getName());
		if (lreload == null) {
			lreload = new ArrayList<String>();
			reloadMap.put(iFile.getName(), lreload);
		}
		lreload.add(fileName);
	}

	@Override
	public void signalUpdatedFile(File iFile) {
		try {
			List<String> lreload = reloadMap.get(iFile.getName());
			if (lreload != null) {
				for (String string : lreload) {
					fileManager.load(string, getClass().getClassLoader().getResourceAsStream(string));
				}
			}
		} catch (Exception e) {
			log.error("Error to loading i18n file:" + iFile.getName(), e);
		}
	}

	@Override
	public void startup() {
		if (packages == null) {
			packages = new ArrayList<String>();
		}
		if (!packages.contains(Utility.getApplicationAspectPackage(aspectName()))) {
			packages.add(Utility.getApplicationAspectPackage(aspectName()));
		}

		// Register this instance on Resources Resolver for find i18n properties files.
		Controller.getInstance().registerListener(ResourceResolverListener.class, this);

		for (String loadPackage : packages) {
			Roma.component(ResourceResolver.class).loadResources(loadPackage);
		}
	}

	public List<String> getPackages() {
		return packages;
	}

	public void setPackages(List<String> packages) {
		this.packages = packages;
	}

	public void setDefaultLocale(String defaultLocale) {
		this.fileManager.setDefaultLocale(defaultLocale);
	}

}
