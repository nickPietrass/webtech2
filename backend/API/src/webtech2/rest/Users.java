package webtech2.rest;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Consumes;
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

import webtech2.rest.storage.PasswordChange;
import webtech2.rest.storage.SerializableUser;
import webtech2.rest.storage.SerializableUserID;
import webtech2.rest.storage.UsernameChange;

/**
 * Created by Ilja on 26.06.2018.
 */
@ApplicationPath("/api")
@Path("/users")
public class Users extends Application{

    @GET
    @Path("/") //geht auch ""?
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(SerializableUserID idObject, @QueryParam("id") String id){
        //Call to JPA with id.
    	//return Response.status(404).build();
        return Response.ok(new SerializableUser()).build();
    }

    @POST
    @Path("/login")
    public Response loginUser(SerializableUserID idObject){ //if user already logged in, give him new sessionID
    	//return Response.ok("<sessionID>").build();
    	return Response.status(400).build(); //falscher Username oder Passwort
    }
    
    @POST
    @Path("/register")
    public Response registerUser(SerializableUserID idObject){
    	//return Response.ok("<sessionID>").build();
        return Response.status(400).build();
    }

    @PUT
    @Path("/editUsername")
    public Response editUsername(UsernameChange changeObject){
    	//return Response.ok().build(); //if it was changed
        return Response.status(400).build(); //if changeObject.sessionID bad
    }
    
    @PUT
    @Path("/editPassword")
    public Response editPassword(PasswordChange changeObject){
    	//return Response.ok().build(); //if it was changed
        return Response.status(400).build(); //if changeObject.sessionID bad
    }
    
    @DELETE
    @Path("/remove")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response removeUser(String sessionID){ //TODO test this stuff if possible, use SessionID.java if it won't work.
    	//return Response.ok().build(); //if user was removed
        return Response.status(400).build(); //if sessionID bad
    }
}
