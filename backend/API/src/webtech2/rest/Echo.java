package webtech2.rest;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;

@ApplicationPath("/data")
@Path("/echo")
public class Echo extends Application{
	
	@POST
    @Path("/ping")
    public Response registerUser(){
        return Response.ok("Pong!").build();
    }
}
