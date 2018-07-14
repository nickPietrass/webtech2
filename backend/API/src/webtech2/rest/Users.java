package webtech2.rest;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

import webtech2.jpa.entities.User;
import webtech2.jpa.exceptions.DuplicateDBEntryException;
import webtech2.jpa.exceptions.NoDBEntryException;
import webtech2.rest.auth.AuthRealm;
import webtech2.rest.exceptions.UserNotResolvedException;
import webtech2.rest.temporary.SerializableUser;
import webtech2.rest.temporary.SerializableUserID;
import webtech2.rest.temporary.params.NewUser;

/**
 * Created by Ilja on 26.06.2018.
 */
@ApplicationPath("/api")
@Path("/users")
public class Users extends Application{
	
    @GET
    @Path("/get")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@QueryParam("id") String userID){
    	try {
			User tempUser = JPAConnector.getAppConnection().getUserByLoginName(userID);
			return Response.ok(
					new SerializableUser(
						tempUser.getLoginName(),
						"***",
						tempUser.getDisplayName(),
						tempUser.getCreationDate())
					).build();
		} catch (NoDBEntryException e) {
			return Response.status(400).build();
		}
    }

    @POST
    @Path("/register")
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerUser(NewUser idObject){ 
    	try {
			JPAConnector.getAppConnection().registerNewUser(idObject.getLoginName(), idObject.getPassword(), idObject.getDisplayName());
			return Response.ok("myCoolSessionID").build(); //TODO Shiro stuff
		} catch (DuplicateDBEntryException e) {
			return Response.status(400).build();
		}
    }
    
    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginUser(SerializableUserID idObject){ //TODO Shiro stuff, also if user already logged in, give him new sessionID
    	Subject currentUser = AuthRealm.getCurrentSubject();
    	try {
			AuthRealm.loginUser(idObject.getLoginName(), idObject.getPassword());
		} catch (Exception e) {
			return Response.status(400).build();
		}
    	return Response.ok().build();
    }
    
    @PUT
    @Path("/editDisplayName")
    @Produces(MediaType.APPLICATION_JSON)
    public Response editDisplayName(@HeaderParam("sessionID") String sessionID, String newName){
    	try {
        	//Get user if possible
    		SerializableUser tempUser = AuthRealm.resolveUser(sessionID);
    		
			JPAConnector.getAppConnection().changeUserDisplayName("GET LOGINNAME FROM SHIRO", newName); //TODO Shiro
			return Response.ok().build(); //if it was changed
		} catch (NoDBEntryException | UserNotResolvedException e) {
			System.out.println("Error in editPassword: "+e.getClass().getSimpleName());
			return Response.status(400).build(); //if changeObject.sessionID bad
		}
    }
    
    @PUT
    @Path("/editPassword")
    @Produces(MediaType.APPLICATION_JSON)
    public Response editPassword(@HeaderParam("sessionID") String sessionID, String newPassword){
    	try {
        	//Get user if possible
    		SerializableUser tempUser = AuthRealm.resolveUser(sessionID);
    		
			JPAConnector.getAppConnection().changeUserPassword("GET LOGINNAME FROM SHIRO", newPassword); //TODO Shiro
			return Response.ok().build(); //if it was changed
		} catch (NoDBEntryException | UserNotResolvedException e) {
			System.out.println("Error in editPassword: "+e.getClass().getSimpleName());
			return Response.status(400).build(); //if changeObject.sessionID bad
		}
    }
    
    @DELETE
    @Path("/remove")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeUser(@HeaderParam("sessionID") String sessionID){
    	try {
    		//Get user if possible
    		SerializableUser tempUser = AuthRealm.resolveUser(sessionID);
    		
			JPAConnector.getAppConnection().deleteUser("SHIRO GIVE ME LOGIN NAME");
			return Response.ok().build(); //if user was removed
		} catch (NoDBEntryException | UserNotResolvedException e) {
			System.out.println("Error in editPassword: "+e.getClass().getSimpleName());
			return Response.status(400).build(); //if changeObject.sessionID bad
		}
    }
}