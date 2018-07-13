package webtech2.jpa;

import java.util.ArrayList;
import java.util.HashSet;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
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
	
	/**
	 * Method used to close GroupApp's EntityManagerFactory.
	 */
	public void close() {
		emf.close();
	}
	
	/**
	 * Create and persist a new group into the DB. Persists the owner first, if not already in the DB.
	 * @param owner owner of the group
	 */
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
    		
    		CriteriaBuilder cb = em.getCriteriaBuilder();
    		CriteriaQuery<TodooGroup> cq = cb.createQuery(TodooGroup.class);
    		Root<TodooGroup> g = cq.from(TodooGroup.class);
    		
    		cq.select(g).where(cb.equal(g.get("groupOwner"), managedOwner));
    		TypedQuery<TodooGroup> query = em.createQuery(cq);
    		
    		int groupNumber = query.getResultList().size() + 1;
    		
    		TodooGroup group = new TodooGroup();
        	group.setGroupName(managedOwner.getDisplayName() + "'s group " + groupNumber);
        	group.setGroupOwner(managedOwner);
        	
        	persist(group);
    	}

    	app.close();
    	em.close();
	}
	
	/**
	 * Searches for a specific group by its name.
	 * @param groupName group name to search for.
	 * @return TodooGroup a group with the given name.
	 * @throws NoDBEntryException if the group with the given name does not exist.
	 */
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
	
	/**
	 * Get all groups, where the user is owner or member of the groups.
	 * @param user user
	 * @return ArrayList<TodooGroup> groups
	 * @throws NoDBEntryException if the given user does note exist.
	 */
	public ArrayList<TodooGroup> getGroupsOfUser(User user) throws NoDBEntryException {
		//create EntityManager and App
		EntityManager em = emf.createEntityManager();
		App app = new App();
		
		User managedUser = app.getUserByLoginNameAndPassword(user.getLoginName(), user.getPassword());
		
		//create criteria
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<TodooGroup> cq = cb.createQuery(TodooGroup.class);
		
		//set root and select
		Root<TodooGroup> g = cq.from(TodooGroup.class);
		Predicate userIsGroupOwner = cb.equal(g.get("groupOwner"), managedUser);
		Predicate userIsGroupMember = cb.isMember(managedUser, g.get("groupMembers"));
		Predicate userIsGroupOwnerOrGroupMember = cb.and(userIsGroupOwner, userIsGroupMember);
		cq.select(g).where(userIsGroupOwnerOrGroupMember);
		
		//return results as ArrayList
		TypedQuery<TodooGroup> query = em.createQuery(cq);
		return new ArrayList<TodooGroup>(query.getResultList());
	}
	
	/**
	 * Adds a User to the group.
	 * @param group group
	 * @param user user to be added to the group
	 * @throws NoDBEntryException if the group or user does not exist.
	 */
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
	
	/**
	 * Deletes a member from the group.
	 * @param group group.
	 * @param user user to delete.
	 * @throws NoDBEntryException if the group or user does not exist.
	 */
	public void deleteMemberFromGroup(TodooGroup group, User user) throws NoDBEntryException {
    	EntityManager em = emf.createEntityManager();
    	App app = new App();
    	
    	TodooGroup todooGroup = getGroupByName(group.getGroupName());
    	HashSet<User> groupMembers = todooGroup.getGroupMembers();
    	
    	User userToRemove = app.getUserByLoginNameAndPassword(user.getLoginName(), user.getPassword());
    	groupMembers.remove(userToRemove);
    	
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
	 * Deletes the whole group.
	 * @param group group to be deleted.
	 */
	private void deleteGroup(TodooGroup group) {
    	EntityManager em = emf.createEntityManager();
    	
    	//create criteria delete
    	CriteriaBuilder cb = em.getCriteriaBuilder();
    	CriteriaDelete<TodooGroup> cd = cb.createCriteriaDelete(TodooGroup.class);
    	Root<TodooGroup> g = cd.from(TodooGroup.class);
    	
    	//set predicate
		Predicate sameGroupUUID = cb.equal(g.get("groupName"), group.getGroupName());
		
		//select
		cd.where(sameGroupUUID);
		
		//delete
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.createQuery(cd).executeUpdate();
		tx.commit();
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
