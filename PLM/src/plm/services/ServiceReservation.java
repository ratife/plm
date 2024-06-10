package plm.services;

import plm.exception.EntityException;

public interface ServiceReservation {
	
	/**
	 * This method reserve a entity, it throws a exception if the rules is not repected
	 * @param userId
	 * @param reference
	 * @param version
	 * @param iteration
	 * @throws EntityException
	 */
	public void reserve(String userId, String reference, String version, int iteration) throws EntityException;
	
	
	/**
	 * This method free a entity, it throws a exception if the rules is not repected
	 * @param userId
	 * @param reference
	 * @param version
	 * @param iteration
	 * @throws EntityException
	 */
	public void free(String userId, String reference, String version, int iteration) throws EntityException;
	
	/**
	 * This method update a entity, it throws a exception if the rules is not repected
	 * @param userId
	 * @param reference
	 * @param version
	 * @param iteration
	 * @param partAttribute1
	 * @param partAttribute2
	 * @throws EntityException
	 */
	public void update(String userId, String reference, String version, int iteration,
			String partAttribute1, String partAttribute2) throws EntityException;
	
}