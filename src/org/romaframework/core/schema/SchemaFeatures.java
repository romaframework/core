/*
 * Copyright 2006 Luca Garulli (luca.garulli--at--assetdata.it)
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

package org.romaframework.core.schema;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.romaframework.core.util.DynaBean;

public abstract class SchemaFeatures implements Cloneable, Serializable {
	private static final long				serialVersionUID	= -4789886810661429988L;

	protected Map<String, DynaBean>	allFeatures;

	public SchemaFeatures() {
		allFeatures = new HashMap<String, DynaBean>();
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		SchemaFeatures cloned = (SchemaFeatures) super.clone();
		cloned.allFeatures = new HashMap<String, DynaBean>();
		for (Map.Entry<String, DynaBean> entry : allFeatures.entrySet()) {
			cloned.allFeatures.put(entry.getKey(), (DynaBean) entry.getValue().clone());
		}
		return cloned;
	}

	public Map<String, DynaBean> getAllFeatures() {
		return allFeatures;
	}

	public DynaBean getFeatures(String iAspectName) {
		return allFeatures.get(iAspectName);
	}

	public void setFeatures(String iAspectName, DynaBean iFeatures) {
		allFeatures.put(iAspectName, iFeatures);
	}

	public Object getFeature(String iAspectName, String iFeatureName) {
		DynaBean features = getFeatures(iAspectName);
		if (features != null)
			return features.getAttribute(iFeatureName);
		return null;
	}

	public void setFeature(String iAspectName, String iFeatureName, Object iFeatureValue) {
		DynaBean features = getFeatures(iAspectName);
		if (features != null)
			features.setAttribute(iFeatureName, iFeatureValue);
	}
}
