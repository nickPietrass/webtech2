package webtech2.rest.storage;

/**
 * Simple serializable user represenation with primitive types only
 * @author Ilja
 */
public class SerializableUser {
	
		private String userUUID; //which is his loginName
		private String password; //hashed pw
		private String displayName;
		private String created;
		
		/**
		 * Empty constructor for Jax-RS.
		 */
		public SerializableUser() {}
		
		/**
		 * Constructor to be used internally.
		 * @param password Hashed PW. 
		 */
		public SerializableUser(String userUUID, String password, String displayName, String created) {
			this.userUUID = userUUID;
			this.password = password;
			this.displayName = displayName;
			this.created = created;
		}
		
		public String getUserUUID() {
			return userUUID;
		}
		public String getPassword() {
			return password;
		}
		public String getDisplayName() {
			return displayName;
		}
		public String getCreated() {
			return created;
		}
		
		public void setGroupUUID(String groupUUID) {
			this.userUUID = groupUUID;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public void setDisplayName(String groupName) {
			this.displayName = groupName;
		}
		public void setCreated(String groupOwner) {
			this.created = groupOwner;
		}

}
