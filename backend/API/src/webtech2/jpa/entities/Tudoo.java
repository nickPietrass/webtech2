package webtech2.jpa.entities;

import java.util.ArrayList;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Tudoo {
	
	@Id
	private String tudooUUID;
	
	private String title;
	private String content;
	
	@OneToOne
	private User tudooOwner;

	private ArrayList<String> visibleBy;
	private ArrayList<String> editableBy;
	
	private String created;
	
	
	
	public String getTudooUUID() {
		return tudooUUID;
	}
	public String getTitle() {
		return title;
	}
	public String getContent() {
		return content;
	}
	public User getTudooOwner() {
		return tudooOwner;
	}
	public ArrayList<String> getVisibleBy() {
		return visibleBy;
	}
	public ArrayList<String> getEditableBy() {
		return editableBy;
	}
	public String getCreationDate() {
		return created;
	}
	
	public void setTudooUUID(String todoUUID) {
		this.tudooUUID = todoUUID;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public void setTudooOwner(User todoOwner) {
		this.tudooOwner = todoOwner;
	}
	public void setVisibleBy(ArrayList<String> visibleBy) {
		this.visibleBy = visibleBy;
	}
	public void setEditableBy(ArrayList<String> editableBy) {
		this.editableBy = editableBy;
	}
	public void setCreationDate(String created) {
		this.created = created;
	}
}
