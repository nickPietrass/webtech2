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
	
	public Tudoo registerNewTudoo(String title, String content, String loginName) throws NoDBEntryException {
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
	
	public ArrayList<Tudoo> getAllVisibleTudoosOfUser(String loginName) throws NoDBEntryException {
		EntityManager em = emf.createEntityManager();
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tudoo> cq = cb.createQuery(Tudoo.class);
		Root<Tudoo> tudoo = cq.from(Tudoo.class);
		
		cq.select(tudoo);
		
		TypedQuery<Tudoo> query = em.createQuery(cq);
		ArrayList<Tudoo> queryResult = new ArrayList<Tudoo>(query.getResultList());
		ArrayList<Tudoo> result = new ArrayList<Tudoo>();
		
		for (Tudoo t : queryResult) {
			if (t.getTudooOwner().getLoginName() == loginName || t.getVisibleBy().contains(loginName)) {
				result.add(t);
			}
		}
		
		em.close();
		return result;
	}
	
	public void changeTudooContent(String tudooID, String newTitle, String newContent) {
		//create EntityManager
			EntityManager em = emf.createEntityManager();
			
			//create update
	    	CriteriaBuilder cb = em.getCriteriaBuilder();
	    	CriteriaUpdate<Tudoo> update = cb.createCriteriaUpdate(Tudoo.class);
	    	
	    	//set the root class
	    	Root<Tudoo> tudoo = update.from(Tudoo.class);

	    	//set the update and where clause
	    	update.set("title", newTitle);
	    	Predicate tudooIDMatches = cb.equal(tudoo.get("tudooUUID"), tudooID);
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
	
	public void addUserOrGroupToTudoo(String tudooID, String id) throws NoDBEntryException {
		EntityManager em = emf.createEntityManager();
		App app = new App();
    	
		if (tudooDoesNotExist(tudooID)) {
			app.close();
			em.close();
			throw new NoDBEntryException("Tudoo does not exist.");
		}
		
    	Tudoo tudoo = getTudooByID(tudooID);
    	
    	if (app.userIsNotInDB(id) && app.getGroupApp().groupIsNotInDB(id)) {
    		throw new NoDBEntryException("id is invalid");
    	}
    	
    	ArrayList<String> newVisibleBy = tudoo.getVisibleBy();
    	if (!newVisibleBy.contains(id)) {
    		newVisibleBy.add(id);
    	}
    	tudoo.setVisibleBy(newVisibleBy);
    	
    	ArrayList<String> newEditableBy = tudoo.getEditableBy();
    	if (!newEditableBy.contains(id)) {
    		newEditableBy.add(id);
    	}
    	tudoo.setEditableBy(newEditableBy);
    	
    	em.getTransaction().begin();
    	em.merge(tudoo);
    	em.flush();
    	em.getTransaction().commit();
    	
    	em.close();
	}
	
	public void changeUserOrGroupPermissionToVisibleByOnly(String tudooID, String id) throws NoDBEntryException {
		EntityManager em = emf.createEntityManager();
    	
    	if (tudooDoesNotExist(tudooID)) {
    		em.close();
    		throw new NoDBEntryException("Tudoo does not exist.");
    	}
    	
    	Tudoo tudoo = getTudooByID(tudooID);
    	ArrayList<String> newEditableBy = tudoo.getEditableBy();
    	newEditableBy.remove(id);
    	
    	tudoo.setEditableBy(newEditableBy);
    	
    	em.getTransaction().begin();
    	em.merge(tudoo);
    	em.getTransaction().commit();
    	em.close();	
	}
	
	public void deleteUserOrGroupFromTudoo(String tudooID, String id) throws NoDBEntryException {
		EntityManager em = emf.createEntityManager();
    	
    	if (tudooDoesNotExist(tudooID)) {
    		em.close();
    		throw new NoDBEntryException("Tudoo does not exist.");
    	}
    	
    	Tudoo tudoo = getTudooByID(tudooID);
    	ArrayList<String> newEditableBy = tudoo.getEditableBy();
    	newEditableBy.remove(id);
    	
    	ArrayList<String> newVisibleBy = tudoo.getVisibleBy();
    	newVisibleBy.remove(id);
    	
    	tudoo.setEditableBy(newEditableBy);
    	tudoo.setVisibleBy(newVisibleBy);
    	
    	em.getTransaction().begin();
    	em.merge(tudoo);
    	em.getTransaction().commit();
    	em.close();	
	}
	
	public void deleteTudoo(String tudooID) throws NoDBEntryException {
		if (tudooDoesNotExist(tudooID)) {
    		throw new NoDBEntryException();
    	}
    	
    	EntityManager em = emf.createEntityManager();
		
    	//create criteria delete
    	CriteriaBuilder cb = em.getCriteriaBuilder();
    	CriteriaDelete<Tudoo> cd = cb.createCriteriaDelete(Tudoo.class);
    	Root<Tudoo> tudoo = cd.from(Tudoo.class);
    	
    	//set predicate
		Predicate TudooIDMatches = cb.equal(tudoo.get("tudooUUID"), tudooID);
		
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
	
	public Tudoo getTudooByID(String tudooID) throws NoDBEntryException {
    	EntityManager em = emf.createEntityManager();
		
		//create criteria
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Tudoo> cq = cb.createQuery(Tudoo.class);
		Root<Tudoo> tudoo = cq.from(Tudoo.class);
		
		//set the predicate
		Predicate tudooIDMatches = cb.equal(tudoo.get("tudooUUID"), tudooID);
		
		//select
		cq.select(tudoo).where(tudooIDMatches);
		TypedQuery<Tudoo> query = em.createQuery(cq);
		ArrayList<Tudoo> result = new ArrayList<Tudoo>(query.getResultList());
		em.close();
		
		if (result.size() > 0) {
			return result.get(0);
		} else {
			throw new NoDBEntryException("The Tudoo does not exist.");
		}
    }
}
