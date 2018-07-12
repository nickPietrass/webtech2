package webtech2.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import webtech2.jpa.entities.TodooGroup;
import webtech2.jpa.entities.User;
import webtech2.jpa.exceptions.NoDBEntryException;

public class GroupApp {
	
	private EntityManagerFactory emf;
	
	public GroupApp(EntityManagerFactory emf) {
		this.emf = emf;
	}
	
	public void registerNewGroup(User owner) {
		EntityManager em = emf.createEntityManager();
    	App app = new App();
		
    	try {
    		User managedOwner = app.getUserByLoginNameAndPassword(owner.getLoginName(), owner.getPassword());
    		
    		TodooGroup group = new TodooGroup();
        	group.setGroupName("group name");
        	group.setGroupOwner(managedOwner);
        	
        	persist(group);
    	} catch (NoDBEntryException e) {
    		em.getTransaction().begin();
    		em.merge(owner);
    		em.flush();
    		em.getTransaction().commit();
    		
    		User managedOwner = em.find(User.class, owner.getUserUUID());
    		
    		TodooGroup group = new TodooGroup();
        	group.setGroupName("group name");
        	group.setGroupOwner(managedOwner);
        	
        	persist(group);
    	}

    	app.close();
    	em.close();
	}
	
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
}
