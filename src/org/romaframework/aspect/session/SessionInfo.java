package org.romaframework.aspect.session;

import java.util.Date;

import org.romaframework.aspect.core.annotation.CoreClass;

/**
 * Object to store session data
 *
 */
@CoreClass(orderFields = "id source account created userAgent")
public class SessionInfo {

	private Object					id;
	private String					source;
	private SessionAccount	account;
	private Date						created;
	private Object					systemSession;
	private String					userAgent;

	
	/**
	 * Instantiates the object, 
	 * with a specific ID, and automatically specifies the date and time at which it was created
	 * @param iId
	 */
	public SessionInfo(Object iId) {
		created = new Date();
		id = iId;
	}

	/**
	 * 
	 * @return SessionAccount
	 */
	public SessionAccount getAccount() {
		return account;
	}

	/**
	 * 
	 * @param account
	 */
	public void setAccount(SessionAccount account) {
		this.account = account;
	}

	/**
	 * Returns the date on which the object was created
	 * 
	 * @return Date
	 */
	public Date getCreated() {
		return created;
	}

	/**
	 * overwrites, date the object was created
	 * 
	 * @param loggedOn
	 */
	public void setCreated(Date loggedOn) {
		this.created = loggedOn;
	}

	/**
	 * 
	 * @return Object
	 */
	public Object getSystemSession() {
		return systemSession;
	}

	/**
	 * 
	 * @param systemSession
	 */
	public void setSystemSession(Object systemSession) {
		this.systemSession = systemSession;
	}

	/**
	 * Returns the object's id
	 * 
	 * @return
	 */
	public Object getId() {
		return id;
	}

	/**
	 * Set a new ID
	 * 
	 * @param id
	 */
	public void setId(Object id) {
		this.id = id;
	}

	/**
	 * 
	 * @param iSource
	 */
	public void setSource(String iSource) {
		source = iSource;
	}

	
	/**
	 * 
	 * @return String
	 */
	public String getSource() {
		return source;
	}

	/**
	 * 
	 * @return String
	 */
	public String getUserAgent() {
		return userAgent;
	}

	/**
	 * 
	 * @param userAgent
	 */
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
}
