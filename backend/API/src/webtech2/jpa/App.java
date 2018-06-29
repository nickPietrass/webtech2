package webtech2.jpa;

import java.util.UUID;

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
	 * Persist a new user in the database. This method will check if the user is already in the database.
	 * @param user new user to be added to the database.
	 */
	public void persistNewUser(User user) {
		entityTransaction.begin();
		
		//Checks if the user is already in the DB via the userUUID
		if (entityManager.find(User.class, user.getUserUUID()).equals(null) ) {
			entityManager.persist(user);
		}
		
		entityTransaction.commit();
	}
	public User getUserById(UUID userUUID) {
		User user = entityManager.find(User.class, userUUID);
		return user;
	}
}
