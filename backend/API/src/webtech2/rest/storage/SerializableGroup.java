package webtech2.rest.storage;

/**
 * Simple serializable group represenation with primitive types only
 * @author Ilja
 */
public class SerializableGroup {
	
		private String groupUUID;
		private String groupName;
		private String groupOwnerLoginName;
		
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
		public SerializableGroup(String groupUUID, String groupName, String groupOwnerLoginName) {
			this.groupUUID = groupUUID;
			this.groupName = groupName;
			this.groupOwnerLoginName = groupOwnerLoginName;
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
		
		public void setGroupUUID(String groupUUID) {
			this.groupUUID = groupUUID;
		}
		public void setGroupName(String groupName) {
			this.groupName = groupName;
		}
		public void setGroupOwner(String groupOwner) {
			this.groupOwnerLoginName = groupOwner;
		}

}
