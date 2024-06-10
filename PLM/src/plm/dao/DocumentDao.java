package plm.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import plm.model.Document;
import plm.model.EntityPK;

@Repository
@Transactional
public class DocumentDao {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public Document getByPK(EntityPK entity) {
		Session currentSession = sessionFactory.getCurrentSession();
        return currentSession.get(Document.class,entity);
	}

	public void update(Document document) {
		Session currentSession = sessionFactory.getCurrentSession();
		currentSession.update(document);
		currentSession.flush();
	}

	public void create(Document document) {
		Session currentSession = sessionFactory.getCurrentSession();
		currentSession.save(document);
		currentSession.flush();
	}
}
