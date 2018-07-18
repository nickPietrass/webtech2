package webtech2.rest;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import webtech2.jpa.entities.User;
import webtech2.jpa.exceptions.DuplicateDBEntryException;
import webtech2.jpa.exceptions.NoDBEntryException;
import webtech2.rest.auth.AuthRealm;
import webtech2.rest.auth.PasswordSaltMixture;
import webtech2.rest.temporary.SerializableUser;
import webtech2.rest.temporary.SerializableUserID;
import webtech2.rest.temporary.params.NewUser;

/**
 * Created by Ilja on 26.06.2018.
 */
@ApplicationPath("/api")
@Path("/users")
public class Users extends Application {
	
//	private static final String EMAIL_REGEX = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\\\.[A-Z]{2,6}$"; 

	@GET
	@Path("/get")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUser(@QueryParam("id") String userID) {
		try {
			User tempUser = JPAConnector
					.getAppConnection()
					.getUserByLoginName(userID);
			return Response.ok(
					new SerializableUser(
							tempUser.getLoginName(), 
							"***", 
							tempUser.getDisplayName(),
							tempUser.getCreationDate())
					).build();
		} catch (Exception e) {
			if(e instanceof NoDBEntryException)
				System.out.println("Someone tried to make an already existing account!");
			else {
				System.out.println("Error in remove: " + e.getClass().getSimpleName());
			}
			return Response.status(400).build();
		}
	}

	@POST
	@Path("/register")
	@Produces(MediaType.APPLICATION_JSON)
	public Response registerUser(NewUser idObject) {
		try {
//			Pattern ptr = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);
//			if(!ptr.matcher(idObject.getLoginName()).matches()) {
//				return Response.status(400).build();
//			}
			if(!(idObject.getLoginName().contains("@") && idObject.getLoginName().contains("."))){
				return Response.status(400).build();
			}
			PasswordSaltMixture tempPW = AuthRealm.instance.generatePassword(idObject.getPassword());
			JPAConnector
				.getAppConnection()
				.registerNewUser(
					idObject.getLoginName(), 
					tempPW.getPassword(),
					idObject.getDisplayName(), 
					tempPW.getSalt()
				);
			return Response.ok().build();
		} catch (Exception e) {
			if(e instanceof DuplicateDBEntryException)
				System.out.println("Someone tried to make an already existing account!");
			else {
				System.out.println("Error in remove: " + e.getClass().getSimpleName());
			}
			return Response.status(400).build();
		}
	}

	@POST
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	public Response loginUser(SerializableUserID idObject) { 
		try {
			if(AuthRealm.instance.loginUser(
					idObject.getLoginName(), 
					idObject.getPassword()
					)) {
				return Response.ok().build();
			}else {
				return Response.status(400).build();
			}
		} catch (Exception e) {
			System.out.println("ERROR: " + e.getMessage());
			return Response.status(400).build();
		}
	}

	@PUT
	@Path("/editDisplayName")
	@Produces(MediaType.APPLICATION_JSON)
	public Response editDisplayName(String newName) {
		try {
			// Get user if possible
			User tempUser = AuthRealm.instance.getCurrentUser();
			JPAConnector.getAppConnection().changeUserDisplayName(tempUser.getLoginName(), newName);
			return Response.ok().build();
		} catch (NoDBEntryException e) {
			System.out.println("Error in editDisplayName: " + e.getClass().getSimpleName());
			return Response.status(400).build();
		}
	}

	@PUT
	@Path("/editPassword")
	@Produces(MediaType.APPLICATION_JSON)
	public Response editPassword(String newPassword) {
		try {
			// Get user if possible
			User tempUser = AuthRealm.instance.getCurrentUser();
			PasswordSaltMixture tempPW = AuthRealm.instance.generatePassword(newPassword);
			JPAConnector.getAppConnection().changeUserPassword(tempUser.getLoginName(), tempPW.getPassword(), tempPW.getSalt());
			return Response.ok().build();
		} catch (NoDBEntryException e) {
			System.out.println("Error in editPassword: " + e.getClass().getSimpleName());
			return Response.status(400).build();
		}
	}

	@DELETE
	@Path("/remove")
	@Produces(MediaType.APPLICATION_JSON)
	public Response removeUser() {
		try {
			// Get user if possible
			User tempUser = AuthRealm.instance.getCurrentUser();
			JPAConnector.getAppConnection().deleteUser(tempUser.getLoginName());
			return Response.ok().build();
		} catch (Exception e) {
			System.out.println("Error in remove: " + e.getClass().getSimpleName());
			return Response.status(400).build();
		}
	}
}