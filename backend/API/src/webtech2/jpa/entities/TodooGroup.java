package webtech2.jpa.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class TodooGroup {
	
	@Id
	private UUID groupUUID;
	
	private String groupName;
	@OneToOne
	private User groupOwner;
	private ArrayList<User> groupMembers;
	
	
	public UUID getGroupUUID() {
		return groupUUID;
	}
	public String getGroupName() {
		return groupName;
	}
	public User getGroupOwner() {
		return groupOwner;
	}
	public ArrayList<User> getGroupMembers() {
		return groupMembers;
	}
	
	public void setGroupUUID(UUID groupUUID) {
		this.groupUUID = groupUUID;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public void setGroupOwner(User groupOwner) {
		this.groupOwner = groupOwner;
	}
	public void setGroupMembers(ArrayList<User> arrayList) {
		this.groupMembers = arrayList;
	}
}
