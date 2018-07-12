package webtech2.jpa;

import java.util.ArrayList;
import java.util.HashSet;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

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
	
	public TodooGroup getGroupByName(String groupName) throws NoDBEntryException{
		//create new EntityManager
    	EntityManager em = emf.createEntityManager();
    	
    	//create criteria
    	CriteriaBuilder cb = em.getCriteriaBuilder();
    	CriteriaQuery<TodooGroup> q = cb.createQuery(TodooGroup.class);
    	
    	//set root
    	Root<TodooGroup> g = q.from(TodooGroup.class);
    	q.select(g).where(cb.equal(g.get("groupName"), groupName));
    	
    	//create query and return result group
    	TypedQuery<TodooGroup> query = em.createQuery(q);
    	ArrayList<TodooGroup> result = new ArrayList<TodooGroup>(query.getResultList());
    	
    	if (result.size() > 0) {
    		return result.get(0);
    	} else {
    		throw new NoDBEntryException("Group does not exist.");
    	}
	}
	
	public void addMemberToGroup(TodooGroup group, User user) throws NoDBEntryException {
		EntityManager em = emf.createEntityManager();
		App app = new App();
    	
    	TodooGroup todooGroup = getGroupByName(group.getGroupName());
    	HashSet<User> groupMembers = todooGroup.getGroupMembers();
    	
    	User newMember = app.getUserByLoginNameAndPassword(user.getLoginName(), user.getPassword());
    	groupMembers.add(newMember);
    	
    	//create update
    	CriteriaBuilder cb = em.getCriteriaBuilder();
    	CriteriaUpdate<TodooGroup> update = cb.createCriteriaUpdate(TodooGroup.class);
    	
    	//set the root class
    	Root<TodooGroup> g = update.from(TodooGroup.class);
    	
    	//set the update and where clause
    	update.set("groupMembers", groupMembers);
    	Predicate sameGroupUUID = cb.equal(g.get("groupName"), "group name");
    	update.where(sameGroupUUID);
    	
    	//update group members
    	EntityTransaction tx = em.getTransaction();
    	tx.begin();
    	//em.createQuery(update).executeUpdate();
    	todooGroup.setGroupMembers(groupMembers);
    	tx.commit();
    	
    	app.close();
    	em.close();
	}
	
	public void deleteMemberFromGroup(TodooGroup group, User user) throws NoDBEntryException {
    	EntityManager em = emf.createEntityManager();
    	App app = new App();
    	
    	TodooGroup todooGroup = getGroupByName(group.getGroupName());
    	HashSet<User> groupMembers = todooGroup.getGroupMembers();
    	
    	User userToRemove = app.getUserByLoginNameAndPassword(user.getLoginName(), user.getPassword());
    	groupMembers.remove(userToRemove);
    	
    	for (User u : groupMembers) {
    		System.out.println(u.getDisplayName());
    	}
    	
    	//create update
    	CriteriaBuilder cb = em.getCriteriaBuilder();
    	CriteriaUpdate<TodooGroup> update = cb.createCriteriaUpdate(TodooGroup.class);
    	
    	//set the root class
    	Root<TodooGroup> g = update.from(TodooGroup.class);
    	
    	//set the update and where clause
    	update.set("groupMembers", groupMembers);
    	Predicate sameGroupUUID = cb.equal(g.get("groupName"), "group name");
    	update.where(sameGroupUUID);
    	
    	//update group members
    	EntityTransaction tx = em.getTransaction();
    	tx.begin();
    	//em.createQuery(update).executeUpdate();
    	todooGroup.setGroupMembers(groupMembers);
    	tx.commit();
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
