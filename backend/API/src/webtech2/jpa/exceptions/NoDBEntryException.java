package webtech2.jpa.exceptions;

/**
 * NoDBEntryException is thrown if a method in App uses queries to search for an entry that currently does not exist in the database.
 * @author Wayne
 *
 */
public class NoDBEntryException extends Exception {
	
	public NoDBEntryException() {
		
	}
	
	public NoDBEntryException(String message) {
		super(message);
	}
}
