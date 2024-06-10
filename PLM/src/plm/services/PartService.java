package plm.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import plm.dao.DocumentDao;
import plm.dao.PartDao;
import plm.exception.EntityException;
import plm.exception.PartException;
import plm.model.Document;
import plm.model.EntityPK;
import plm.model.Part;
import plm.utils.ParamValidator;
import plm.utils.Utils;

@Transactional
@Service
public class PartService implements ServiceLifeCycle,ServiceReservation,ServiceVersionning{
	
	@Autowired
	private PartDao partDao;

	@Autowired
	private DocumentDao documentDao;
	
	@Autowired
	 private MessageSource messageSource;
	
	@Autowired
	 private ParamValidator paramValidator;
	
	private static Logger LOGGER = LoggerFactory.getLogger(PartService.class);
	

   public void reserve(String userId, String reference, String version, int iteration) throws EntityException {
	   paramValidator.validateServiceParameters(reference, version, userId, iteration);
	   
		EntityPK pk = new EntityPK(reference, version, iteration);
		Part part = partDao.getByPK(pk);
    	
		if(part==null) {
		    LOGGER.error(Utils.message(messageSource, "part.reserve.not_found") + pk);
		    throw new PartException(Utils.message(messageSource, "part.reserve.not_found") + pk);
		}

		if(part.isReserved()) {
		    LOGGER.error(Utils.message(messageSource, "part.reserve.already_researved") + part);
		    throw new PartException(Utils.message(messageSource, "part.reserve.already_researved"), part);
		}

		if (part.getLifeCycleTemplate().isFinal(part.getLifeCycleState())) {
		    LOGGER.error(Utils.message(messageSource, "part.reserve.life_cycle_final") + part);
		    throw new PartException(Utils.message(messageSource, "part.reserve.life_cycle_final"), part);
		}

    	part.reserve(userId);
    	partDao.update(part);
    
	}

	public void update(String userId, String reference, String version, int iteration,
			String partAttribute1, String partAttribute2) throws EntityException {
		
		paramValidator.validateServiceParameters(reference, version, userId, iteration);
		
		EntityPK pk = new EntityPK(reference, version, iteration);
		Part part = partDao.getByPK(pk);
    	
		if(part==null) {
		    LOGGER.error(Utils.message(messageSource, "part.update.not_found") + pk);
		    throw new PartException(Utils.message(messageSource, "part.update.not_found") + pk);
		}

		if(!part.isReserved()) {
		    LOGGER.error(Utils.message(messageSource, "part.update.not_reserved") + part);
		    throw new PartException(Utils.message(messageSource, "part.update.not_reserved"), part);
		}

		if(!part.getReservedBy().equals(userId)) {
		    LOGGER.error(Utils.message(messageSource, "part.update.not_permite_user") + part);
		    throw new PartException(Utils.message(messageSource, "part.update.not_permite_user"), part);
		}

		if(part.getLifeCycleTemplate().isFinal(part.getLifeCycleState())) {
		    LOGGER.error(Utils.message(messageSource, "part.update.final_life_cycle") + part);
		    throw new PartException(Utils.message(messageSource, "part.update.final_life_cycle"), part);
		}

    		
		part.setPartAttribute1(partAttribute1);
		part.setPartAttribute2(partAttribute2);
		
		partDao.update(part);
    	
	}

	public void free(String userId, String reference, String version, int iteration) throws EntityException {
		paramValidator.validateServiceParameters(reference, version, userId, iteration);
		EntityPK pk = new EntityPK(reference, version, iteration);
		Part part = partDao.getByPK(pk);
    	
		if(part==null) {
		    LOGGER.error(Utils.message(messageSource, "part.update.final_life_cycle") + pk);
		    throw new PartException(Utils.message(messageSource, "part.free.not_found") + pk);
		}

		if(!part.isReserved()) {
		    LOGGER.error(Utils.message(messageSource, "part.free.not_reserved") + part);
		    throw new PartException(Utils.message(messageSource, "part.free.not_reserved"), part);
		}

		if(!part.getReservedBy().equals(userId)) {
		    LOGGER.error(Utils.message(messageSource, "part.free.not_permite_user") + part);
		    throw new PartException(Utils.message(messageSource, "part.free.not_permite_user"), part);
		}

		if(part.getLifeCycleTemplate().isFinal(part.getLifeCycleState())) {
		    LOGGER.error(Utils.message(messageSource, "part.free.final_life_cycle") + part);
		    throw new PartException(Utils.message(messageSource, "part.free.final_life_cycle"), part);
		}

    		
    	part.free();
    	
    	partDao.update(part);
	}

	public void setState(String userId, String reference, String version, 
			int iteration, String state) throws EntityException  {
		
		paramValidator.validateServiceParameters(reference, version, userId, iteration);
		
		EntityPK pk = new EntityPK(reference, version, iteration);
		Part part = partDao.getByPK(pk);
    	
		if(part==null) {
		    LOGGER.error(Utils.message(messageSource, "part.state.not_found") + pk);
		    throw new PartException(Utils.message(messageSource, "part.state.not_found") + pk);
		}

		if(part.isReserved()) {
		    LOGGER.error(Utils.message(messageSource, "part.state.reserved") + part);
		    throw new PartException(Utils.message(messageSource, "part.state.reserved"), part);
		}

		if(!part.getLifeCycleTemplate().isKnown(state)) {
		    LOGGER.error(Utils.message(messageSource, "part.state.life_cycle_unknow") + state);
		    throw new PartException(Utils.message(messageSource, "part.state.life_cycle_unknow") + state, part);
		}

		if(part.getLifeCycleTemplate().isFinal(state)) {
		    LOGGER.error(Utils.message(messageSource, "part.state.life_cycle_final") + state);
		    throw new PartException(Utils.message(messageSource, "part.state.life_cycle_final") + state, part);
		}

		if(!part.isCorrectNextState(state)) {
		    LOGGER.error(Utils.message(messageSource, "part.state.life_cycle_error") + state);
		    throw new PartException(Utils.message(messageSource, "part.state.life_cycle_error") + state, part);
		}

    		
		part.changeState(state);

		partDao.update(part);
	}

	public void revise(String userId, String reference, String version, int iteration) {
		
		paramValidator.validateServiceParameters(reference, version, userId, iteration);
		
		EntityPK pk = new EntityPK(reference,version,iteration);
		
    	Part part = partDao.getByPK(pk);
    	
    	if(part==null) {
    	    LOGGER.error(Utils.message(messageSource, "part.revise.not_found") + pk);
    	    throw new PartException(Utils.message(messageSource, "part.revise.not_found") + pk);
    	}

    	if(part.isReserved()) {
    	    LOGGER.error(Utils.message(messageSource, "part.revise.reserved") + part);
    	    throw new PartException(Utils.message(messageSource, "part.revise.reserved"), part);
    	}

    	if(!part.getLifeCycleTemplate().isFinal(part.getLifeCycleState())) {
    	    LOGGER.error(Utils.message(messageSource, "part.revise.enity_not_realese") + part);
    	    throw new PartException(Utils.message(messageSource, "part.revise.enity_not_realese"), part);
    	}

		Part nextPartVersion = new Part(part.getReference(), part.getVersionSchema().getNextVersionLabel(version), 1);
		
		nextPartVersion.setReserved(false);
		nextPartVersion.setReservedBy(null);
		
		nextPartVersion.setLifeCycleTemplate(part.getLifeCycleTemplate());
		nextPartVersion.setLifeCycleState(part.getLifeCycleTemplate().getInitialState());

		nextPartVersion.setVersionSchema(part.getVersionSchema());
		nextPartVersion.setPartAttribute1(part.getPartAttribute1());
		nextPartVersion.setPartAttribute2(part.getPartAttribute2());
		
		partDao.create(nextPartVersion);

		part.getDocument().forEach(document->{
			Document nextDocumentVersion = new Document(document.getId().getReference(), document.getVersionSchema().getNextVersionLabel(version), 1);
    		
    		nextDocumentVersion.setReserved(false);
    		nextDocumentVersion.setReservedBy(null);
    		
    		nextDocumentVersion.setLifeCycleTemplate(document.getLifeCycleTemplate());
    		nextDocumentVersion.setLifeCycleState(document.getLifeCycleTemplate().getInitialState());

    		nextDocumentVersion.setVersionSchema(document.getVersionSchema());
    		
    		nextDocumentVersion.setDocumentAttribute1(document.getDocumentAttribute1());
    		nextDocumentVersion.setDocumentAttribute2(document.getDocumentAttribute2());
    		
    		documentDao.create(nextDocumentVersion);
		});	
	}	
}