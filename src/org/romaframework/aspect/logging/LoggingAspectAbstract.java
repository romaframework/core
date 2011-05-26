/*
 * Copyright 2006-2007 Luca Garulli      (luca.garulli--at--assetdata.it)
 *                     Giordano Maestro  (giordano.maestro--at--assetdata.it)
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

package org.romaframework.aspect.logging;

import java.lang.annotation.Annotation;
import java.math.BigInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.logging.annotation.LoggingAction;
import org.romaframework.aspect.logging.annotation.LoggingClass;
import org.romaframework.aspect.logging.annotation.LoggingField;
import org.romaframework.aspect.logging.feature.LoggingActionFeatures;
import org.romaframework.aspect.logging.feature.LoggingClassFeatures;
import org.romaframework.aspect.logging.feature.LoggingFieldFeatures;
import org.romaframework.core.Utility;
import org.romaframework.core.module.SelfRegistrantConfigurableModule;
import org.romaframework.core.schema.SchemaClassDefinition;
import org.romaframework.core.schema.SchemaClassElement;
import org.romaframework.core.schema.SchemaEvent;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.schema.xmlannotations.XmlAspectAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlActionAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlClassAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlEventAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlFieldAnnotation;
import org.romaframework.core.util.DynaBean;

/**
 * Abstract implementation for Logging Aspect.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public abstract class LoggingAspectAbstract extends SelfRegistrantConfigurableModule<String> implements LoggingAspect {

	public static final String	ASPECT_NAME	= "logging";

	protected Log								log					= LogFactory.getLog(this.getClass());

	public void beginConfigClass(SchemaClassDefinition iClass) {
	}

	public void endConfigClass(SchemaClassDefinition iClass) {
	}

	public void configClass(SchemaClassDefinition iClass, Annotation iAnnotation, XmlClassAnnotation iNode) {
		DynaBean features = iClass.getFeatures(ASPECT_NAME);
		if (features == null) {
			// CREATE EMPTY FEATURES
			features = new LoggingActionFeatures();
			iClass.setFeatures(ASPECT_NAME, features);
		}
		configClassAnnotations(iClass, iAnnotation, features);
		readClassXml(iClass, iNode, features);
	}

	/**
	 * Read the annotation of the class
	 * 
	 * @param class1
	 *          the SchemaClassReflection of the class
	 * @param iXmlNode
	 *          the xml configuration of the class
	 * @param features
	 *          The Dynabean with the feature configuration
	 */
	private void readClassXml(SchemaClassDefinition class1, XmlClassAnnotation iXmlNode, DynaBean features) {
		// PROCESS DESCRIPTOR CFG DESCRIPTOR ATTRIBUTES (IF DEFINED) OVERWRITE DEFAULT AND ANNOTATION VALUES
		if (iXmlNode == null || iXmlNode.aspect(ASPECT_NAME) == null) {
			return;
		}

		XmlAspectAnnotation descriptor = iXmlNode.aspect(ASPECT_NAME);

		if (descriptor == null) {
			return;
		}

		String enabled = descriptor.getAttribute(LoggingClassFeatures.ENABLED);
		if (enabled != null) {
			features.setAttribute(LoggingClassFeatures.ENABLED, new Boolean(enabled));
		}

		String mode = descriptor.getAttribute(LoggingClassFeatures.MODE);
		if (mode != null) {
			features.setAttribute(LoggingClassFeatures.MODE, new Boolean(mode));
		}

	}

	private void configClassAnnotations(SchemaClassDefinition iClass, Annotation iAnnotation, DynaBean features) {
		LoggingClass annotation = (LoggingClass) iAnnotation;
		if (annotation != null) {
			if (annotation.enabled() != AnnotationConstants.UNSETTED)
				features.setAttribute(LoggingClassFeatures.ENABLED, annotation.enabled() == AnnotationConstants.TRUE);

			if (annotation.mode() != AnnotationConstants.DEF_VALUE)
				features.setAttribute(LoggingClassFeatures.MODE, annotation.mode());
		}

	}

	public void configField(SchemaField iField, Annotation iAnnotation, Annotation iGenericAnnotation, Annotation getterAnnotation,
			XmlFieldAnnotation iXmlNode) {
		configFieldAnnotations(iField, iAnnotation, iGenericAnnotation);
		readFieldXml(iField, iXmlNode);
	}

	public void configAction(SchemaClassElement iAction, Annotation iActionAnnotation, Annotation iGenericAnnotation,
			XmlActionAnnotation iXmlNode) {
		configActionAnnotations(iAction, iActionAnnotation, iGenericAnnotation);
		readActionXml(iAction, iXmlNode);
	}

	private void configActionAnnotations(SchemaClassElement iElement, Annotation iAnnotation, Annotation iGenericAnnotation) {
		DynaBean features = iElement.getFeatures(ASPECT_NAME);
		if (features == null) {
			// CREATE EMPTY FEATURES
			features = new LoggingActionFeatures();
			iElement.setFeatures(ASPECT_NAME, features);
		}

		readAnnotation(iElement, iAnnotation, features);
	}

	private void configFieldAnnotations(SchemaClassElement iElement, Annotation iAnnotation, Annotation iGenericAnnotation) {
		DynaBean features = iElement.getFeatures(ASPECT_NAME);
		if (features == null) {
			// CREATE EMPTY FEATURES
			features = new LoggingFieldFeatures();
			iElement.setFeatures(ASPECT_NAME, features);
		}

		readAnnotation(iElement, iAnnotation, features);
	}

	private void readAnnotation(SchemaClassElement iElement, Annotation iAnnotation, DynaBean features) {

		if (iAnnotation instanceof LoggingAction) {

			LoggingAction annotation = (LoggingAction) iAnnotation;

			if (annotation != null) {
				// PROCESS ANNOTATIONS
				// ANNOTATION ATTRIBUTES (IF DEFINED) OVERWRITE DEFAULT VALUES
				if (annotation != null) {
					if (annotation.level() != Integer.MIN_VALUE) {
						features.setAttribute(LoggingActionFeatures.LEVEL, new Integer(annotation.level()));
					}
					if (!AnnotationConstants.DEF_VALUE.equals(annotation.category())) {
						features.setAttribute(LoggingActionFeatures.CATEGORY, annotation.category());
					}
					if (!AnnotationConstants.DEF_VALUE.equals(annotation.category())) {
						features.setAttribute(LoggingActionFeatures.MODE, annotation.mode());
					}
					if (annotation.exception().length() > 0) {
						features.setAttribute(LoggingActionFeatures.EXCEPTIONS_TO_LOG, annotation.exceptionsToLog());
					}
					if (AnnotationConstants.TRUE == annotation.enabled()) {
						features.setAttribute(LoggingActionFeatures.ENABLED, Boolean.TRUE);
					} else if (AnnotationConstants.FALSE == annotation.enabled()) {
						features.setAttribute(LoggingActionFeatures.ENABLED, Boolean.FALSE);
					}

					if (!annotation.post().equals(AnnotationConstants.DEF_VALUE)) {
						features.setAttribute(LoggingActionFeatures.POST, annotation.post());
					}
					if (!annotation.exception().equals(AnnotationConstants.DEF_VALUE)) {
						features.setAttribute(LoggingActionFeatures.EXCEPTION, annotation.exception());
					}

					if (!annotation.pre().equals(AnnotationConstants.DEF_VALUE)) {
						features.setAttribute(LoggingActionFeatures.PRE, annotation.pre());
					}
				}
			}

		}

		if (iAnnotation instanceof LoggingField) {

			LoggingField annotation = (LoggingField) iAnnotation;

			if (annotation != null) {
				// PROCESS ANNOTATIONS
				// ANNOTATION ATTRIBUTES (IF DEFINED) OVERWRITE DEFAULT VALUES
				if (annotation != null) {
					if (annotation.level() != Integer.MIN_VALUE) {
						features.setAttribute(LoggingFieldFeatures.LEVEL, new Integer(annotation.level()));
					}
					if (!AnnotationConstants.DEF_VALUE.equals(annotation.category())) {
						features.setAttribute(LoggingFieldFeatures.CATEGORY, annotation.category());
					}
					if (!AnnotationConstants.DEF_VALUE.equals(annotation.category())) {
						features.setAttribute(LoggingActionFeatures.MODE, annotation.mode());
					}
					if (annotation.exception().length() > 0) {
						features.setAttribute(LoggingFieldFeatures.EXCEPTIONS_TO_LOG, annotation.exceptionsToLog());
					}
					if (AnnotationConstants.TRUE == annotation.enabled()) {
						features.setAttribute(LoggingActionFeatures.ENABLED, Boolean.TRUE);
					} else if (AnnotationConstants.FALSE == annotation.enabled()) {
						features.setAttribute(LoggingFieldFeatures.ENABLED, Boolean.FALSE);
					}

					if (!annotation.post().equals(AnnotationConstants.DEF_VALUE)) {
						features.setAttribute(LoggingFieldFeatures.POST, annotation.post());
					}
					if (!annotation.exception().equals(AnnotationConstants.DEF_VALUE)) {
						features.setAttribute(LoggingFieldFeatures.EXCEPTION, annotation.exception());
					}

				}
			}

		}

	}

	private void readFieldXml(SchemaClassElement iElement, XmlFieldAnnotation iXmlNode) {
		// PROCESS DESCRIPTOR CFG DESCRIPTOR ATTRIBUTES (IF DEFINED) OVERWRITE DEFAULT AND ANNOTATION VALUES
		if (iXmlNode == null || iXmlNode.aspect(ASPECT_NAME) == null) {
			return;
		}

		DynaBean features = iElement.getFeatures(ASPECT_NAME);

		XmlAspectAnnotation descriptor = iXmlNode.aspect(ASPECT_NAME);

		configureFieldXml(features, descriptor);
	}

	private void configureFieldXml(DynaBean features, XmlAspectAnnotation descriptor) {
		if (descriptor != null) {
			String level = descriptor.getAttribute(LoggingFieldFeatures.LEVEL);
			if (level != null) {
				features.setAttribute(LoggingFieldFeatures.LEVEL, new BigInteger(level));// biginteger
			}
			String category = descriptor.getAttribute(LoggingFieldFeatures.CATEGORY);
			if (category != null) {
				features.setAttribute(LoggingFieldFeatures.CATEGORY, category);// string
			}
			String mode = descriptor.getAttribute(LoggingFieldFeatures.MODE);
			if (mode != null) {
				features.setAttribute(LoggingFieldFeatures.MODE, mode);// string
			}
			String post = descriptor.getAttribute(LoggingFieldFeatures.POST);
			if (post != null) {
				features.setAttribute(LoggingFieldFeatures.POST, post);// string
			}
			String exception = descriptor.getAttribute(LoggingFieldFeatures.EXCEPTION);
			if (exception != null) {
				features.setAttribute(LoggingFieldFeatures.EXCEPTION, exception);// string
			}
			String enabled = descriptor.getAttribute(LoggingFieldFeatures.ENABLED);
			if (enabled != null) {
				features.setAttribute(LoggingFieldFeatures.ENABLED, new Boolean(enabled));// boolean
			}

			// XmlConfigExceptionsToLogType exceptionsToLog = descriptor.getExceptionsToLog();
			// if (exceptionsToLog != null) {
			// configureExceptionsToLog(features, exceptionsToLog);
			// }
			String exceptionsToLog = descriptor.getAttribute(LoggingFieldFeatures.EXCEPTIONS_TO_LOG);
			if (exceptionsToLog != null) {
				configureExceptionsToLog(features, exceptionsToLog);
			}
		}

	}

	private void readActionXml(SchemaClassElement iElement, XmlActionAnnotation iXmlNode) {
		// PROCESS DESCRIPTOR CFG DESCRIPTOR ATTRIBUTES (IF DEFINED) OVERWRITE DEFAULT AND ANNOTATION VALUES
		if (iXmlNode == null || iXmlNode.aspect(ASPECT_NAME) == null) {
			return;
		}

		DynaBean features = iElement.getFeatures(ASPECT_NAME);

		XmlAspectAnnotation descriptor = iXmlNode.aspect(ASPECT_NAME);

		configureActionXml(features, descriptor);
	}

	private void configureActionXml(DynaBean features, XmlAspectAnnotation descriptor) {
		if (descriptor != null) {
			String level = descriptor.getAttribute(LoggingActionFeatures.LEVEL);
			if (level != null) {
				features.setAttribute(LoggingActionFeatures.LEVEL, new BigInteger(level));// biginteger
			}
			String category = descriptor.getAttribute(LoggingActionFeatures.CATEGORY);
			if (category != null) {
				features.setAttribute(LoggingActionFeatures.CATEGORY, category);// string
			}
			String mode = descriptor.getAttribute(LoggingActionFeatures.MODE);
			if (mode != null) {
				features.setAttribute(LoggingActionFeatures.MODE, mode);// string
			}
			String post = descriptor.getAttribute(LoggingActionFeatures.POST);
			if (post != null) {
				features.setAttribute(LoggingActionFeatures.POST, post);// string
			}
			String exception = descriptor.getAttribute(LoggingActionFeatures.EXCEPTION);
			if (exception != null) {
				features.setAttribute(LoggingActionFeatures.EXCEPTION, exception);// string
			}
			String pre = descriptor.getAttribute(LoggingActionFeatures.PRE);
			if (pre != null) {
				features.setAttribute(LoggingActionFeatures.ENABLED, pre);// string
			}
			String enabled = descriptor.getAttribute(LoggingActionFeatures.ENABLED);
			if (enabled != null) {
				features.setAttribute(LoggingActionFeatures.ENABLED, new Boolean(enabled));// boolean
			}

			// XmlConfigExceptionsToLogType exceptionsToLog = descriptor.getExceptionsToLog();
			// if (exceptionsToLog != null) {
			// configureExceptionsToLog(features, exceptionsToLog);
			// }
			String exceptionsToLog = descriptor.getAttribute(LoggingActionFeatures.EXCEPTIONS_TO_LOG);
			if (exceptionsToLog != null) {
				configureExceptionsToLog(features, exceptionsToLog);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void configureExceptionsToLog(DynaBean features, String exceptionsToLog) {
		String[] nameList = exceptionsToLog.split(",");
		if (nameList != null) {
			Class[] classes = new Class[nameList.length];
			int i = 0;
			for (String exception : nameList) {
				try {
					classes[i] = Class.forName(exception.trim());
				} catch (Exception e) {
					if (log.isErrorEnabled()) {
						log.error("[" + Utility.getClassName(getClass()) + "]: cannot initialize class " + exception);
					}
				}
				i++;
			}
			features.setAttribute(LoggingActionFeatures.EXCEPTIONS_TO_LOG, classes);
		}
	}

	public void configEvent(SchemaEvent iEvent, Annotation iEventAnnotation, Annotation iGenericAnnotation,
			XmlEventAnnotation iXmlNode) {
		configAction(iEvent, iEventAnnotation, iGenericAnnotation, iXmlNode);
	}

	public String aspectName() {
		return ASPECT_NAME;
	}
}
