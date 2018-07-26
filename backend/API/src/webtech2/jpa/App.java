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

import webtech2.jpa.entities.Tudoo;
import webtech2.jpa.entities.TudooGroup;
import webtech2.jpa.entities.User;
import webtech2.jpa.exceptions.DuplicateDBEntryException;
import webtech2.jpa.exceptions.NoDBEntryException;

public class App {
	
	private EntityManagerFactory emf;
	private GroupApp groupApp;
	private TudooApp tudooApp;
	private PermissionApp permissionApp;
	private UserRoleApp userRoleApp;
	
	public static App instance;
	
	/**
	 * Standard constructor
	 */
	public App() {
		/**
		 * Give the EntityMangagerFactory information about the persistence context. "tudoo-persistence-unit" is configured in WebContent/META-INF/persistence.xml
		 * In other words: Its properties. Tells the EntityManagerFactory what it is, where the DB is and how it should behave. 
		 */
		this.emf = Persistence.createEntityManagerFactory("tudoo-persistence-unit");
		this.groupApp = new GroupApp(emf);
		this.tudooApp = new TudooApp(emf);
		this.permissionApp = new PermissionApp(emf);
		this.userRoleApp = new UserRoleApp(emf);
		instance = this;
	}
	
	/**
	 * Method used to close App
	 */
	public void close() {
		groupApp.close();
		tudooApp.close();
		emf.close();
	}
	
	/**
	 * Get the groupApp
	 * @return groupApp
	 */
	public GroupApp getGroupApp() {
		return groupApp;
	}
	
	/**
	 * Get the tudooApp
	 * @return tudooApp
	 */
	public TudooApp getTudooApp() {
		return tudooApp;
	}
	
	//Methods used to persist something in the DB
	
	/**
	 * Persists a new user in the database and gives it a new UUID.
	 * @param loginName login name of the user
	 * @param password password of the user
	 * @param displayName display name of the user
	 */
	public void registerNewUser(String loginName, String password, String displayName, String salt) throws DuplicateDBEntryException {
		if (loginNameIsAlreadyTaken(loginName)) {
			throw new DuplicateDBEntryException("Login name already taken");
		}
		
		//create empty User object
		User user = new User();
		
		//format date
		DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss z");
		Date date = Calendar.getInstance().getTime();
		String created = dateFormat.format(date);
		
		//set User information
		user.setLoginName(loginName);
		user.setPassword(password);
		user.setDisplayName(displayName);
		user.setCreationDate(created);
		user.setSalt(salt);
		
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
		/**
		 * emf already knows what context it lies in. The created EntityManager will handle everything within this context.
		 */
		EntityManager em = emf.createEntityManager();
		
		//create update
		/**
		 * Criteria API. Every CriteriaQuery/CriteriaUpdate/CriteriaDelete needs a type, in this case: User.
		 */
    	CriteriaBuilder cb = em.getCriteriaBuilder();
    	CriteriaUpdate<User> update = cb.createCriteriaUpdate(User.class);
    	
    	//set the root class
    	/**
    	 * Every CriteriaQuery/CriteriaUpdate/CriteriaDelete needs a Root<T>. This is equivalent to SQL's "FROM user [alias of your choice]"
    	 */
    	Root<User> user = update.from(User.class);
    	
    	//set the update and where clause
    	update.set("displayName", newDisplayName);
    	/**
    	 * Predicate is a combined boolean expression
    	 * This is equivalent to SQL's "user.loginName = loginName"
    	 */
    	Predicate loginNameMatches = cb.equal(user.get("loginName"), loginName);
    	
    	/**
    	 * Equivalent to SQL's "WHERE user.loginName = loginName"
    	 */
    	update.where(loginNameMatches);
    	
    	//change displayName
    	/**
    	 * Every object that needs to be persisted/updated/deleted must be in a transaction context.
    	 * If something goes wrong before and between begin() and commit(), then the changes will not be persisted.
    	 */
    	EntityTransaction tx = em.getTransaction();
    	tx.begin();
    	
    	/**
    	 * Update needs to be executed via executeUpdate().
    	 * createQuery only creates a query, duh.
    	 */
    	em.createQuery(update).executeUpdate();
    	tx.commit();
    	
    	/**
    	 * Release all resources.
    	 */
    	em.close();
    	
    	/**
    	 * Now that I think about it... I should have put everything between begin() and commit().
    	 * Also there exists an @Transactional annoation that will handle everything about transactions for you.
    	 */
	}
	
	/**
	 * Changes the user password.
	 * @param loginName login name of the user.
	 * @param newPassword new password of the user.
	 * @throws NoDBEntryException if the user does not exist.
	 */
	public void changeUserPassword(String loginName, String newPassword, String newSalt) throws NoDBEntryException {
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
    	
    	update.set("salt", newSalt);
    	update.where(loginNameMatches);
    	
    	//change displayName
    	tx = em.getTransaction();
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
		
		/**
		 * Don't forget the Root<T>
		 */
		Root<User> user = cq.from(User.class);
		
		//set the predicate
		Predicate loginNameMatches = cb.equal(user.get("loginName"), loginName);
		
		//select
		/**
		 * Equivalent to SQL's "SELECT * FROM user WHERE user.loginName = loginName"
		 */
		cq.select(user).where(loginNameMatches);
		
		/**
		 * Even the Query needs a type.
		 */
		TypedQuery<User> query = em.createQuery(cq);
		ArrayList<User> result = new ArrayList<User>(query.getResultList());
		
		/**
		 * An example of why the context is important:
		 * If you em.close() at 255 and called query.getResultList() at 257, you will get an exception.
		 * Everything you fetched with the CriteriaQuery will be lost.
		 */
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
		
		User user = getUserByLoginName(loginName);
		ArrayList<Tudoo> visibleTudoos = tudooApp.getAllVisibleTudoosOfUser(loginName);
		ArrayList<TudooGroup> groups = groupApp.getGroupsWhereUserIsPartOf(loginName);
		
		for (Tudoo tudoo : visibleTudoos) {
			tudooApp.deleteUserOrGroupFromTudoo(tudoo.getTudooUUID(), loginName);
		}
		for (TudooGroup g : groups) {
			groupApp.deleteUserFromGroup(g.getGroupUUID(), loginName);
		}
		
		//Removes groups where user is the owner
		//create criteria delete
    	CriteriaBuilder cb = em.getCriteriaBuilder();
    	
    	/**
    	 * CriteriaDelete analogous to CriteriaUpdate
    	 */
    	CriteriaDelete<TudooGroup> cdg = cb.createCriteriaDelete(TudooGroup.class);
    	Root<TudooGroup> g = cdg.from(TudooGroup.class);
    	
    	//set predicate
		Predicate userIsOwnerOfGroup = cb.equal(g.get("groupOwner"), user);
		
		//select
		cdg.where(userIsOwnerOfGroup);
		
		//delete
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.createQuery(cdg).executeUpdate();
		tx.commit();
		
		//Removes tudoos where user is the owner
		CriteriaDelete<Tudoo> cdt = cb.createCriteriaDelete(Tudoo.class);
		Root<Tudoo> t = cdt.from(Tudoo.class);
		
		//set predicate
		Predicate userIsOwnerOfTudoo = cb.equal(t.get("tudooOwner"), user);
		
		//select
		cdt.where(userIsOwnerOfTudoo);
		
		//delete
		tx = em.getTransaction();
		tx.begin();
		em.createQuery(cdt).executeUpdate();
		tx.commit();
		
		
		//Removes the user from the user table
    	//create criteria delete
    	cb = em.getCriteriaBuilder();
    	CriteriaDelete<User> cdu = cb.createCriteriaDelete(User.class);
    	Root<User> u = cdu.from(User.class);
    	
    	//set predicate
		Predicate loginNameMatches = cb.equal(u.get("loginName"), loginName);
		
		//select
		cdu.where(loginNameMatches);
		
		//delete
		tx = em.getTransaction();
		tx.begin();
		em.createQuery(cdu).executeUpdate();
		tx.commit();
		
		em.close();
		
		/**
		 * Yeah... I should have put everything between begin() and commit(). Right now if we unexpectatly shut down at 243,
		 * then the user would have lost all his tudoos and tudooGroups, but the user itself would still be in the DB. Haha, unlucky.
		 */
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
		ArrayList<User> result = new ArrayList<User> (query.getResultList());
		em.close();
		return result.size() == 0;
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
