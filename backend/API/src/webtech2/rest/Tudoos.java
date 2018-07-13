package webtech2.rest;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import webtech2.rest.temporary.SerializableTudoo;
import webtech2.rest.temporary.params.SerializableParam;
import webtech2.rest.temporary.params.TudooPermissionChange;

/**
 * Created by Ilja on 26.06.2018.
 */
@ApplicationPath("/api")
@Path("/tudoos")
public class Tudoos extends Application{

    /* TODO Useless?!
	@GET
    @Path("/get") //geht auch ""?
    @Produces(MediaType.APPLICATION_JSON)
    public Object getTudoo(@QueryParam("id") String id){
        //Call to JPA with id.
        return "NONE";
    }*/
    
    @POST
    @Path("/create")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createTudoo(@HeaderParam("sessionID") String sessionID, SerializableTudoo tudooSkeleton){ //Check if Jax-RS really uses this object even though he can't fill it completely
        return Response.ok(new SerializableTudoo()).build();
    }
    
    @PUT
    @Path("/editText")
    @Produces(MediaType.APPLICATION_JSON)
    public Response editTudooText(@HeaderParam("sessionID") String sessionID, SerializableTudoo tudooSkeleton){
    	//TODO check if uuid is given. If not, wrong!
        return Response.ok(new SerializableTudoo()).build();
    }
    
    /**
     * Checks for all Tudoos he can see.
     * @return Array of his AND his visible tudoos.
     */
    @GET
    @Path("/userTudoos")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserTudoos(@HeaderParam("sessionID") String sessionID){
    	return Response.ok(new SerializableTudoo[] {new SerializableTudoo()}).build();
    }
    
    /**
     * Is able to change {@link webtech2.rest.auth.permission.PermissionLevel}s up to removing a user/group completely.
     * Examples: 
     * 1. User with level 0 -> user has no more permissions now.
     * 2. Group with level 2 -> all users of the given group have edit permissions now.
     * 
     * @return 200 with {@link SerializableTudoo} if possible, 400 else.
     */
    @PUT
    @Path("/updatePermission")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePermission(@HeaderParam("sessionID") String sessionID, TudooPermissionChange permChange) {
    	return Response.ok(new SerializableTudoo()).build();
    }
    
    @DELETE
    @Path("/remove")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeUser(@HeaderParam("sessionID") String sessionID, SerializableParam tudooUUID){
        return Response.status(400).build();
    }
}
