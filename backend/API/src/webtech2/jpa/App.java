package webtech2.jpa;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import webtech2.jpa.entities.User;

public class App {
	
	private EntityManager entityManager;
	private EntityTransaction entityTransaction;
	
	public App() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("tudoo-persistence-unit");
		this.entityManager = emf.createEntityManager();
		this.entityTransaction = entityManager.getTransaction();
	}
	
	/**
	 * Persists a new user in the database. This method will check if the user is already in the database.
	 * @param user new user to be added to the database.
	 */
	public void registerNewUser(User user) {
		//Checks if the user is already in the DB via the userUUID
		if (entityManager.find(User.class, user.getUserUUID()).equals(null) ) {
			persist((Entity) user);
		}
	}
	
	public void registerNewUser(String loginName, String password, String displayName) {
		User user = new User();
		UUID userUUID = UUID.randomUUID();
		DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD-hh-mm-ss");
		Date date = Calendar.getInstance().getTime();
		String created = dateFormat.format(date);
		
		user.setUserUUID(userUUID);
		user.setLoginName(loginName);
		user.setPassword(password);
		user.setDisplayName(displayName);
		user.setCreationDate(created);
		
		persist(user);
	}
	
	/**
	 * Returns a user by his userUUID.
	 * @param userUUID userUUID.
	 * @return user if the user with the userUUID is in the database, null else.
	 */
	public User getUserById(UUID userUUID) {
		User user = entityManager.find(User.class, userUUID);
		return user;
	}
	
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
