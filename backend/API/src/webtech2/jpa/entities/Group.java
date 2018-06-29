package webtech2.jpa.entities;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Group {
	
	@Id
	private UUID groupUUID;
	
	private String groupName;
	@OneToOne
	private User groupOwner;
	
	
	
	public UUID getGroupUUID() {
		return groupUUID;
	}
	public String getGroupName() {
		return groupName;
	}
	public User getGroupOwner() {
		return groupOwner;
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
}
