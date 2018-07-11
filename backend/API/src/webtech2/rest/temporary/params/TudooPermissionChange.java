package webtech2.rest.temporary.params;

/**
 * Temporary storage for HTTP body data of a tudoo permission change request.
 * @author Ilja
 */
public class TudooPermissionChange {
	private String tudooID,targetID;
	private int permissionLevel;
	
	/**
	 * Empty constructor for Jax-RS.
	 */
	public TudooPermissionChange() {}

	public TudooPermissionChange(String tudooID, String targetID, int permissionLevel) {
		this.tudooID = tudooID;
		this.targetID = targetID;
		this.permissionLevel = permissionLevel;
	}

	public String getTudooID() {
		return tudooID;
	}

	public void setTudooID(String tudooID) {
		this.tudooID = tudooID;
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
