package webtech2.rest.temporary;

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
	public SerializableTudoo(String tudooUUID, String title, String content, String owner, String created,
			String[] visibleBy, String[] editableBy) {
		this.tudooUUID = tudooUUID;
		this.title = title;
		this.content = content;
		this.owner = owner;
		this.created = created;
		this.visibleBy = visibleBy;
		this.editableBy = editableBy;
	}
	
	public String getTudooUUID() {
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

	public void setTudooUUID(String tudooUUID) {
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