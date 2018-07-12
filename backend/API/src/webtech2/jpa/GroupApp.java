package webtech2.jpa;

import javax.persistence.EntityManagerFactory;

import webtech2.jpa.entities.User;

public class GroupApp {
	
	private EntityManagerFactory emf;
	
	public GroupApp(EntityManagerFactory emf) {
		this.emf = emf;
	}
	
	public void registerNewGroup(User owner) {
		
	}
}
