package webtech2.rest.storage;

/**
 * Temporary storage for HTTP body data of a login/register request.
 * @author Ilja
 */
public class SerializableUserID {
	private String username, password;
	
	/**
	 * Empty constructor for Jax-RS.
	 */
	public SerializableUserID() {}

	public SerializableUserID(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
