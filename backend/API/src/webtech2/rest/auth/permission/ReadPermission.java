package webtech2.rest.auth.permission;

/**
 * Provides a user with reading permissions for a specific tudoo. (no groups here)
 * @author Ilja
 */
public class ReadPermission{
	/**
	 * The tudooID this permission is valid for.
	 */
	private String tudooID;

	public ReadPermission(String tudooID) {
		this.tudooID = tudooID;
	}
	
	public String getBelongingTudooID(){
		return tudooID;
	}
}
