package plm.services;

import plm.exception.EntityException;

public interface ServiceLifeCycle {
	/**
	 * This method setState to a entity, it throws a exception if the rules is not repected
	 * @param userId
	 * @param reference
	 * @param version
	 * @param iteration
	 * @param state
	 * @throws EntityException
	 */
	public void setState(String userId, String reference, String version, 
			int iteration, String state) throws EntityException;
	
}