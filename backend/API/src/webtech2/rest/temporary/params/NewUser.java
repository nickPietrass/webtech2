package webtech2.rest.temporary.params;

/**
 * Temporary storage for HTTP body data of a register request.
 * @author Ilja
 */
public class NewUser {
	private String loginName, password, displayName;
	
	/**
	 * Empty constructor for Jax-RS.
	 */
	public NewUser() {}

	public NewUser(String loginName, String password, String displayName) {
		this.loginName = loginName;
		this.password = password;
		this.displayName = displayName;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
