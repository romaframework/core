package org.romaframework.core.flow;

import org.romaframework.aspect.session.SessionInfo;
import org.romaframework.core.schema.SchemaField;

/**
 * Listener of a field update.
 * 
 */
public interface FieldRefreshListener {

	/**
	 * Invoked when a field is changed outside Roma, usually from the user side.
	 * 
	 * @param iSession
	 *          User's session
	 * @param iContent
	 *          POJO refreshed
	 * @param iField
	 *          Field refreshed
	 */
	public void onFieldRefresh(SessionInfo iSession, Object iContent, SchemaField iField);
}
