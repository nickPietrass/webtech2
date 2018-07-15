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

import webtech2.jpa.entities.RolesPermission;
import webtech2.jpa.exceptions.NoDBEntryException;

public class PermissionApp {
	
	private EntityManagerFactory emf;
	
	public PermissionApp(EntityManagerFactory emf) {
		this.emf = emf;
	}
	
	public void close() {
		emf.close();
	}
	
	public RolesPermission getPermissionByID(int id) throws NoDBEntryException {
		EntityManager em = emf.createEntityManager();
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<RolesPermission> cq = cb.createQuery(RolesPermission.class);
		Root<RolesPermission> rolesPermission = cq.from(RolesPermission.class);
		
		Predicate permissionIDMatches = cb.equal(rolesPermission.get("id"), id);
		
		cq.select(rolesPermission).where(permissionIDMatches);
		TypedQuery<RolesPermission> query = em.createQuery(cq);
		ArrayList<RolesPermission> result = new ArrayList<RolesPermission>(query.getResultList());
		em.close();
		
		if (result.size() > 0) {
			return result.get(0);
		} else {
			throw new NoDBEntryException("The RolePermission does not exist");
		}
	}
	
	public void changeRolesPermission(String id, String newPermission) throws NoDBEntryException {
		if (rolesPermissionIsNotInDB(id)) {
			throw new NoDBEntryException("Error changing permission. The given RolesPermission does not exist.");
		}
		
		//create update
		EntityManager em = emf.createEntityManager();
    	CriteriaBuilder cb = em.getCriteriaBuilder();
    	CriteriaUpdate<RolesPermission> update = cb.createCriteriaUpdate(RolesPermission.class);
    	
    	//set the root class
    	Root<RolesPermission> rolesPermission = update.from(RolesPermission.class);
    	
    	//set the update and where clause
    	update.set("permission", newPermission);
    	Predicate permissionIDMatches = cb.equal(rolesPermission.get("id"), id);
    	update.where(permissionIDMatches);
    	
    	//change displayName
    	EntityTransaction tx = em.getTransaction();
    	tx.begin();
    	em.createQuery(update).executeUpdate();
    	tx.commit();
    	em.close();
	}
	
	public void changeRoleName(String id, String newRoleName) throws NoDBEntryException {
		if (rolesPermissionIsNotInDB(id)) {
			throw new NoDBEntryException("Error changing permission. The given RolesPermission does not exist.");
		}
		
		//create update
		EntityManager em = emf.createEntityManager();
    	CriteriaBuilder cb = em.getCriteriaBuilder();
    	CriteriaUpdate<RolesPermission> update = cb.createCriteriaUpdate(RolesPermission.class);
    	
    	//set the root class
    	Root<RolesPermission> rolesPermission = update.from(RolesPermission.class);
    	
    	//set the update and where clause
    	update.set("roleName", newRoleName);
    	Predicate permissionIDMatches = cb.equal(rolesPermission.get("id"), id);
    	update.where(permissionIDMatches);
    	
    	//change displayName
    	EntityTransaction tx = em.getTransaction();
    	tx.begin();
    	em.createQuery(update).executeUpdate();
    	tx.commit();
    	em.close();
	}
	
	
	public void deleteRolesPermission(String id) throws NoDBEntryException {
		if (rolesPermissionIsNotInDB(id)) {
			throw new NoDBEntryException("Error deleting RolesPermission. RolesPermission does not exist.");
		}
		
		EntityManager em = emf.createEntityManager();
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaDelete<RolesPermission> cd = cb.createCriteriaDelete(RolesPermission.class);
		Root<RolesPermission> rolesPermission = cd.from(RolesPermission.class);
		
		Predicate permissionIDMatches = cb.equal(rolesPermission.get("id"), id);
		
		cd.where(permissionIDMatches);
		
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.createQuery(cd).executeUpdate();
		tx.commit();
		em.close();
	}
	
	
	
	public boolean rolesPermissionIsNotInDB(String id) {
		//create EntityManager and criteria
		EntityManager em = emf.createEntityManager();
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<RolesPermission> cq = cb.createQuery(RolesPermission.class);
		Root<RolesPermission> user = cq.from(RolesPermission.class);
		cq.select(user).where(cb.equal(user.get("id"), id));
		
		TypedQuery<RolesPermission> query = em.createQuery(cq);
		return query.getResultList().size() == 0;
	}
}
