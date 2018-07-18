package webtech2.rest.temporary.params;

/**
 * Temporary storage for HTTP body data of a tudoo permission change request.
 * @author Ilja
 */
public class TudooPermissionChange {
	private String tudooUUID,targetID;
	private int permissionLevel;
	
	/**
	 * Empty constructor for Jax-RS.
	 */
	public TudooPermissionChange() {}

	public TudooPermissionChange(String tudooUUID, String targetID, int permissionLevel) {
		this.tudooUUID = tudooUUID;
		this.targetID = targetID;
		this.permissionLevel = permissionLevel;
	}

	public String getTudooUUID() {
		return tudooUUID;
	}

	public void setTudooUUID(String tudooUUID) {
		this.tudooUUID = tudooUUID;
	}

	public String getTargetID() {
		return targetID;
	}

	public void setTargetID(String targetID) {
		this.targetID = targetID;
	}

	public int getPermissionLevel() {
		return permissionLevel;
	}

	public void setPermissionLevel(int permissionLevel) {
		this.permissionLevel = permissionLevel;
	}
	
}
