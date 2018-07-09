package webtech2.rest.storage;

/**
 * Simple serializable TuDoo represenation with primitive types only
 * @author Ilja
 */
public class SerializableTudoo {
		
	private String 	tudooUUID,
					title,
					content,
					owner, //Owner UUID/login name
					created;
	private String[] visibleBy,editableBy;
	
	/**
	 * Empty constructor for Jax-RS.
	 */
	public SerializableTudoo() {}
	
	/**
	 * Constructor to be used internally.
	 * @param password Hashed PW. 
	 */
	public SerializableTudoo(String userUUID, String title, String content, String owner, String created) {
		this.tudooUUID = userUUID;
		this.title = title;
		this.content = content;
		this.owner = owner;
		this.created = created;
	}
	
	public String getUserUUID() {
		return tudooUUID;
	}
	public String getTitle() {
		return title;
	}
	public String getContent() {
		return content;
	}
	public String getOwner() {
		return owner;
	}
	public String getCreated() {
		return created;
	}
	public String[] getVisibleBy() {
		return visibleBy;
	}
	public String[] getEditableBy() {
		return editableBy;
	}

	public void setTuDooUUID(String tudooUUID) {
		this.tudooUUID = tudooUUID;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	public void setVisibleBy(String[] visibleBy) {
		this.visibleBy = visibleBy;
	}
	public void setEditableBy(String[] editableBy) {
		this.editableBy = editableBy;
	}
	
}