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
    @Path("/get") //geht auch ""?
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@QueryParam("id") String userID){
        //Call to JPA with id.
    	//return Response.status(404).build();
        return Response.ok(new SerializableUser()).build();
    }

    @POST
    @Path("/register")
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerUser(NewUser idObject){
    	//return Response.status(400).build(); //Fehler.
    	return Response.ok("myCoolSessionID").build();
    }
    
    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginUser(SerializableUserID idObject){ //TODO if user already logged in, give him new sessionID
    	//return Response.status(400).build(); //Falscher Username oder Passwort
    	return Response.ok("myCoolSessionID").build();
    }
    
    @PUT
    @Path("/editDisplayName")
    @Produces(MediaType.APPLICATION_JSON)
    public Response editDisplayName(@HeaderParam("sessionID") String sessionID, String newName){
    	//return Response.ok().build(); //if it was changed
        return Response.status(400).build(); //if changeObject.sessionID bad
    }
    
    @PUT
    @Path("/editPassword")
    @Produces(MediaType.APPLICATION_JSON)
    public Response editPassword(@HeaderParam("sessionID") String sessionID, String newPassword){
    	//return Response.ok().build(); //if it was changed
        return Response.status(400).build(); //if changeObject.sessionID bad
    }
    
    @DELETE
    @Path("/remove")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeUser(@HeaderParam("sessionID") String sessionID){
    	//return Response.ok().build(); //if user was removed
        return Response.status(400).build(); //if sessionID bad
    }
}
