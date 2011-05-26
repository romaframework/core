package org.romaframework.core.command;

import org.romaframework.aspect.persistence.PersistenceAspect;

public class TransactionalCommandContext extends CommandContext {
	private static final long	serialVersionUID	= 2475254708437401436L;

	public TransactionalCommandContext(long id) {
		super(id);
	}

	public TransactionalCommandContext() {
		super();
	}

	private transient PersistenceAspect	db;

	public PersistenceAspect getDb() {
		return db;
	}

	public void setDb(PersistenceAspect db) {
		this.db = db;
	}
}
