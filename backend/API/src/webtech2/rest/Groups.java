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

import webtech2.rest.temporary.SerializableGroup;
import webtech2.rest.temporary.params.GroupKick;

/**
 * Created by Ilja on 26.06.2018.
 */
@ApplicationPath("/api")
@Path("/groups")
public class Groups extends Application{

    @GET
    @Path("/get")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@HeaderParam("sessionID") String sessionID, @QueryParam("id") String groupID){
        //Call to JPA with id.
        return Response.ok(new SerializableGroup()).build();
    }
    
    @POST
    @Path("/create")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createGroup(@HeaderParam("sessionID") String sessionID, String groupName){
    	return Response.ok(new SerializableGroup()).build();
    }
    
    @PUT
    @Path("/addUser")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addUserToGroup(@HeaderParam("sessionID") String sessionID, String loginName) {
    	// jpa
    	return Response.ok(new SerializableGroup()).build();
    }
    
    @DELETE
    @Path("/removeUser")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeUserFromGroup(@HeaderParam("sessionID") String sessionID, GroupKick kickEvent) {
    	// jpa
    	return Response.ok(new SerializableGroup()).build();
    }
    
    @GET
    @Path("/userGroups")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserGroups(@HeaderParam("sessionID") String sessionID){
    	return Response.ok(new SerializableGroup[] {new SerializableGroup()}).build();
    }
    
    @DELETE
    @Path("/remove")
    public Response removeUser(@HeaderParam("sessionID") String sessionID, String groupID){
    	//return Response.status(401).build();
        return Response.ok().build();
    }
}
