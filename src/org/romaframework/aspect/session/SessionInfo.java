package org.romaframework.aspect.session;

import java.util.Date;

import org.romaframework.aspect.core.annotation.CoreClass;

@CoreClass(orderFields = "id source account created userAgent")
public class SessionInfo {
	private Object					id;
	private String					source;
	private SessionAccount	account;
	private Date						created;
	private Object					systemSession;
	private String					userAgent;

	public SessionInfo(Object iId) {
		created = new Date();
		id = iId;
	}

	public SessionAccount getAccount() {
		return account;
	}

	public void setAccount(SessionAccount account) {
		this.account = account;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date loggedOn) {
		this.created = loggedOn;
	}

	public Object getSystemSession() {
		return systemSession;
	}

	public void setSystemSession(Object systemSession) {
		this.systemSession = systemSession;
	}

	public Object getId() {
		return id;
	}

	public void setId(Object id) {
		this.id = id;
	}

	public void setSource(String iSource) {
		source = iSource;
	}

	public String getSource() {
		return source;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
}
