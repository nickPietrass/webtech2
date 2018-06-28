package webtech2.rest;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Ilja on 26.06.2018.
 */
@ApplicationPath("/data")
@Path("/groups")
public class Groups extends Application{

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
    
    @DELETE
    @Path("/remove")
    public Response removeUser(@QueryParam("id") String id){
        return Response.status(401).build();
    }
}
