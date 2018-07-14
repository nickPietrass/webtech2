package webtech2.jpa;

import java.util.ArrayList;
import java.util.HashSet;
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

import webtech2.jpa.entities.TudooGroup;
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
	public TudooGroup registerNewTodooGroup(String groupName, String loginName) throws NoDBEntryException {
		EntityManager em = emf.createEntityManager();
    	App app = new App();
    	
    	//get user from the DB
    	if (app.userIsNotInDB(loginName)) {
    		app.close();
    		em.close();
    		throw new NoDBEntryException("User does not exist.");
    	}
    	
        User user = app.getUserByLoginName(loginName);
		
    	//create new group
    	TudooGroup group = new TudooGroup();
    	group.setGroupUUID(UUID.randomUUID().toString());
    	group.setGroupOwner(user);
    	group.setGroupName(groupName);
    	group.setGroupMembers(new ArrayList<String>());
    	
    	//persist new group
    	persist(group);
    	
    	//get the new group from the DB
    	CriteriaBuilder cb = em.getCriteriaBuilder();
    	CriteriaQuery<TudooGroup> cq = cb.createQuery(TudooGroup.class);
    	Root<TudooGroup> g = cq.from(TudooGroup.class);
    	
    	Predicate groupUUIDMatches = cb.equal(g.get("groupUUID"), group.getGroupUUID());
    	cq.select(g).where(groupUUIDMatches);
    	
    	//return result
    	TypedQuery<TudooGroup> query = em.createQuery(cq);
    	ArrayList<TudooGroup> result = new ArrayList<TudooGroup>(query.getResultList());
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
	public TudooGroup getGroupByID(String groupID) throws NoDBEntryException {
		EntityManager em = emf.createEntityManager();

		//create criteria
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<TudooGroup> cq = cb.createQuery(TudooGroup.class);
		Root<TudooGroup> user = cq.from(TudooGroup.class);
		
		//set the predicate
		Predicate groupUUIDMatches = cb.equal(user.get("groupUUID"), groupID);
		
		//select
		cq.select(user).where(groupUUIDMatches);
		TypedQuery<TudooGroup> query = em.createQuery(cq);
		ArrayList<TudooGroup> result = new ArrayList<TudooGroup>(query.getResultList());
		em.close();
		
		if (result.size() > 0) {
			return result.get(0);
		} else {
			throw new NoDBEntryException("Group does not exist.");
		}
	}
	
	/**
	 * Get all groups where the give user is owner of.
	 * @param loginName user
	 * @return ArrayList<TodooGroup> groups with the given user as the owner.
	 * @throws NoDBEntryException if the user does not exist.
	 */
	public ArrayList<TudooGroup> getGroupsWhereOwnerIs(String loginName) throws NoDBEntryException {
    	EntityManager em = emf.createEntityManager();
    	App app = new App();
    	
    	if (app.userIsNotInDB(loginName)) {
    		app.close();
    		em.close();
    		throw new NoDBEntryException("User does not exist");
    	}
    	
    	User managedUser = app.getUserByLoginName(loginName);

		//create criteria
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<TudooGroup> cq = cb.createQuery(TudooGroup.class);
		Root<TudooGroup> user = cq.from(TudooGroup.class);
		
		//set the predicate
		Predicate groupUUIDMatches = cb.equal(user.get("groupOwner"), managedUser);
		
		//select
		cq.select(user).where(groupUUIDMatches);
		TypedQuery<TudooGroup> query = em.createQuery(cq);
		ArrayList<TudooGroup> result = new ArrayList<TudooGroup>(query.getResultList());
		em.close();
		
		return result;
	}
	
	public ArrayList<TudooGroup> getGroupsWhereUserIsPartOf(String loginName) throws NoDBEntryException {
		EntityManager em = emf.createEntityManager();
		App app = new App();
		
		if (app.userIsNotInDB(loginName)) {
    		app.close();
    		em.close();
    		throw new NoDBEntryException("User does not exist");
    	}
		
		ArrayList<TudooGroup> groups = getGroupsWhereOwnerIs(loginName);
	
		//create criteria
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<TudooGroup> cq = cb.createQuery(TudooGroup.class);
		Root<TudooGroup> g = cq.from(TudooGroup.class);
		
		//select
		cq.select(g);
		TypedQuery<TudooGroup> query = em.createQuery(cq);
		ArrayList<TudooGroup> result = new ArrayList<TudooGroup>(query.getResultList());
		
		for (TudooGroup group : result) {
			if (group.getGroupMembers().contains(loginName)) {
				groups.add(group);
			}
		}
		
		return groups;
	}
	
	/**
	 * Adds a User to the group.
	 * @param group group
	 * @param user user to be added to the group
	 * @throws NoDBEntryException if the group or user does not exist.
	 */
	public void addMemberToGroup(String groupID, String loginName) throws NoDBEntryException {
		EntityManager em = emf.createEntityManager();
		App app = new App();
		
		if (app.userIsNotInDB(loginName)) {
			em.close();
			app.close();
			throw new NoDBEntryException("User does not exist.");
		}
    	
    	TudooGroup existingGroup = getGroupByID(groupID);
    	
    	ArrayList<String> newMemberList = existingGroup.getGroupMembers();
    	newMemberList.add(loginName);
    	existingGroup.setGroupMembers(newMemberList);
    	
    	em.getTransaction().begin();
    	em.merge(existingGroup);
    	em.flush();
    	em.getTransaction().commit();
    	
    	em.close();
	}
	
	/**
	 * Deletes a member from the group.
	 * @param group group.
	 * @param user user to delete.
	 * @throws NoDBEntryException if the group or user does not exist.
	 */
	public void deleteUserFromGroup(String groupID, String loginName) throws NoDBEntryException {
		EntityManager em = emf.createEntityManager();
		App app = new App();
    	
    	if (app.userIsNotInDB(loginName)) {
    		app.close();
    		em.close();
    		throw new NoDBEntryException("User does not exist.");
    	}
    	
    	TudooGroup group = getGroupByID(groupID);
    	ArrayList<String> members = group.getGroupMembers();
    	members.remove(loginName);
    	
    	group.setGroupMembers(members);
    	
    	em.getTransaction().begin();
    	em.merge(group);
    	em.getTransaction().commit();
    	em.close();
    }
	
	/**
	 * Deletes the whole group.
	 * @param group group to be deleted.
	 */
	public void deleteGroup(String groupID) {
    	EntityManager em = emf.createEntityManager();
    	
    	//create criteria delete
    	CriteriaBuilder cb = em.getCriteriaBuilder();
    	CriteriaDelete<TudooGroup> cd = cb.createCriteriaDelete(TudooGroup.class);
    	Root<TudooGroup> g = cd.from(TudooGroup.class);
    	
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
	
	public boolean groupIsNotInDB(String groupID) {
    	//create EntityManager and criteria
		EntityManager em = emf.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<TudooGroup> cq = cb.createQuery(TudooGroup.class);
		Root<TudooGroup> g = cq.from(TudooGroup.class);
		cq.select(g).where(cb.equal(g.get("groupUUID"), groupID));
		
		TypedQuery<TudooGroup> query = em.createQuery(cq);
		return query.getResultList().size() == 0;
    }
}
