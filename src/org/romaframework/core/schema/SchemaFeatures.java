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

public abstract class SchemaFeatures implements Cloneable, Serializable {
	private static final long		serialVersionUID	= -4789886810661429988L;

	private static final Object	UNSETTED_VALUE		= new Object() {
																									public String toString() {
																										return "Unsetted value";
																									}
																								};

	protected Object[][]				features;
	protected SchemaFeatures		parent;
	protected FeatureType				featureType;

	public SchemaFeatures(FeatureType featureType) {
		this.featureType = featureType;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		SchemaFeatures cloned = (SchemaFeatures) super.clone();
		cloned.features = null;
		cloned.parent = this;
		return cloned;
	}

	public <T> void setFeature(Feature<T> feature, T iFeatureValue) {
		if (features == null) {
			features = new Object[FeatureRegistry.aspectTotal()][];
		}
		Object[] aspectFeature = features[feature.getAspectId()];
		if (aspectFeature == null) {
			aspectFeature = new Object[FeatureRegistry.featureCount(feature.getAspectName(), feature.getType())];
			for (int i = 0; i < aspectFeature.length; i++)
				aspectFeature[i] = UNSETTED_VALUE;
			features[feature.getAspectId()] = aspectFeature;
		}
		aspectFeature[feature.getFeatureId()] = iFeatureValue;
	}

	@SuppressWarnings("unchecked")
	public <T> T getFeature(Feature<T> feature) {
		if (features != null) {
			Object[] aspectFeature = features[feature.getAspectId()];
			if (aspectFeature != null) {
				Object value = aspectFeature[feature.getFeatureId()];
				if (value != UNSETTED_VALUE) {
					return (T) value;
				}
			}
		}
		if (parent == null)
			return feature.getDefaultValue();
		return (T) parent.getFeature(feature);
	}

	public <T> boolean isSettedFeature(Feature<T> feature) {
		if (features != null) {
			Object[] aspectFeature = features[feature.getAspectId()];
			if (aspectFeature != null) {
				Object value = aspectFeature[feature.getFeatureId()];
				if (value != UNSETTED_VALUE) {
					return true;
				}
			}
		}
		if (parent == null)
			return false;
		return parent.isSettedFeature(feature);
	}

	public FeatureType getFeatureType() {
		return featureType;
	}
}
