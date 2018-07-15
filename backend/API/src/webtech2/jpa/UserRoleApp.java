package webtech2.jpa;

import java.util.ArrayList;

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

import webtech2.jpa.entities.UserRole;
import webtech2.jpa.exceptions.NoDBEntryException;

public class UserRoleApp {

private EntityManagerFactory emf;
	
	public UserRoleApp(EntityManagerFactory emf) {
		this.emf = emf;
	}
	
	public void close() {
		emf.close();
	}
	
	public UserRole getUserRoleByID(int id) throws NoDBEntryException {
		EntityManager em = emf.createEntityManager();
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<UserRole> cq = cb.createQuery(UserRole.class);
		Root<UserRole> userRole = cq.from(UserRole.class);
		
		Predicate userRoleIDMatches = cb.equal(userRole.get("id"), id);
		
		cq.select(userRole).where(userRoleIDMatches);
		TypedQuery<UserRole> query = em.createQuery(cq);
		ArrayList<UserRole> result = new ArrayList<UserRole>(query.getResultList());
		em.close();
		
		if (result.size() > 0) {
			return result.get(0);
		} else {
			throw new NoDBEntryException("The UserRole does not exist");
		}
	}
	
	public void changeUserRoleRoleName(String id, String newRoleName) throws NoDBEntryException {
		if (userRoleIsNotInDB(id)) {
			throw new NoDBEntryException("Error changing permission. The given RolesPermission does not exist.");
		}
		
		//create update
		EntityManager em = emf.createEntityManager();
    	CriteriaBuilder cb = em.getCriteriaBuilder();
    	CriteriaUpdate<UserRole> update = cb.createCriteriaUpdate(UserRole.class);
    	
    	//set the root class
    	Root<UserRole> userRole = update.from(UserRole.class);
    	
    	//set the update and where clause
    	update.set("roleName", newRoleName);
    	Predicate userRoleIDMatches = cb.equal(userRole.get("id"), id);
    	update.where(userRoleIDMatches);
    	
    	//change displayName
    	EntityTransaction tx = em.getTransaction();
    	tx.begin();
    	em.createQuery(update).executeUpdate();
    	tx.commit();
    	em.close();
	}
	
	public void changeUserRoleLoginName(String id, String newLoginName) throws NoDBEntryException {
		if (userRoleIsNotInDB(id)) {
			throw new NoDBEntryException("Error changing loginName. The given UserRole does not exist.");
		}
		
		//create update
		EntityManager em = emf.createEntityManager();
    	CriteriaBuilder cb = em.getCriteriaBuilder();
    	CriteriaUpdate<UserRole> update = cb.createCriteriaUpdate(UserRole.class);
    	
    	//set the root class
    	Root<UserRole> userRole = update.from(UserRole.class);
    	
    	//set the update and where clause
    	update.set("loginName", newLoginName);
    	Predicate userRoleIDMatches = cb.equal(userRole.get("id"), id);
    	update.where(userRoleIDMatches);
    	
    	//change displayName
    	EntityTransaction tx = em.getTransaction();
    	tx.begin();
    	em.createQuery(update).executeUpdate();
    	tx.commit();
    	em.close();
	}
	
	
	public void deletesUserRole(String id) throws NoDBEntryException {
		if (userRoleIsNotInDB(id)) {
			throw new NoDBEntryException("Error deleting UserRole. RolesPermission does not exist.");
		}
		
		EntityManager em = emf.createEntityManager();
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaDelete<UserRole> cd = cb.createCriteriaDelete(UserRole.class);
		Root<UserRole> rolesPermission = cd.from(UserRole.class);
		
		Predicate userRoleIDMatches = cb.equal(rolesPermission.get("id"), id);
		
		cd.where(userRoleIDMatches);
		
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.createQuery(cd).executeUpdate();
		tx.commit();
		em.close();
	}
	
	
	
	public boolean userRoleIsNotInDB(String id) {
		//create EntityManager and criteria
		EntityManager em = emf.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<UserRole> cq = cb.createQuery(UserRole.class);
		Root<UserRole> userRole = cq.from(UserRole.class);
		cq.select(userRole).where(cb.equal(userRole.get("id"), id));
		
		TypedQuery<UserRole> query = em.createQuery(cq);
		return query.getResultList().size() == 0;
	}
}
