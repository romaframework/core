/*
 * Copyright 2006-2007 Luca Garulli (luca.garulli--at--assetdata.it)
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
package org.romaframework.aspect.persistence;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.romaframework.aspect.persistence.feature.PersistenceFeatures;
import org.romaframework.core.Roma;
import org.romaframework.core.flow.ContextLifecycleListener;
import org.romaframework.core.flow.Controller;
import org.romaframework.core.flow.ObjectContext;
import org.romaframework.core.flow.SchemaActionListener;
import org.romaframework.core.schema.SchemaAction;

/**
 * Inject TxPersistenceAspect object inside Thread Local Context to be reachable by actions.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public class PersistenceContextInjector implements ContextLifecycleListener, SchemaActionListener {
	private static Log	log	= LogFactory.getLog(PersistenceContextInjector.class);

	public PersistenceContextInjector() {
		Controller.getInstance().registerListener(SchemaActionListener.class, this);
		Controller.getInstance().registerListener(ContextLifecycleListener.class, this);
	}

	public void onContextCreate() {
	}

	public void onContextDestroy() {
		removePersistenceAspectFromContext();
	}

	public Object onContextRead(String iComponentName, Object iComponentInstance) {
		if (iComponentInstance == null && iComponentName.toString().equals("PersistenceAspect")) {
			if (!Roma.context().isCreated())
				throw new PersistenceException(
						"Can't access to the PersistenceAspect component in the context because no one has started the transaction. Probably you're accessing from outside Roma controller. Please use Roma.persistence() in this case.");

			return setPersistenceAspectInContext(PersistenceConstants.MODE_TX);
		}
		return null;
	}

	public void onAfterAction(Object iContent, SchemaAction iAction, Object returnedValue) {
	}

	public boolean onBeforeAction(Object iContent, SchemaAction iAction) {

		// TODO:Review the plug of transaction mode.
		if (!ObjectContext.getInstance().existContextComponent(PersistenceAspect.class)) {
			String mode = (String) iAction.getFeature(PersistenceFeatures.MODE);
			if (mode != null) {
				setPersistenceAspectInContext(mode);
			}
		}

		return true;
	}

	public void onExceptionAction(Object iContent, SchemaAction iAction, Exception exception) {
	}

	protected PersistenceAspect setPersistenceAspectInContext(String mode) {
		PersistenceAspect pa = PersistenceAspectAbstract.getPersistenceComponent(mode);

		ObjectContext.getInstance().setContextComponent(PersistenceAspect.class, pa);

		if (log.isDebugEnabled())
			log.debug("[PersistenceContextInjector.setPersistenceAspectInContext] mode: " + mode);

		return pa;
	}

	private void removePersistenceAspectFromContext() {
		if (!ObjectContext.getInstance().existContextComponent(PersistenceAspect.class))
			return;

		PersistenceAspect pa = ObjectContext.getInstance().getContextComponent(PersistenceAspect.class);
		if (pa != null) {
			if (log.isDebugEnabled()) {
				log.debug("[PersistenceContextInjector.removePersistenceAspectFromContext]");
			}

			ObjectContext.getInstance().setContextComponent(PersistenceAspect.class, null);
			try {
				// REMOVE PREVIOUS PERSISTENCE ASPECT BOUND
				if (pa.isActive()) {
					pa.commit();
				}
			} finally {
				pa.close();
			}
		}
	}
}
