package webtech2.jpa;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import webtech2.jpa.entities.Tudoo;
import webtech2.jpa.entities.TudooGroup;
import webtech2.jpa.entities.User;
import webtech2.jpa.exceptions.NoDBEntryException;

public class TudooApp {
	
	private EntityManagerFactory emf;
	
	public TudooApp(EntityManagerFactory emf) {
		this.emf = emf;
	}
	
	public void close() {
		emf.close();
	}
	
	public Tudoo registerNewTodoo(String title, String content, String loginName) throws NoDBEntryException {
		EntityManager em = emf.createEntityManager();
		App app = new App();
    	
    	if (app.userIsNotInDB(loginName)) {
    		throw new NoDBEntryException("User does not exist.");
    	}
    	
    	User user = app.getUserByLoginName(loginName);
    	DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss z");
		Date date = Calendar.getInstance().getTime();
		String created = dateFormat.format(date);
    	
    	//create tudoo
		Tudoo tudoo = new Tudoo();
		tudoo.setTudooUUID(UUID.randomUUID().toString());
		tudoo.setTudooOwner(user);
		tudoo.setCreationDate(created);
		tudoo.setTitle(title);
		tudoo.setContent(content);
		tudoo.setVisibleBy(new ArrayList<String>());
		tudoo.setEditableBy(new ArrayList<String>());

		persist(tudoo);
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tudoo> cq = cb.createQuery(Tudoo.class);
		Root<Tudoo> t = cq.from(Tudoo.class);
		
		cq.select(t).where(cb.equal(t.get("tudooUUID"), tudoo.getTudooUUID()));
		TypedQuery<Tudoo> query = em.createQuery(cq);
		
		return query.getSingleResult();
	}
	
	public ArrayList<Tudoo> getAllVisibleTodoosOfUser(String loginName) {
		//TODO
		
		return new ArrayList<Tudoo>();
	}
	
	public void changeTudooContent(UUID tudooID, String newTitle, String newContent) {
		//create EntityManager
			EntityManager em = emf.createEntityManager();
			
			//create update
	    	CriteriaBuilder cb = em.getCriteriaBuilder();
	    	CriteriaUpdate<Tudoo> update = cb.createCriteriaUpdate(Tudoo.class);
	    	
	    	//set the root class
	    	Root<Tudoo> tudoo = update.from(Tudoo.class);

	    	//set the update and where clause
	    	update.set("title", newTitle);
	    	Predicate tudooIDMatches = cb.equal(tudoo.get("todooUUID"), tudooID);
	    	update.where(tudooIDMatches);
	    	
	    	EntityTransaction tx = em.getTransaction();
	    	tx.begin();
	    	em.createQuery(update).executeUpdate();
	    	tx.commit();
	    	
	    	update.set("content", newContent);
	    	update.where(tudooIDMatches);
	    	
	    	tx.begin();
	    	em.createQuery(update).executeUpdate();
	    	tx.commit();
	    	
	    	em.close();
	}
	
	public void addUserOrGroupToTodoo(UUID todooID, String loginName) {
		//TODO
	}
	
	public void changeUserOrGroupPermissionToVisibleByOnly(UUID todooID, String loginName) {
		//TODO
	}
	
	public void deleteUserOrGroupFromTodoo(UUID todooID, String loginName) {
		//TODO
	}
	
	public void deleteTodoo(String tudooID) throws NoDBEntryException {
		if (tudooDoesNotExist(tudooID)) {
    		throw new NoDBEntryException();
    	}
    	
    	EntityManager em = emf.createEntityManager();
		
    	//create criteria delete
    	CriteriaBuilder cb = em.getCriteriaBuilder();
    	CriteriaDelete<Tudoo> cd = cb.createCriteriaDelete(Tudoo.class);
    	Root<Tudoo> user = cd.from(Tudoo.class);
    	
    	//set predicate
		Predicate TudooIDMatches = cb.equal(user.get("tudooUUID"), tudooID);
		
		//select
		cd.where(TudooIDMatches);
		
		//delete
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.createQuery(cd).executeUpdate();
		tx.commit();
		em.close();
	}
	
	
	
	//Helper functions
	
	/**
	 * Helper function to persist an entity in the database. This method does not check for duplicates.
	 * @param entity some entity that needs to be persisted.
	 */
	private void persist(Object entity) {
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		
		tx.begin();
		em.persist(entity);
		tx.commit();
		em.close();
	}
	
	public boolean tudooDoesNotExist(String tudooID) {
    	//create EntityManager and criteria
		EntityManager em = emf.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tudoo> cq = cb.createQuery(Tudoo.class);
		Root<Tudoo> t = cq.from(Tudoo.class);
		cq.select(t).where(cb.equal(t.get("tudooUUID"), tudooID));
		
		TypedQuery<Tudoo> query = em.createQuery(cq);
		return query.getResultList().size() == 0;
    }
}
