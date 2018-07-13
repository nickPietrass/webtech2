package webtech2.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import webtech2.jpa.entities.Todoo;
import webtech2.jpa.entities.TodooGroup;
import webtech2.jpa.entities.User;
import webtech2.jpa.exceptions.NoDBEntryException;

public class TodooApp {
	
	private EntityManagerFactory emf;
	
	public TodooApp(EntityManagerFactory emf) {
		this.emf = emf;
	}
	
	public void close() {
		emf.close();
	}
	
	public void registerNewTodoo(User owner) throws NoDBEntryException {
		EntityManager em = emf.createEntityManager();
		App app = new App();
		
		User managedUser = app.getUserByLoginName(owner.getLoginName());
		
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Todoo> cq = cb.createQuery(Todoo.class);
		Root<Todoo> t = cq.from(Todoo.class);
		
		cq.select(t).where(cb.equal(t.get("todooOwner"), managedUser));
		TypedQuery<Todoo> query = em.createQuery(cq);
		
		
	}
}
