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

package org.romaframework.aspect.core;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.core.annotation.CoreClass;
import org.romaframework.aspect.core.annotation.CoreField;
import org.romaframework.aspect.core.feature.CoreClassFeatures;
import org.romaframework.aspect.core.feature.CoreFieldFeatures;
import org.romaframework.core.Roma;
import org.romaframework.core.Utility;
import org.romaframework.core.aspect.Aspect;
import org.romaframework.core.classloader.ClassLoaderListener;
import org.romaframework.core.config.ApplicationConfiguration;
import org.romaframework.core.config.RomaApplicationListener;
import org.romaframework.core.config.Serviceable;
import org.romaframework.core.flow.Controller;
import org.romaframework.core.module.SelfRegistrantModule;
import org.romaframework.core.resource.AutoReloadManager;
import org.romaframework.core.schema.SchemaClassDefinition;
import org.romaframework.core.schema.SchemaClassResolver;
import org.romaframework.core.schema.SchemaClassElement;
import org.romaframework.core.schema.SchemaEvent;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.schema.SchemaHelper;
import org.romaframework.core.schema.xmlannotations.XmlActionAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlAspectAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlClassAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlEventAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlFieldAnnotation;
import org.romaframework.core.util.DynaBean;

public class CoreAspect extends SelfRegistrantModule implements Aspect, RomaApplicationListener, ClassLoaderListener {

	public static final String	ASPECT_NAME							= "core";

	protected List<Class<?>>		classesToBeLoadedEarly	= new ArrayList<Class<?>>();						;

	protected static Log				log											= LogFactory.getLog(CoreAspect.class);

	public CoreAspect() {
		Controller.getInstance().registerListener(RomaApplicationListener.class, this);
		Controller.getInstance().registerListener(ClassLoaderListener.class, this);
	}

	public void startup() {
		if (status == STATUS_STARTING || status == STATUS_UP)
			return;

		status = STATUS_STARTING;

		SchemaClassResolver classResolver = Roma.component(SchemaClassResolver.class);

		// REGISTER THE APPLICATION DOMAIN AS FIRST ONE PATH
		classResolver.addDomainPackage(Roma.component(ApplicationConfiguration.class).getApplicationPackage());
		classResolver.addDomainPackage(Utility.ROMA_PACKAGE + Utility.PACKAGE_SEPARATOR + "core");
		classResolver.addPackage("java.lang");

		status = STATUS_UP;
	}

	public void shutdown() {
		Roma.component(AutoReloadManager.class).shutdown();
		Roma.component(SchemaClassResolver.class).shutdown();
	}

	public void beginConfigClass(SchemaClassDefinition iClass) {
	}

	public void endConfigClass(SchemaClassDefinition iClass) {
	}

	public void configClass(SchemaClassDefinition iClass, Annotation iAnnotation, XmlClassAnnotation iXmlNode) {
		DynaBean features = iClass.getFeatures(ASPECT_NAME);
		if (features == null) {
			// CREATE EMPTY FEATURES
			features = new CoreClassFeatures();
			iClass.setFeatures(ASPECT_NAME, features);
		}

		if (iClass.getSchemaClass().isComposedEntity()) {
			features.setAttribute(CoreClassFeatures.ENTITY, SchemaHelper.getSuperclassGenericType(iClass));
		}

		readClassAnnotation(iClass, iAnnotation, features);
		readClassXml(iClass, iXmlNode);
	}

	protected void readClassAnnotation(SchemaClassDefinition iClass, Annotation iAnnotation, DynaBean features) {
		CoreClass annotation = (CoreClass) iAnnotation;

		if (annotation != null) {
			// PROCESS ANNOTATIONS
			// ANNOTATION ATTRIBUTES (IF DEFINED) OVERWRITE DEFAULT VALUES
			if (annotation != null) {
				if (annotation.entity() != Object.class)
					features.setAttribute(CoreClassFeatures.ENTITY, Roma.schema().getSchemaClass(annotation.entity()));
				if (annotation.orderFields() != AnnotationConstants.DEF_VALUE)
					features.setAttribute(CoreClassFeatures.ORDER_FIELDS, annotation.orderFields());
				if (annotation.orderActions() != AnnotationConstants.DEF_VALUE)
					features.setAttribute(CoreClassFeatures.ORDER_ACTIONS, annotation.orderActions());
			}
		}
	}

	protected void readClassXml(SchemaClassDefinition iClass, XmlClassAnnotation iXmlNode) {
		// PROCESS DESCRIPTOR CFG
		// DESCRIPTOR ATTRIBUTES (IF DEFINED) OVERWRITE DEFAULT AND ANNOTATION
		// VALUES
		if (iXmlNode == null || iXmlNode.aspect(CoreAspect.ASPECT_NAME) == null)
			return;

		DynaBean features = iClass.getFeatures(ASPECT_NAME);

		XmlAspectAnnotation descriptor = iXmlNode.aspect(CoreAspect.ASPECT_NAME);

		if (descriptor != null) {
			String entity = descriptor.getAttribute(CoreClassFeatures.ENTITY);
			if (entity != null)
				features.setAttribute(CoreClassFeatures.ENTITY, Roma.schema().getSchemaClass(entity));
		}
	}

	public void configField(SchemaField iField, Annotation iFieldAnnotation, Annotation iGenericAnnotation,
			Annotation iGetterAnnotation, XmlFieldAnnotation iXmlNode) {
		DynaBean features = iField.getFeatures(ASPECT_NAME);
		if (features == null) {
			// CREATE EMPTY FEATURES
			features = new CoreFieldFeatures();
			iField.setFeatures(ASPECT_NAME, features);
		}

		readFieldAnnotation(iField, iFieldAnnotation, features);
		readFieldAnnotation(iField, iGetterAnnotation, features);
		readFieldXml(iField, iXmlNode);
		setFieldDefaults(iField);
	}

	public void configAction(SchemaClassElement iAction, Annotation iActionAnnotation, Annotation iGenericAnnotation,
			XmlActionAnnotation iXmlNode) {
	}

	protected void readFieldAnnotation(SchemaField iField, Annotation iAnnotation, DynaBean features) {
		CoreField annotation = (CoreField) iAnnotation;

		if (annotation != null) {
			// PROCESS ANNOTATIONS
			// ANNOTATION ATTRIBUTES (IF DEFINED) OVERWRITE DEFAULT VALUES
			if (annotation != null) {
				if (!"Object".equals(annotation.embeddedType()))
					features.setAttribute(CoreFieldFeatures.EMBEDDED_TYPE, Roma.schema().getSchemaClass(annotation.embeddedType()));
				if (annotation.embedded() != AnnotationConstants.UNSETTED)
					features.setAttribute(CoreFieldFeatures.EMBEDDED, annotation.embedded() == AnnotationConstants.TRUE);
				if (annotation.useRuntimeType() != AnnotationConstants.UNSETTED)
					features.setAttribute(CoreFieldFeatures.USE_RUNTIME_TYPE, annotation.useRuntimeType() == AnnotationConstants.TRUE);
			}
		}
	}

	protected void readFieldXml(SchemaField iField, XmlFieldAnnotation iXmlNode) {
		// PROCESS DESCRIPTOR CFG
		// DESCRIPTOR ATTRIBUTES (IF DEFINED) OVERWRITE DEFAULT AND ANNOTATION
		// VALUES
		if (iXmlNode == null || iXmlNode.aspect(CoreAspect.ASPECT_NAME) == null)
			return;

		DynaBean features = iField.getFeatures(ASPECT_NAME);

		XmlAspectAnnotation descriptor = iXmlNode.aspect(CoreAspect.ASPECT_NAME);

		if (descriptor != null) {
			String embeddedType = descriptor.getAttribute(CoreFieldFeatures.EMBEDDED_TYPE);
			if (embeddedType != null) {
				features.setAttribute(CoreFieldFeatures.EMBEDDED_TYPE, Roma.schema().getSchemaClass(embeddedType));
			}

			String embedded = descriptor.getAttribute(CoreFieldFeatures.EMBEDDED);
			if (embedded != null) {
				try {
					features.setAttribute(CoreFieldFeatures.EMBEDDED, Boolean.parseBoolean(embedded));
				} catch (Exception e) {
					log.warn("invalid value for CoreField - " + CoreFieldFeatures.EMBEDDED + " = " + embedded);
				}
			}
			String useRuntimeType = descriptor.getAttribute(CoreFieldFeatures.USE_RUNTIME_TYPE);
			boolean useRT = false;
			if (useRuntimeType != null) {
				try {
					useRT = Boolean.parseBoolean(useRuntimeType);
				} catch (Exception e) {
					log.warn("invalid value for CoreField - " + CoreFieldFeatures.USE_RUNTIME_TYPE + " = " + useRuntimeType);
				}
			}
			features.setAttribute(CoreFieldFeatures.USE_RUNTIME_TYPE, useRT);// boolean
		}
	}

	protected void setFieldDefaults(SchemaField field) {
	}

	public void configEvent(SchemaEvent event, Annotation annotation, Annotation iGenericAnnotation, XmlEventAnnotation node) {
	}

	public String aspectName() {
		return ASPECT_NAME;
	}

	@Override
	public String getStatus() {
		return Serviceable.STATUS_UP;
	}

	public String moduleName() {
		return aspectName();
	}

	public Object getUnderlyingComponent() {
		return null;
	}

	public void onBeforeStartup() {
		Roma.context().create();
	}

	public void onAfterStartup() {
		for (Class<?> cls : classesToBeLoadedEarly)
			Roma.schema().getSchemaClass(cls);
		Roma.context().destroy();
	}

	public void onBeforeShutdown() {
		Roma.context().create();
	}

	public void onAfterShutdown() {
		Roma.context().destroy();
	}

	/**
	 * Register the class to be loaded early
	 */
	public void onClassLoading(Class<?> iClass) {
		CoreClass ann = iClass.getAnnotation(CoreClass.class);
		if (ann != null && ann.loading() == CoreClass.LOADING_MODE.EARLY)
			classesToBeLoadedEarly.add(iClass);
	}
}
