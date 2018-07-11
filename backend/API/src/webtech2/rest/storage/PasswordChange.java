package webtech2.rest.storage;

/**
 * Temporary storage for HTTP body data of a password change request.
 * @author Illya
 *
 */
public class PasswordChange {
	private String sessionID,newPassword;

	/**
	 * Empty constructor for Jax-RS.
	 */
	public PasswordChange() {}
	
	public PasswordChange(String sessionID, String newPassword) {
		super();
		this.sessionID = sessionID;
		this.newPassword = newPassword;
	}
	
	public String getSessionID() {
		return sessionID;
	}

	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
}
