package webtech2.jpa.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table
public class TudooGroup {
	
	@Id
	private String groupUUID;
	
	private String groupName;
	@OneToOne
	private User groupOwner;
	private ArrayList<String> groupMembers;
	
	
	public String getGroupUUID() {
		return groupUUID;
	}
	public String getGroupName() {
		return groupName;
	}
	public User getGroupOwner() {
		return groupOwner;
	}
	public ArrayList<String> getGroupMembers() {
		return groupMembers;
	}
	
	public void setGroupUUID(String groupUUID) {
		this.groupUUID = groupUUID;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public void setGroupOwner(User groupOwner) {
		this.groupOwner = groupOwner;
	}
	public void setGroupMembers(ArrayList<String> arrayList) {
		this.groupMembers = arrayList;
	}
}
