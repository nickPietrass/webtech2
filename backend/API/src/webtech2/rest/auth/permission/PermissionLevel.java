package webtech2.rest.auth.permission;

/**
 * Permission levels used internally.
 * @see {@link webtech2.rest.Tudoos}
 * @author Ilja
 */
public enum PermissionLevel {
	NONE(0),
	VIEW(1),
	EDIT(2);
	
	private int value;
	
	private PermissionLevel(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
}
