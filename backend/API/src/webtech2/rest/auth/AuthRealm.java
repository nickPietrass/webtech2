package webtech2.rest.auth;

import java.util.HashMap;

import webtech2.rest.exceptions.UserNotResolvedException;
import webtech2.rest.temporary.SerializableUser;

public class AuthRealm {
	private static HashMap<String,SerializableUser> userlist = new HashMap<>(); //TODO Shiro magic
	
	public static SerializableUser resolveUser(String sessionID) throws UserNotResolvedException{
		SerializableUser tempUser = userlist.get(sessionID);
		if(tempUser==null) throw new UserNotResolvedException(sessionID);
		return tempUser;
	} 
}
