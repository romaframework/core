package org.romaframework.core.command;

import org.romaframework.aspect.persistence.PersistenceAspect;

/**
 * Mark the command as transactional
 * 
 * @author Giordano Maestro (giordano.maestro--at--assetdata.it) 20/set/07
 * 
 */
public abstract class TransactionCommand implements Command {

	/**
	 * Retrieve the persistence aspect in the context and call the execute passing
	 * the aspect
	 */
	public final Object execute(CommandContext iContext) {
		PersistenceAspect db = ((TransactionalCommandContext) iContext).getDb();
		return execute(db, iContext);
	}

	protected abstract Object execute(PersistenceAspect db, CommandContext iContext);

}
