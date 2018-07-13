package webtech2.rest.temporary;

/**
 * Temporary storage for HTTP body data of a login request.
 * @author Ilja
 */
public class SerializableUserID {
	private String loginName, password;
	
	/**
	 * Empty constructor for Jax-RS.
	 */
	public SerializableUserID() {}
	
	public SerializableUserID(String loginName, String password) {
		this.loginName = loginName;
		this.password = password;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}



	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
