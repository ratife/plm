package plm.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import plm.model.EntityPK;
import plm.model.Part;

@Repository
public class PartDao {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	public Part getByPK(EntityPK entity) {
		Session currentSession = sessionFactory.getCurrentSession();
        return currentSession.get(Part.class,entity);
	}
	
	public void create(Part part) {
		Session currentSession = sessionFactory.getCurrentSession();
		currentSession.save(part);
		currentSession.flush();
	}

	public void update(Part part) {
		Session currentSession = sessionFactory.getCurrentSession();
		currentSession.update(part);
		currentSession.flush();
	}
	
}
