package webtech2.jpa;

import java.util.ArrayList;
import java.util.UUID;

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
	
	
	
	//Methods used to create a group
	
	/**
	 * Create a new group and persists it in the DB.
	 * @param groupName group name of the new group.
	 * @param loginName owner of the new group.
	 * @return TodooGroup the newly created group.
	 * @throws NoDBEntryException if the user does not exist or, for some reason, the persisted group can not be found.
	 */
	public TodooGroup registerNewTodooGroup(String groupName, String loginName) throws NoDBEntryException {
		EntityManager em = emf.createEntityManager();
    	App app = new App();
    	
    	//get user from the DB
    	User user = app.getUserByLoginName(loginName);
		
    	//create new group
    	TodooGroup group = new TodooGroup();
    	group.setGroupUUID(UUID.randomUUID().toString());
    	group.setGroupOwner(user);
    	group.setGroupName(groupName);
    	group.setGroupMembers(new ArrayList<User>());
    	
    	//persist new group
    	persist(group);
    	
    	//get the new group from the DB
    	CriteriaBuilder cb = em.getCriteriaBuilder();
    	CriteriaQuery<TodooGroup> cq = cb.createQuery(TodooGroup.class);
    	Root<TodooGroup> g = cq.from(TodooGroup.class);
    	
    	Predicate groupUUIDMatches = cb.equal(g.get("groupUUID"), group.getGroupUUID());
    	cq.select(g).where(groupUUIDMatches);
    	
    	//return result
    	TypedQuery<TodooGroup> query = em.createQuery(cq);
    	ArrayList<TodooGroup> result = new ArrayList<TodooGroup>(query.getResultList());
    	em.close();
    	
    	if (result.size() > 0) {
    		return result.get(0);
    	} else {
    		throw new NoDBEntryException("Error finding the group after creating. Group does not exist.");
    	}
	}
	
	//Methods used to get something from the DB
	
	/**
	 * Gets a TodooGroup from the DB.
	 * @param groupUUID the group id
	 * @return TodooGroup the group
	 * @throws NoDBEntryException if the group with the given group ID does not exist.
	 */
	public TodooGroup getGroupByID(String groupID) throws NoDBEntryException {
		EntityManager em = emf.createEntityManager();

		//create criteria
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<TodooGroup> cq = cb.createQuery(TodooGroup.class);
		Root<TodooGroup> user = cq.from(TodooGroup.class);
		
		//set the predicate
		Predicate groupUUIDMatches = cb.equal(user.get("groupUUID"), groupID);
		
		//select
		cq.select(user).where(groupUUIDMatches);
		TypedQuery<TodooGroup> query = em.createQuery(cq);
		ArrayList<TodooGroup> result = new ArrayList<TodooGroup>(query.getResultList());
		em.close();
		
		if (result.size() > 0) {
			return result.get(0);
		} else {
			throw new NoDBEntryException("Group does not exist.");
		}
	}
	
	public ArrayList<TodooGroup> getGroupWhereOwnerIs(String loginName) {
		return new ArrayList<TodooGroup>();
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
		
		User managedUser = app.getUserByLoginName(user.getLoginName());
		
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
	public void addMemberToGroup(String groupID, User user) throws NoDBEntryException {
		EntityManager em = emf.createEntityManager();
		App app = new App();
    	
    	TodooGroup todooGroup = getGroupByID(groupID);
    	ArrayList<User> groupMembers = todooGroup.getGroupMembers();
    	
    	User newMember = app.getUserByLoginName(user.getLoginName());
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
	public void deleteUserFromGroup(String groupID, User user) throws NoDBEntryException {
    	EntityManager em = emf.createEntityManager();
    	App app = new App();
    	
    	TodooGroup todooGroup = getGroupByID(groupID);
    	ArrayList<User> groupMembers = todooGroup.getGroupMembers();
    	
    	User userToRemove = app.getUserByLoginName(user.getLoginName());
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
	public void deleteGroup(String groupID) {
    	EntityManager em = emf.createEntityManager();
    	
    	//create criteria delete
    	CriteriaBuilder cb = em.getCriteriaBuilder();
    	CriteriaDelete<TodooGroup> cd = cb.createCriteriaDelete(TodooGroup.class);
    	Root<TodooGroup> g = cd.from(TodooGroup.class);
    	
    	//set predicate
		Predicate sameGroupUUID = cb.equal(g.get("groupUUID"), groupID);
		
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
