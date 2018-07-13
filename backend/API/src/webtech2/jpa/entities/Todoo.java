package webtech2.jpa.entities;

import java.util.ArrayList;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Todoo {
	
	@Id
	@GeneratedValue
	private UUID todooUUID;
	
	private String title;
	private String content;
	
	@OneToOne
	private User todooOwner;
	
	@OneToMany
	private ArrayList<User> visibleBy;
	
	@OneToMany
	private ArrayList<User> editableBy;
	
	private String created;
	
	
	
	public UUID getTodooUUID() {
		return todooUUID;
	}
	public String getTitle() {
		return title;
	}
	public String getContent() {
		return content;
	}
	public User getTodooOwner() {
		return todooOwner;
	}
	public ArrayList<User> getVisibleBy() {
		return visibleBy;
	}
	public ArrayList<User> editableBy() {
		return editableBy;
	}
	public String getCreationDate() {
		return created;
	}
	
	public void setTodooUUID(UUID todoUUID) {
		this.todooUUID = todoUUID;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public void setTodooOwner(User todoOwner) {
		this.todooOwner = todoOwner;
	}
	public void setVisibleBy(ArrayList<User> visibleBy) {
		this.visibleBy = visibleBy;
	}
	public void setEditableBy(ArrayList<User> editableBy) {
		this.editableBy = editableBy;
	}
	public void setCreationDate(String created) {
		this.created = created;
	}
}
