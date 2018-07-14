package webtech2.rest.exceptions;

/**
 * Is thrown if given sessionID is outdated.
 * @author Ilja
 */
@SuppressWarnings("serial")
public class UserNotResolvedException extends Exception{
	
	private String sessionID;

	public UserNotResolvedException(String sessionID) {
		this.sessionID = sessionID;
	}
	
	public String getSessionID() {
		return sessionID;
	}
	
}
