package plm.services;

import plm.exception.EntityException;

public interface ServiceVersionning {
	
	/**
	 * This method revise a entity, it throws a exception if the rules is not repected
	 * @param userId
	 * @param reference
	 * @param version
	 * @param iteration
	 * @throws EntityException
	 */
	public void revise(String userId, String reference, String version, int iteration) throws EntityException;
	
}