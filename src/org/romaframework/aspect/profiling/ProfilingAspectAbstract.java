/*
 * Copyright 2006-2008 Luca Garulli (luca.garulli--at--assetdata.it)
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

package org.romaframework.aspect.profiling;

import java.lang.annotation.Annotation;

import org.romaframework.aspect.profiling.annotation.ProfilingAction;
import org.romaframework.aspect.profiling.annotation.ProfilingClass;
import org.romaframework.aspect.profiling.feature.ProfilingActionFeatures;
import org.romaframework.aspect.profiling.feature.ProfilingClassFeatures;
import org.romaframework.core.module.SelfRegistrantConfigurableModule;
import org.romaframework.core.schema.SchemaClassDefinition;
import org.romaframework.core.schema.SchemaClassElement;
import org.romaframework.core.schema.SchemaEvent;
import org.romaframework.core.schema.xmlannotations.XmlAspectAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlActionAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlClassAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlEventAnnotation;
import org.romaframework.core.util.DynaBean;

/**
 * Abstract implementation for Profiling Aspect.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public abstract class ProfilingAspectAbstract extends SelfRegistrantConfigurableModule<String> implements ProfilingAspect {

  public void configClass(SchemaClassDefinition iClass, Annotation iAnnotation, XmlClassAnnotation iXmlNode) {
    DynaBean features = iClass.getFeatures(ProfilingAspect.ASPECT_NAME);
    if (features == null) {
      // CREATE EMPTY FEATURES
      features = new ProfilingClassFeatures();
      iClass.setFeatures(ProfilingAspect.ASPECT_NAME, features);
    }

    readClassAnnotation(iAnnotation, features);
    readClassXml(iClass, iXmlNode);
  }

  public void configAction(SchemaClassElement iAction, Annotation iActionAnnotation, Annotation iGenericAnnotation,
		  XmlActionAnnotation iXmlNode) {
    configCommonAnnotations(iAction, iActionAnnotation, iGenericAnnotation);
    readActionXml(iAction, iXmlNode);
  }

  private void readClassAnnotation(Annotation iAnnotation, DynaBean features) {
    ProfilingClass annotation = (ProfilingClass) iAnnotation;
    if (annotation != null) {
      // PROCESS ANNOTATIONS
      features.setAttribute(ProfilingClassFeatures.ENABLED, annotation.enabled());
    }
  }

  private void readClassXml(SchemaClassDefinition iClass, XmlClassAnnotation iXmlNode) {
    if (iXmlNode == null || iXmlNode.aspect(ASPECT_NAME) == null)
      return;

    DynaBean features = iClass.getFeatures(ProfilingAspect.ASPECT_NAME);

    XmlAspectAnnotation featureDescriptor = iXmlNode.aspect(ASPECT_NAME);

    if (featureDescriptor != null) {
      String enabled = featureDescriptor.getAttribute(ProfilingClassFeatures.ENABLED);
      if(enabled!=null){
        features.setAttribute(ProfilingClassFeatures.ENABLED, new Boolean(enabled));
      }

    }
  }

  private void configCommonAnnotations(SchemaClassElement iElement, Annotation iAnnotation, Annotation iGenericAnnotation) {
    DynaBean features = iElement.getFeatures(ASPECT_NAME);
    if (features == null) {
      // CREATE EMPTY FEATURES
      features = new ProfilingActionFeatures();
      iElement.setFeatures(ASPECT_NAME, features);
    }

    readAnnotation(iElement, iGenericAnnotation, features);
    readAnnotation(iElement, iAnnotation, features);
  }

  private void readAnnotation(SchemaClassElement iElement, Annotation iAnnotation, DynaBean features) {
    ProfilingAction annotation = (ProfilingAction) iAnnotation;

    if (annotation != null) {
      // PROCESS ANNOTATIONS
      // ANNOTATION ATTRIBUTES (IF DEFINED) OVERWRITE DEFAULT VALUES
      if (annotation != null) {
        features.setAttribute(ProfilingActionFeatures.ENABLED, annotation.enabled());
        features.setAttribute(ProfilingActionFeatures.KEY, annotation.key());
      }
    }
  }

  private void readActionXml(SchemaClassElement iElement, XmlActionAnnotation iXmlNode) {
    // PROCESS DESCRIPTOR CFG DESCRIPTOR ATTRIBUTES (IF DEFINED) OVERWRITE DEFAULT AND ANNOTATION VALUES
    if (iXmlNode == null || iXmlNode.aspect(ASPECT_NAME) == null)
      return;

    DynaBean features = iElement.getFeatures(ASPECT_NAME);

    XmlAspectAnnotation descriptor = iXmlNode.aspect(ASPECT_NAME);

    if (descriptor != null) {
      String enabled = descriptor.getAttribute(ProfilingActionFeatures.ENABLED);
      if(enabled != null){
    	  features.setAttribute(ProfilingActionFeatures.ENABLED, new Boolean(enabled));
      }
      String key = descriptor.getAttribute(ProfilingActionFeatures.KEY);
      if(key!=null){
    	  features.setAttribute(ProfilingActionFeatures.KEY, key);
      }
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
