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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.romaframework.aspect.core.annotation.CoreClass;
import org.romaframework.aspect.core.feature.CoreClassFeatures;
import org.romaframework.aspect.core.feature.CoreFieldFeatures;
import org.romaframework.core.Roma;
import org.romaframework.core.Utility;
import org.romaframework.core.aspect.Aspect;
import org.romaframework.core.classloader.ClassLoaderListener;
import org.romaframework.core.config.ApplicationConfiguration;
import org.romaframework.core.config.RomaApplicationListener;
import org.romaframework.core.config.Serviceable;
import org.romaframework.core.domain.entity.ComposedEntity;
import org.romaframework.core.flow.Controller;
import org.romaframework.core.module.SelfRegistrantModule;
import org.romaframework.core.resource.AutoReloadManager;
import org.romaframework.core.schema.FeatureType;
import org.romaframework.core.schema.SchemaAction;
import org.romaframework.core.schema.SchemaClass;
import org.romaframework.core.schema.SchemaClassDefinition;
import org.romaframework.core.schema.SchemaClassResolver;
import org.romaframework.core.schema.SchemaEvent;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.schema.SchemaHelper;
import org.romaframework.core.schema.reflection.SchemaActionDelegate;
import org.romaframework.core.schema.reflection.SchemaEventDelegate;
import org.romaframework.core.schema.reflection.SchemaFieldDelegate;
import org.romaframework.core.schema.reflection.SchemaFieldReflection;

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

		for (Aspect aspect : Roma.aspects()) {
			String clBaseName = Utility.ROMA_PACKAGE + ".aspect." + aspect.aspectName() + ".feature." + Utility.getCapitalizedString(aspect.aspectName());
			try {
				Class.forName(clBaseName + "Features");
			} catch (Exception e) {
				log.error(e);
			}
			for (FeatureType type : FeatureType.values()) {
				String name = clBaseName + type.getBaseName() + "Features";
				try {
					// if (aspect.getClass().getClassLoader().getResource(name + ".class") != null)
					Class.forName(name);
				} catch (Exception e) {
					log.error(e);
				}
			}

		}

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

	public void configClass(SchemaClassDefinition iClass) {

		if (iClass.isSettedFeature(CoreClassFeatures.ENTITY) && iClass.getSchemaClass().isComposedEntity() && iClass.getField(ComposedEntity.NAME) != null) {
			iClass.getField(ComposedEntity.NAME).setType(iClass.getFeature(CoreClassFeatures.ENTITY));
		}
	}

	private void expandField(SchemaField iField) {
		SchemaClass cl = iField.getType().getSchemaClass();
		//iField.getEntity().getFields().remove(iField.getName());
		Iterator<SchemaField> fields = cl.getFieldIterator();
		while (fields.hasNext()) {
			SchemaField sf = fields.next();
			SchemaFieldDelegate sfd = new SchemaFieldDelegate(iField.getEntity(), iField, sf);
			sfd.configure();
			iField.getEntity().setField(sf.getName(), sfd);
		}
		Iterator<SchemaAction> actions = cl.getActionIterator();
		while (actions.hasNext()) {
			SchemaAction sa = actions.next();
			SchemaActionDelegate sad = new SchemaActionDelegate(iField.getEntity(), iField, sa);
			sad.configure();
			iField.getEntity().setAction(sa.getName(), sad);
		}
		Iterator<SchemaEvent> events = cl.getEventIterator();
		while (events.hasNext()) {
			SchemaEvent se = events.next();
			SchemaEventDelegate sed = new SchemaEventDelegate(iField.getEntity(), iField, se);
			sed.configure();
			iField.getEntity().setEvent(se.getName(), sed);
		}

	}

	public void configField(SchemaField iField) {

		if (iField.getFeature(CoreFieldFeatures.EXPAND)) {
			expandField(iField);
		}
		if (iField instanceof SchemaFieldReflection) {

			SchemaFieldReflection ref = (SchemaFieldReflection) iField;
			SchemaClass[] embeddedTypeGenerics = null;
			Type embType = null;
			if (ref.getGetterMethod() != null) {
				if ((embType = assignEmbeddedType(ref, ref.getGetterMethod().getGenericReturnType())) == null)
					embType = assignEmbeddedType(ref, ref.getGetterMethod().getReturnType());
			}

			if (embType == null && ref.getField() != null)
				embType = assignEmbeddedType(ref, ref.getField().getGenericType());

			if (embType != null && embType instanceof ParameterizedType) {
				ParameterizedType pt = (ParameterizedType) embType;
				if (pt.getActualTypeArguments() != null) {
					embeddedTypeGenerics = new SchemaClass[pt.getActualTypeArguments().length];
					int i = 0;
					for (Type argType : pt.getActualTypeArguments())
						if (argType instanceof Class<?>)
							embeddedTypeGenerics[i++] = Roma.schema().getSchemaClassIfExist((Class<?>) argType);
				}
				ref.setEmbeddedTypeGenerics(embeddedTypeGenerics);
			}
		}
	}

	/**
	 * Get embedded type using Generics Reflection.
	 * 
	 * @param iType
	 *          Type to check
	 * @return true if an embedded type was found, otherwise false
	 */
	private Type assignEmbeddedType(SchemaFieldReflection ref, Type iType) {
		// CHECK FOR ARRAY
		if (iType instanceof Class<?>) {
			Class<?> cls = (Class<?>) iType;
			if (cls.isArray())
				ref.setEmbeddedLanguageType(cls.getComponentType());
		}

		Class<?> embClass = SchemaHelper.getGenericClass(iType);
		if (embClass != null) {
			ref.setEmbeddedLanguageType(embClass);
		}

		return embClass != null ? iType : null;
	}

	public void configAction(SchemaAction iAction) {
	}

	public void configEvent(SchemaEvent event) {
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
