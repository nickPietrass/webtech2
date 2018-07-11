package webtech2.rest.storage;

/**
 * Temporary storage for HTTP body data of a username change request.
 * @author Ilja
 */
public class UsernameChange {
	private String sessionID,newName;

	/**
	 * Empty constructor for Jax-RS.
	 */
	public UsernameChange() {}

	public UsernameChange(String sessionID, String newName) {
		super();
		this.sessionID = sessionID;
		this.newName = newName;
	}

	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	public String getNewName() {
		return newName;
	}

	public void setNewName(String newName) {
		this.newName = newName;
	}
	
}
