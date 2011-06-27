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
package org.romaframework.aspect.session.impl.app;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;

import org.romaframework.aspect.session.SessionAccount;
import org.romaframework.aspect.session.SessionAspectAbstract;
import org.romaframework.aspect.session.SessionInfo;

/**
 * Applicative Session Aspect implementation. It stores in memory session data in order to get working with Client/Desktop
 * applications. <br/>
 * Use this also for client/server applications using the ServiceAspect. <br/>
 * It uses AppSessionInfo objects to store session information.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 * @see AppSessionInfo
 * 
 */
public class AppSessionManager extends SessionAspectAbstract {
	protected HashMap<Long, SessionInfo>	sessions			= new HashMap<Long, SessionInfo>();
	protected long												lastSessionId	= -1;

	public SessionInfo addSession(Object session) {
		AppSessionInfo sess = new AppSessionInfo(++lastSessionId);
		sess.setAccount((SessionAccount) session);
		sessions.put(lastSessionId, sess);
		return sess;
	}

	public void destroyCurrentSession() {
	}

	public void destroyCurrentSession(Object systemSession) {
	}

	public Locale getActiveLocale() {
		return null;
	}

	public Object getActiveSystemSession() {
		return null;
	}

	public SessionInfo getActiveSessionInfo() {
		return null;
	}

	@SuppressWarnings("unchecked")
	public <T> T getProperty(Object session, String key) {
		AppSessionInfo sess = (AppSessionInfo) sessions.get(session);
		if (sess == null)
			return null;
		return (T) sess.getProperty(key);
	}

	public <T> T getProperty(String key) {
		return null;
	}

	public SessionInfo getSession(Object systemSession) {
		return sessions.get(systemSession);
	}

	public Collection<SessionInfo> getSessionInfos() {
		return sessions.values();
	}

	public SessionInfo removeSession(Object session) {
		return sessions.remove(session);
	}

	public <T> void setProperty(Object session, String key, T value) {
		AppSessionInfo sess = (AppSessionInfo) sessions.get(session);
		if (sess == null)
			return;
		sess.setProperty(key, value);
	}

	public <T> void setProperty(String key, T value) {
	}

	public void setActiveLocale(Locale locale) {
	}

	public Object getUnderlyingComponent() {
		return null;
	}
}
