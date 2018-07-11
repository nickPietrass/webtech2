package webtech2.rest.temporary;

/**
 * Simple serializable group represenation with primitive types only
 * @author Ilja
 */
public class SerializableGroup {
	
		private String groupUUID;
		private String groupName;
		private String groupOwnerLoginName;
		private String[] userLoginNames;
		
		/**
		 * Empty constructor for Jax-RS.
		 */
		public SerializableGroup() {}
		
		/**
		 * Constructor to be used internally.
		 * @param groupUUID ID of the group.
		 * @param groupName Name of the Group.
		 * @param groupOwnerLoginName Login name of the owner of the group (which happens to be the uuid of the owner).
		 */
		public SerializableGroup(String groupUUID, String groupName, String groupOwnerLoginName, String[] userLoginNames) {
			this.groupUUID = groupUUID;
			this.groupName = groupName;
			this.groupOwnerLoginName = groupOwnerLoginName;
			this.userLoginNames = userLoginNames;
		}
		
		public String getGroupUUID() {
			return groupUUID;
		}
		public String getGroupName() {
			return groupName;
		}
		public String getGroupOwner() {
			return groupOwnerLoginName;
		}
		public String[] getUserLoginNames() {
			return userLoginNames;
		}
		
		public void setGroupUUID(String groupUUID) {
			this.groupUUID = groupUUID;
		}
		public void setGroupName(String groupName) {
			this.groupName = groupName;
		}
		public void setGroupOwner(String groupOwner) {
			this.groupOwnerLoginName = groupOwner;
		}
		public void setUserLoginNames(String[] userLoginNames) {
			this.userLoginNames = userLoginNames;
		}

}
