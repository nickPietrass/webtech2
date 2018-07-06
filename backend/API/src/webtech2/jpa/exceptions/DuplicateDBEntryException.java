package webtech2.jpa.exceptions;

/**
 * DuplicateDBEntryException is thrown, if you try to persist an object that already exists in the database.
 * @author Wayne
 *
 */
public class DuplicateDBEntryException extends Exception {
	
	public DuplicateDBEntryException() {
		
	}
	
	public DuplicateDBEntryException(String message) {
		super(message);
	}
}
