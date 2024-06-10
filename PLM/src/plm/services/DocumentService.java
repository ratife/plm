package plm.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import plm.dao.DocumentDao;
import plm.exception.DocumentException;
import plm.exception.EntityException;
import plm.model.Document;
import plm.model.EntityPK;
import plm.utils.ParamValidator;
import plm.utils.Utils;

@Transactional
@Service
public class DocumentService implements ServiceLifeCycle,ServiceReservation,ServiceVersionning{
	
	@Autowired
	private DocumentDao documentDao;
	
	private static Logger LOGGER = LoggerFactory.getLogger(PartService.class);
	
	 @Autowired
	 private MessageSource messageSource;
	 
	 @Autowired
	 private ParamValidator paramValidator;
	
	@Override
    public void reserve(String userId, String reference, String version, int iteration) throws EntityException {
		paramValidator.validateServiceParameters(reference, version, userId, iteration);
		
		EntityPK pk = new EntityPK(reference, version, iteration);
    	Document document = documentDao.getByPK(pk);
    	
    	if(document==null) {
    	    LOGGER.error(Utils.message(messageSource, "document.reserve.not_found") + pk);
    	    throw new DocumentException(Utils.message(messageSource, "document.reserve.not_found") + pk);
    	}

    	if(document.getPart()!=null){
    	    LOGGER.error(Utils.message(messageSource, "document.reserve.have_part"), document);
    	    throw new DocumentException(Utils.message(messageSource, "document.reserve.have_part"), document);
    	}

    	if(document.isReserved()) {
    	    LOGGER.error(Utils.message(messageSource, "document.reserve.already_researved"), document);
    	    throw new DocumentException(Utils.message(messageSource, "document.reserve.already_researved"), document);
    	}

    	if (document.getLifeCycleTemplate().isFinal(document.getLifeCycleState())) {
    	    LOGGER.error(Utils.message(messageSource, "document.reserve.life_cycle_final"), document);
    	    throw new DocumentException(Utils.message(messageSource, "document.reserve.life_cycle_final"), document);
    	}
    		
  
    	document.reserve(userId);
    	
  		documentDao.update(document);
	}

	@Override
	public void update(String userId, String reference, String version, 
			int iteration, String documentAttribute1, String documentAttribute2) throws EntityException {
		paramValidator.validateServiceParameters(reference, version, userId, iteration);
		
		EntityPK pk = new EntityPK(reference, version, iteration);
    	Document document = documentDao.getByPK(pk);
    	
    	if(document==null) {
    	    LOGGER.error(Utils.message(messageSource, "document.update.not_found"), pk);
    	    throw new DocumentException(Utils.message(messageSource, "document.update.not_found") + pk);
    	}

    	if(document.getPart()!=null){
    	    LOGGER.error(Utils.message(messageSource, "document.update.have_part") + document);
    	    throw new DocumentException(Utils.message(messageSource, "document.update.have_part"), document);
    	}

    	if(!document.isReserved()) {
    	    LOGGER.error(Utils.message(messageSource, "document.update.not_reserved") + document);
    	    throw new DocumentException(Utils.message(messageSource, "document.update.not_reserved"), document);
    	}

    	if(!document.getReservedBy().equals(userId)) {
    	    LOGGER.error(Utils.message(messageSource, "document.update.not_permite_user") + document);
    	    throw new DocumentException(Utils.message(messageSource, "document.update.not_permite_user"), document);
    	}

    	if(document.getLifeCycleTemplate().isFinal(document.getLifeCycleState())) {
    	    LOGGER.error(Utils.message(messageSource, "document.update.final_life_cycle") + document);
    	    throw new DocumentException(Utils.message(messageSource, "document.update.final_life_cycle"), document);
    	}

    	document.setDocumentAttribute1(documentAttribute1);
    	document.setDocumentAttribute2(documentAttribute2);	
    	
		documentDao.update(document);
	}

	@Override
	public void free(String userId, String reference, String version, int iteration) throws EntityException{
		paramValidator.validateServiceParameters(reference, version, userId, iteration);
		
		EntityPK pk = new EntityPK(reference, version, iteration);
		Document document = documentDao.getByPK(pk);
    	
		if(document==null) {
		    LOGGER.error(Utils.message(messageSource, "document.free.not_found") + pk);
		    throw new DocumentException(Utils.message(messageSource, "document.free.not_found") + pk);
		}

		if(document.getPart()!=null){
		    LOGGER.error(Utils.message(messageSource, "document.update.have_part") + document);
		    throw new DocumentException(Utils.message(messageSource, "document.free.have_part"), document);
		}

		if(!document.isReserved()) {
		    LOGGER.error(Utils.message(messageSource, "document.free.not_reserved") + document);
		    throw new DocumentException(Utils.message(messageSource, "document.free.not_reserved"), document);
		}

		if(document.getLifeCycleTemplate().isFinal(document.getLifeCycleState())) {
		    LOGGER.error(Utils.message(messageSource, "document.free.final_life_cycle") + document);
		    throw new DocumentException(Utils.message(messageSource, "document.free.final_life_cycle"), document);
		}

    	
    	document.free();	
		documentDao.update(document);
	}

	@Override
	public void setState(String userId, String reference, String version, int iteration, String state) throws EntityException {
		paramValidator.validateServiceParameters(reference, version, userId, iteration);
		
		EntityPK pk = new EntityPK(reference, version, iteration);
		Document document = documentDao.getByPK(pk);
    	
		if(document==null) {
		    LOGGER.error(Utils.message(messageSource, "document.state.not_found") + pk);
		    throw new DocumentException(Utils.message(messageSource, "document.state.not_found") + pk);
		}

		if(document.getPart()!=null){
		    LOGGER.error(Utils.message(messageSource, "document.state.have_part") + document);
		    throw new DocumentException(Utils.message(messageSource, "document.state.have_part"), document);
		}

		if(document.isReserved()) {
		    LOGGER.error(Utils.message(messageSource, "document.state.reserved") + document);
		    throw new DocumentException(Utils.message(messageSource, "document.state.reserved"), document);
		}

		if(!document.getLifeCycleTemplate().isKnown(state)) {
		    LOGGER.error(Utils.message(messageSource, "document.state.life_cycle_unknow") + state);
		    throw new DocumentException(Utils.message(messageSource, "document.state.life_cycle_unknow") + state, document);
		}

		if(document.getLifeCycleTemplate().isFinal(state)) {
		    LOGGER.error(Utils.message(messageSource, "document.state.life_cycle_final") + state);
		    throw new DocumentException(Utils.message(messageSource, "document.state.life_cycle_final") + state, document);
		}

		if(!document.isCorrectNextState(state)) {
		    LOGGER.error(Utils.message(messageSource, "document.state.life_cycle_error") + state);
		    throw new DocumentException(Utils.message(messageSource, "document.state.life_cycle_error") + state, document);
		}

    	
    	document.setLifeCycleState(state);
    	documentDao.update(document);
	}

	@Override
	public void revise(String userId, String reference, String version, int iteration) throws EntityException{
		paramValidator.validateServiceParameters(reference, version, userId, iteration);
		
		EntityPK pk = new EntityPK(reference, version, iteration);
		Document document = documentDao.getByPK(pk);
    	
		if(document==null) {
		    LOGGER.error(Utils.message(messageSource, "document.revise.not_found") + pk);
		    throw new DocumentException(Utils.message(messageSource, "document.revise.not_found") + pk);
		}

		if(document.getPart()!=null){
		    LOGGER.error(Utils.message(messageSource, "document.revise.have_part"));
		    throw new DocumentException(Utils.message(messageSource, "document.revise.have_part"));
		}

		if(!document.getLifeCycleTemplate().isFinal(document.getLifeCycleState())) {
		    LOGGER.error(Utils.message(messageSource, "document.revise.enity_not_final") + document);
		    throw new DocumentException(Utils.message(messageSource, "document.revise.enity_not_final"), document);
		}

    	Document nextVersion = new Document(document.getId().getReference(), document.getVersionSchema().getNextVersionLabel(version), 1);
    	nextVersion.free();
    	nextVersion.setLifeCycleTemplate(document.getLifeCycleTemplate());
    	nextVersion.setLifeCycleState(document.getLifeCycleTemplate().getInitialState());
    	nextVersion.setVersionSchema(document.getVersionSchema());	
    	nextVersion.setDocumentAttribute1(document.getDocumentAttribute1());
    	nextVersion.setDocumentAttribute2(document.getDocumentAttribute2());	
    	documentDao.create(nextVersion);
    }
}
