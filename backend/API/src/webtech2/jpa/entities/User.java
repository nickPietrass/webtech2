package webtech2.jpa.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class User {
	
	/**
	 * Id annotation. This attribute will be the PK in the table
	 * Column name is automatically generated, unless it is annotated with @Table (name = userLoginName)
	 * @GeneratedValue can be annotated to @Id. In this case, the PK will be automatically created by the database itself.
	 * Generated PKs are incrementing IDs
	 */
	@Id
	private String loginName;
	private String password;
	private String displayName;
	private String created;
	private String salt;
	
	
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
	public String getSalt() {
		return salt;
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
	public void setSalt(String salt) {
		this.salt = salt;
	}
}
