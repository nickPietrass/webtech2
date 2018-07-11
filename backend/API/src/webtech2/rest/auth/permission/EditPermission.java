package webtech2.rest.auth.permission;

/**
 * Provides a user with editing permissions for a specific tudoo. (no groups here)
 * @author Ilja
 */
public class EditPermission{
	/**
	 * The tudooID this permission is valid for.
	 */
	private String tudooID;

	public EditPermission(String tudooID) {
		this.tudooID = tudooID;
	}
	
	public String getBelongingTudooID(){
		return tudooID;
	}
}
