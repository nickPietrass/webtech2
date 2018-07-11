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

/**
 * Created by Ilja on 26.06.2018.
 */
@ApplicationPath("/api")
@Path("/tudoos")
public class Tudoos extends Application{

    @GET
    @Path("/") //geht auch ""?
    @Produces(MediaType.APPLICATION_JSON)
    public Object getUser(@QueryParam("id") String id){
        //Call to JPA with id.
        return "NONE";
    }
    
    @POST
    @Path("/create")
    public Response registerUser(){
        return Response.status(401).build();
    }
    
    
    /**
     * Is able to change permissions up to removing a user/group completely.
     * Examples: 
     * 1. User with level 0 -> user has no more permissions now.
     * 2. Group with level 2 -> all users of the given group have edit permissions now.
     *  
     * @param permLevel {@link webtech2.rest.auth.permission.PermissionLevel}
     * @param targetID ID of the target (group ID or user login name).
     * @return 200 if possible, 400 else.
     */
    @PUT
    @Path("/updatePermission")
    public Response updatePermission(@QueryParam("tudooID") String tudooID, @QueryParam("targetID") String targetID, @QueryParam("permissionLevel") String permLevel) {
    	return Response.ok("").build();
    }
    
    @DELETE
    @Path("/remove")
    public Response removeUser(@QueryParam("id") String id){
        return Response.status(401).build();
    }
}
