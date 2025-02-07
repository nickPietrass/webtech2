package webtech2.rest.temporary.params;

/**
 * Temporary storage for HTTP body data of a request where a user should be kicked/added from/to a group.
 * @author Ilja
 */
public class GroupMoveAction {
	private String groupID,loginName;
	
	/**
	 * Empty constructor for Jax-RS.
	 */
	public GroupMoveAction() {}

	public GroupMoveAction(String groupID, String loginName) {
		this.groupID = groupID;
		this.loginName = loginName;
	}
	
	public String getGroupID() {
		return groupID;
	}
	public void setGroupID(String groupID) {
		this.groupID = groupID;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
}
