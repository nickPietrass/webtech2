package webtech2.rest;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

/**
 * Simple ping/pong test.
 * @author Ilja
 */
@ApplicationPath("")
@Path("")
public class Echo extends Application{
	
	@GET
    @Path("/ping")
    public Response registerUser(){
        return Response.ok("Pong!").build();
    }
}
