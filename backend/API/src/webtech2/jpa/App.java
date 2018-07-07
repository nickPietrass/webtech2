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
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import webtech2.jpa.entities.User;
import webtech2.jpa.exceptions.DuplicateDBEntryException;
import webtech2.jpa.exceptions.NoDBEntryException;

public class App {
	
	private EntityManagerFactory emf;
	
	public App() {
		this.emf = Persistence.createEntityManagerFactory("tudoo-persistence-unit");
	}
	
	
	
	//Methods used to persist something in the DB
	
	/**
	 * Persists a new user in the database. This method will check if the user is already in the database.
	 * @param user new user to be added to the database.
	 * @throws DuplicateDBEntryException if the given user's UUID already exists.
	 */
	public void registerNewUser(User user) throws DuplicateDBEntryException{
		EntityManager em = emf.createEntityManager();
		
		//Checks if the user is already in the DB via the userUUID
		if (em.find(User.class, user.getUserUUID()).equals(null) ) {
			persist(user);
		} else {
			throw new DuplicateDBEntryException("User already exists");
		}
	}
	
	/**
	 * Persists a new user in the database and gives it a new UUID.
	 * @param loginName loginname of the user
	 * @param password password of the user
	 * @param displayName displayname of the user
	 */
	public void registerNewUser(String loginName, String password, String displayName) {
		User user = new User();
		UUID userUUID = UUID.randomUUID();
		DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss z");
		Date date = Calendar.getInstance().getTime();
		String created = dateFormat.format(date);
		
		user.setUserUUID(userUUID);
		user.setLoginName(loginName);
		user.setPassword(password);
		user.setDisplayName(displayName);
		user.setCreationDate(created);
		
		persist(user);
	}
	
	
	
	//Methods used to update an entry in the DB
	
	
	
	
	
	//Methods used to get something from the DB
	
	/**
	 * Returns a user by his userUUID.
	 * @param userUUID userUUID.
	 * @return user if the user with the userUUID is in the database.
	 * @throws NoDBEntryException if the given userUUID and its user does not exists.
	 */
	public User getUserById(UUID userUUID) throws NoDBEntryException {
		EntityManager em = emf.createEntityManager();
		
		User user = em.find(User.class, userUUID);
		em.close();
		
		if (user == null) {
			throw new NoDBEntryException("There exists no user with the given UUID: " + userUUID);
		} else {
			return user;
		}
	}
	
	/**
	 * Returns a list of all users.
	 * @return users all users.
	 * @throws NoDBEntryException if there aren't any users registered.
	 */
	public ArrayList<User> getAllUsers() throws NoDBEntryException {
		EntityManager em = emf.createEntityManager();
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<User> cq = cb.createQuery(User.class);
		Root<User> users = cq.from(User.class);
		cq.select(users);
		
		TypedQuery<User> query = em.createQuery(cq);
		ArrayList<User> result = new ArrayList<>(query.getResultList());
		em.close();
		
		if (result.size() > 0) {
			return result;
		} else {
			throw new NoDBEntryException("There aren't any users registered.");
		}
	}
	
	/**
	 * Returns a list of all users with the given display name.
	 * @param displayName display name to search for.
	 * @return users with the given display name.
	 * @throws NoDBEntryException if there aren't any users with the given display name.
	 */
	public ArrayList<User> getUsersByDisplayName(String displayName) throws NoDBEntryException{
		EntityManager em = emf.createEntityManager();
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<User> cq = cb.createQuery(User.class);
		Root<User> users = cq.from(User.class);
		cq.select(users).where(cb.equal(users.get("displayName"), displayName));
		
		TypedQuery<User> query = em.createQuery(cq);
		ArrayList<User> result = new ArrayList<>(query.getResultList());
		em.close();
		
		if (result.size() > 0) {
			return result;
		} else {
			throw new NoDBEntryException("The user(s) with the given display name: " + displayName + " do(es) not exist.");
		}
	}
	
	/**
	 * Returns a User with the given loginName and password.
	 * @param loginName loginName of the user.
	 * @param password password of the user.
	 * @return result a user if it exists.
	 * @throws NoDBEntryException if the user with the given loginName and password does not exist.
	 */
	public User getUserByLoginNameAndPassword(String loginName, String password) throws NoDBEntryException {
		EntityManager em = emf.createEntityManager();
		
		//create criteria
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<User> cq = cb.createQuery(User.class);
		Root<User> user = cq.from(User.class);
		
		//set the predicate
		Predicate loginNameAndPasswordMatches = cb.and(cb.equal(user.get("loginName"), loginName), cb.equal(user.get("password"), password));
		
		//select
		cq.select(user).where(loginNameAndPasswordMatches);
		TypedQuery<User> query = em.createQuery(cq);
		User result = query.getResultList().get(0);
		em.close();
		
		if (result != null) {
			return result;
		} else {
			throw new NoDBEntryException("The user with the given loginName and password does not exist.");
		}
	}
	
	
	
	//Methods used to delete an entry in the DB
	
	/**
	 * Deletes a user from the DB.
	 * @param loginName the users loginName.
	 * @param password the users password.
	 */
	public void deleteUser(String loginName, String password) {
		EntityManager em = emf.createEntityManager();
    	
    	//create criteria delete
    	CriteriaBuilder cb = em.getCriteriaBuilder();
    	CriteriaDelete<User> cd = cb.createCriteriaDelete(User.class);
    	Root<User> user = cd.from(User.class);
    	
    	//set predicate
		Predicate loginNameAndPasswordMatches = cb.and(cb.equal(user.get("loginName"), loginName), cb.equal(user.get("password"), password));
		
		//select
		cd.where(loginNameAndPasswordMatches);
		
		//delete
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.createQuery(cd).executeUpdate();
		tx.commit();
		em.close();
	}
	
	
	//Helper methods
	
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
