package org.romaframework.core.test.features;

import junit.framework.Assert;

import org.romaframework.aspect.core.feature.CoreFieldFeatures;
import org.romaframework.core.schema.FeatureRegistry;
import org.romaframework.core.schema.FeatureType;

public class FeatureLoading {

	//@Test
	public void testLoadFeatures() {

		try {
			Class.forName("org.romaframework.aspect.core.feature.CoreFieldFeatures");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		Assert.assertEquals("Number of aspect", FeatureRegistry.aspectTotal(), 1);
		Assert.assertEquals("Number of feature", FeatureRegistry.featureCount("core", FeatureType.FIELD), 3);
		Assert.assertEquals("Id  of aspect in  feature", CoreFieldFeatures.EMBEDDED_TYPE.getAspectId(), 0);
		Assert.assertEquals("Id EMBEDDED_TYPE of feature", CoreFieldFeatures.EMBEDDED_TYPE.getFeatureId(), 0);
		Assert.assertEquals("Id EMBEDDED of feature", CoreFieldFeatures.EMBEDDED.getFeatureId(), 1);
		Assert.assertEquals("Id USE_RUNTIME_TYPE of feature", CoreFieldFeatures.USE_RUNTIME_TYPE.getFeatureId(), 2);
	}

}
