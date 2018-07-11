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

import webtech2.rest.temporary.SerializableGroup;

/**
 * Created by Ilja on 26.06.2018.
 */
@ApplicationPath("/api")
@Path("/groups")
public class Groups extends Application{

    @GET
    @Path("/get") //geht auch ""?
    @Produces(MediaType.APPLICATION_JSON)
    public Object getUser(@QueryParam("id") String id){
        //Call to JPA with id.
        return "NONE";
    }
    
    @POST
    @Path("/create")
    public Response createGroup(){
        return Response.status(401).build();
    }
    
    @PUT
    @Path("/addUser")
    public SerializableGroup addUserToGroup() {
    	// jpa
    	return new SerializableGroup();
    }
    
    @DELETE
    @Path("/removeUser")
    public SerializableGroup removeUserFromGroup() {
    	// jpa
    	return new SerializableGroup();
    }
    
    @PUT
    @Path("/getUserGroups")
    public SerializableGroup[] getUserGroups(@QueryParam("id") String loginName){
    	return null;
    }
    
    @DELETE
    @Path("/remove")
    public Response removeUser(@QueryParam("id") String id){
        return Response.status(401).build();
    }
}
