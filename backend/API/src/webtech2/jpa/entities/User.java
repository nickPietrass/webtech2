package webtech2.jpa.entities;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {
	
	@Id
	private UUID userUUID;
	
	private String loginName;
	private String password;
	private String displayName;
	private String created;
	
	
	
	public UUID getUserUUID() {
		return userUUID;
	}
	public String getLoginName() {
		return loginName;
	}
	public String getPassword() {
		return password;
	}
	public String getDisplayName() {
		return displayName;
	}
	public String getCreationDate() {
		return created;
	}
	
	public void setUserUUID(UUID userUUID) {
		this.userUUID = userUUID;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public void setCreationDate(String created) {
		this.created = created;
	}
}
