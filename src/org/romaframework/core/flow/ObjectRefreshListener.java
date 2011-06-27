package org.romaframework.core.flow;

import org.romaframework.aspect.session.SessionInfo;

public interface ObjectRefreshListener {

	/**
	 * Refresh of an full object.
	 * 
	 * @param iSession
	 *          session to refresh.
	 * @param iContent
	 *          to refresh
	 */
	public void onObjectRefresh(SessionInfo iSession, Object iContent);
	
}
