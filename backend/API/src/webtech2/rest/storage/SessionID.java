package webtech2.rest.storage;

/**
 * Session id string holder object cause f*ck Jax-RS.
 * @author Ilja
 */
public class SessionID {
	private String sessionID;
	
	public SessionID() {}
	public SessionID(String sessionID) {
		this.sessionID = sessionID;
	}
	
	public String getSessionID() {
		return sessionID;
	}
	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}
}
