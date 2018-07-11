package webtech2.rest.temporary.params;

/**
 * Temporary storage for HTTP body data of a password change request, display name change request etc.
 * @author Ilja
 */
public class SerializableParam {
	private String param;

	/**
	 * Empty constructor for Jax-RS.
	 */
	public SerializableParam() {}
	
	public SerializableParam(String sessionID, String newPassword) {
		this.param = newPassword;
	}
	
	public String getNewPassword() {
		return param;
	}

	public void setNewPassword(String newPassword) {
		this.param = newPassword;
	}
}
