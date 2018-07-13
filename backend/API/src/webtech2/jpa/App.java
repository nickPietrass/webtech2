package webtech2.jpa;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;
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

import webtech2.jpa.entities.User;
import webtech2.jpa.exceptions.DuplicateDBEntryException;
import webtech2.jpa.exceptions.NoDBEntryException;

public class App {
	
	private EntityManagerFactory emf;
	private GroupApp groupApp;
	
	public static App instance;
	
	/**
	 * Standard constructor
	 */
	public App() {
		this.emf = Persistence.createEntityManagerFactory("tudoo-persistence-unit");
		this.groupApp = new GroupApp(emf);
		instance = this;
	}
	
	/**
	 * Method used to close App
	 */
	public void close() {
		emf.close();
		groupApp.close();
	}
	
	/**
	 * Get the groupApp
	 * @return groupApp
	 */
	public GroupApp getGroup() {
		return groupApp;
	}
	
	//Methods used to persist something in the DB
	
	/**
	 * Persists a new user in the database and gives it a new UUID.
	 * @param loginName login name of the user
	 * @param password password of the user
	 * @param displayName display name of the user
	 */
	public void registerNewUser(String loginName, String password, String displayName) throws DuplicateDBEntryException {
		if (loginNameIsAlreadyTaken(loginName)) {
			throw new DuplicateDBEntryException("Login name already taken");
		}
		
		User user = new User();
		DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss z");
		Date date = Calendar.getInstance().getTime();
		String created = dateFormat.format(date);
		
		user.setLoginName(loginName);
		user.setPassword(password);
		user.setDisplayName(displayName);
		user.setCreationDate(created);
		
		persist(user);
	}
	
	
	
	//Methods used to update an entry in the DB
	
	/**
	 * Changes the user's display name.
	 * @param loginName login name of the user.
	 * @param newDisplayName new display name of the user.
	 * @throws NoDBEntryException if the user does not exist.
	 */
	public void changeUserDisplayName(String loginName, String newDisplayName) throws NoDBEntryException {
		if (userIsNotInDB(loginName)) {
			throw new NoDBEntryException("Error changing display name. The user does not exist.");
		}
		
		//create EntityManager
		EntityManager em = emf.createEntityManager();
		
		//create update
    	CriteriaBuilder cb = em.getCriteriaBuilder();
    	CriteriaUpdate<User> update = cb.createCriteriaUpdate(User.class);
    	
    	//set the root class
    	Root<User> user = update.from(User.class);
    	
    	//set the update and where clause
    	update.set("displayName", newDisplayName);
    	Predicate loginNameMatches = cb.equal(user.get("loginName"), loginName);
    	update.where(loginNameMatches);
    	
    	//change displayName
    	EntityTransaction tx = em.getTransaction();
    	tx.begin();
    	em.createQuery(update).executeUpdate();
    	tx.commit();
    	em.close();
	}
	
	/**
	 * Changes the user password.
	 * @param loginName login name of the user.
	 * @param newPassword new password of the user.
	 * @throws NoDBEntryException if the user does not exist.
	 */
	public void changeUserPassword(String loginName, String newPassword) throws NoDBEntryException {
		if (userIsNotInDB(loginName)) {
			throw new NoDBEntryException("Error changing password. The given user does not exist.");
		}
		
		//create update
		EntityManager em = emf.createEntityManager();
    	CriteriaBuilder cb = em.getCriteriaBuilder();
    	CriteriaUpdate<User> update = cb.createCriteriaUpdate(User.class);
    	
    	//set the root class
    	Root<User> user = update.from(User.class);
    	
    	//set the update and where clause
    	update.set("password", newPassword);
    	Predicate loginNameMatches = cb.equal(user.get("loginName"), loginName);
    	update.where(loginNameMatches);
    	
    	//change displayName
    	EntityTransaction tx = em.getTransaction();
    	tx.begin();
    	em.createQuery(update).executeUpdate();
    	tx.commit();
    	em.close();
	}
	
	
	
	//Methods used to get something from the DB
	
	/**
	 * Returns a User with the given loginName.
	 * @param loginName loginName of the user.
	 * @return result a user if it exists.
	 * @throws NoDBEntryException if the user with the given loginName does not exist.
	 */
	public User getUserByLoginName(String loginName) throws NoDBEntryException {
		EntityManager em = emf.createEntityManager();
		
		//create criteria
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<User> cq = cb.createQuery(User.class);
		Root<User> user = cq.from(User.class);
		
		//set the predicate
		Predicate loginNameMatches = cb.equal(user.get("loginName"), loginName);
		
		//select
		cq.select(user).where(loginNameMatches);
		TypedQuery<User> query = em.createQuery(cq);
		ArrayList<User> result = new ArrayList<User>(query.getResultList());
		em.close();
		
		if (result.size() > 0) {
			return result.get(0);
		} else {
			throw new NoDBEntryException("The user with the given loginName does not exist.");
		}
	}
	
	
	
	//Methods used to delete an entry in the DB
	
	/**
	 * Deletes a user from the DB.
	 * @param loginName the users loginName.
	 * @param password the users password.
	 * @throws NoDBEntryException if the user does not exist.
	 */
	public void deleteUser(String loginName) throws NoDBEntryException {
		if (userIsNotInDB(loginName)) {
			throw new NoDBEntryException("Error deleting user. User does not exist.");
		}
		
		EntityManager em = emf.createEntityManager();
		
    	//create criteria delete
    	CriteriaBuilder cb = em.getCriteriaBuilder();
    	CriteriaDelete<User> cd = cb.createCriteriaDelete(User.class);
    	Root<User> user = cd.from(User.class);
    	
    	//set predicate
		Predicate loginNameMatches = cb.equal(user.get("loginName"), loginName);
		
		//select
		cd.where(loginNameMatches);
		
		//delete
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.createQuery(cd).executeUpdate();
		tx.commit();
		em.close();
	}
	
	
	//Helper methods
	
	/**
	 * Checks if the user is not in the DB.
	 * @param loginName login name of the user
	 * @return true if user is not in the DB, else false.
	 */
	public boolean userIsNotInDB(String loginName) {
		//create EntityManager and criteria
		EntityManager em = emf.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<User> cq = cb.createQuery(User.class);
		Root<User> user = cq.from(User.class);
		cq.select(user).where(cb.equal(user.get("loginName"), loginName));
		
		TypedQuery<User> query = em.createQuery(cq);
		return query.getResultList().size() == 0;
	}
	
	/**
	 * Checks if the login name is already taken.
	 * @param loginName search for that login name.
	 * @return true if it is taken, false else.
	 */
	public boolean loginNameIsAlreadyTaken(String loginName) {
		//check if the login name is already taken
		EntityManager em = emf.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<User> cq = cb.createQuery(User.class);
		Root<User> users = cq.from(User.class);
		cq.select(users).where(cb.equal(users.get("loginName"), loginName));
		
		TypedQuery<User> query = em.createQuery(cq);
		ArrayList<User> result = new ArrayList<User>(query.getResultList());
		em.close();
		
		return result.size() > 0;
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
