package org.romaframework.core.schema;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class FeatureRegistry {

	private static final Object																					lock						= new Object();
	private static int																									ASPECT_COUNT		= 0;
	private static Map<String, Integer>																	aspectId				= new HashMap<String, Integer>();

	private static Map<String, Map<FeatureType, Integer>>								aspectFeatureId	= new HashMap<String, Map<FeatureType, Integer>>();
	private static Map<FeatureType, Map<String, Map<String, Feature>>>	features				= new HashMap<FeatureType, Map<String, Map<String, Feature>>>();

	/**
	 * Count the aspect.
	 * 
	 * @param aspectName
	 * 
	 * @return the counter of aspect.
	 */
	static int aspectNext(String aspectName) {
		synchronized (lock) {
			Integer id = aspectId.get(aspectName);
			if (id == null) {
				id = ASPECT_COUNT++;
				aspectId.put(aspectName, id);
			}
			return id;
		}
	}

	/**
	 * count the feature in the aspect for feature type.
	 * 
	 * @param aspectName
	 *          name of aspect fo feature.
	 * @param type
	 *          of target of feature.
	 * @return the count of aspect/type.
	 */
	public static int featureCount(String aspectName, FeatureType type) {
		synchronized (lock) {
			Map<FeatureType, Integer> ft = aspectFeatureId.get(aspectName);
			if (ft == null) {
				ft = new HashMap<FeatureType, Integer>();
				aspectFeatureId.put(aspectName, ft);
			}
			Integer i = ft.get(type);
			if (i == null)
				return 0;
			return i;
		}
	}

	/**
	 * Increase and return count the feature in the aspect for feature type.
	 * 
	 * @param aspectName
	 *          name of aspect fo feature.
	 * @param type
	 *          of target of feature.
	 * @return the count of aspect/type.
	 */

	static int featureNext(String aspectName, FeatureType type) {
		synchronized (lock) {
			Map<FeatureType, Integer> ft = aspectFeatureId.get(aspectName);
			if (ft == null) {
				ft = new HashMap<FeatureType, Integer>();
				aspectFeatureId.put(aspectName, ft);
			}
			Integer i = ft.get(type);
			if (i == null)
				i = 0;
			ft.put(type, i + 1);
			return i;
		}
	}

	/**
	 * 
	 * @return the total of up aspect.
	 */
	public static int aspectTotal() {
		synchronized (lock) {
			return ASPECT_COUNT;
		}
	}

	/**
	 * Register a feature.
	 * 
	 * @param register
	 *          a feature.
	 */
	public static void register(Feature<?> feature) {
		Map<String, Map<String, Feature>> types = features.get(feature.getType());
		if (types == null) {
			types = new HashMap<String, Map<String, Feature>>();
			features.put(feature.getType(), types);
		}
		Map<String, Feature> aspect = types.get(feature.getAspectName());
		if (aspect == null) {
			aspect = new HashMap<String, Feature>();
			types.put(feature.getAspectName(), aspect);
		}
		aspect.put(feature.getName(), feature);
	}

	/**
	 * Retrieve the feature with named parameters.
	 * 
	 * @param aspectName
	 *          the name of aspect.
	 * @param featureType
	 *          the type of target feature.
	 * @param featureName
	 *          the name of the feature.
	 * @return the feature correspondent to the info.
	 */
	public static Feature getFeature(String aspectName, FeatureType featureType, String featureName) {
		Map<String, Map<String, Feature>> types = features.get(featureType);
		if (types == null) {
			return null;
		}
		Map<String, Feature> aspects = types.get(aspectName);
		if (aspects == null) {
			return null;
		}
		return aspects.get(featureName);
	}

	/**
	 * Retrieve all features of an specified featureType.
	 * 
	 * @param featureType
	 *          the type of features.
	 * @return the list of features for the featureType.
	 */
	public static List<Feature> getFeatures(FeatureType featureType) {
		List<Feature> typeFeatures = new ArrayList<Feature>();
		Map<String, Map<String, Feature>> fe = features.get(featureType);
		if (fe != null) {
			for (Map<String, Feature> vals : fe.values()) {
				for (Feature feature : vals.values()) {
					typeFeatures.add(feature);
				}
			}
		}
		return typeFeatures;
	}

	/**
	 * Retrieve all features of an specified featureType , aspect.
	 * 
	 * @param aspectName
	 *          the name of aspect.
	 * 
	 * @param featureType
	 *          the type of features.
	 * @return the list of features for the featureType.
	 */
	public static List<Feature> getFeatures(String aspectName, FeatureType featureType) {
		List<Feature> typeFeatures = new ArrayList<Feature>();
		Map<String, Map<String, Feature>> fe = features.get(featureType);
		for (Feature feature : fe.get(aspectName).values()) {
			typeFeatures.add(feature);
		}
		return typeFeatures;
	}

}
