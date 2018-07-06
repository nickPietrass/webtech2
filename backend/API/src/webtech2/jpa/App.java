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
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import webtech2.jpa.entities.User;
import webtech2.jpa.exceptions.DuplicateDBEntryException;
import webtech2.jpa.exceptions.NoDBEntryException;

public class App {
	
	private EntityManager entityManager;
	private EntityTransaction entityTransaction;
	
	public App() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("tudoo-persistence-unit");
		this.entityManager = emf.createEntityManager();
		this.entityTransaction = entityManager.getTransaction();
	}
	
	
	
	//Methods used to persist something in the DB
	
	/**
	 * Persists a new user in the database. This method will check if the user is already in the database.
	 * @param user new user to be added to the database.
	 * @throws DuplicateDBEntryException if the given user's UUID already exists.
	 */
	public void registerNewUser(User user) throws DuplicateDBEntryException{
		//Checks if the user is already in the DB via the userUUID
		if (entityManager.find(User.class, user.getUserUUID()).equals(null) ) {
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
	
	
	
	//Methods used to get something from the DB
	
	/**
	 * Returns a user by his userUUID.
	 * @param userUUID userUUID.
	 * @return user if the user with the userUUID is in the database.
	 * @throws NoDBEntryException if the given userUUID and its user does not exists.
	 */
	public User getUserById(UUID userUUID) throws NoDBEntryException {
		User user = entityManager.find(User.class, userUUID);
		
		if (user == null) {
			throw new NoDBEntryException("There exists no user with the given UUID: " + userUUID);
		} else {
			return user;
		}
	}
	
	public ArrayList<User> getAllUsers() throws NoDBEntryException {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<User> cq = cb.createQuery(User.class);
		Root<User> users = cq.from(User.class);
		cq.select(users);
		
		TypedQuery<User> query = entityManager.createQuery(cq);
		ArrayList<User> result = new ArrayList<>(query.getResultList());
		
		if (result.size() > 0) {
			return result;
		} else {
			throw new NoDBEntryException("There aren't any users registered.");
		}
	}
	
	public ArrayList<User> getUserByDisplayName(String displayName) {
		ArrayList<User> result = new ArrayList<>();
		
		//ToDo
		
		return result;
	}
	
	
	
	//Helper methods
	
	/**
	 * Helper function to persist an entity in the database. This method does not check for duplicates.
	 * @param entity some entity that needs to be persisted.
	 */
	private void persist(Object entity) {
		entityTransaction.begin();
		entityManager.persist(entity);
		entityTransaction.commit();
	}
}
