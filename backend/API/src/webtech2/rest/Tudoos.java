package webtech2.rest;

import java.util.ArrayList;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import webtech2.jpa.entities.Tudoo;
import webtech2.jpa.exceptions.NoDBEntryException;
import webtech2.rest.auth.AuthRealm;
import webtech2.rest.auth.permission.PermissionLevel;
import webtech2.rest.temporary.SerializableTudoo;
import webtech2.rest.temporary.params.TudooPermissionChange;

/**
 * Created by Ilja on 26.06.2018.
 */
@ApplicationPath("/api")
@Path("/tudoos")
public class Tudoos extends Application{

    /*Currently useless.
	@GET
    @Path("/get")
    @Produces(MediaType.APPLICATION_JSON)
    public Object getTudoo(@QueryParam("id") String id){
        //Call to JPA with id.
        return "NONE";
    }*/
    
    @POST
    @Path("/create")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createTudoo(SerializableTudoo tudooSkeleton){ //Check if Jax-RS really uses this object even though he can't fill it completely
    	try {
			return Response.ok(JPAConnector
						.getTudooAppConnection()
						.registerNewTodoo(
								tudooSkeleton.getTitle(), 
								tudooSkeleton.getContent(), 
								AuthRealm.instance.getCurrentUser().getLoginName()
						)
					).build();
		} catch (Exception e) {
			System.out.println("ERROR: " + e.getMessage());
			return Response.status(400).build();
		}
    }
    
    @PUT
    @Path("/editText")
    @Produces(MediaType.APPLICATION_JSON)
    public Response editTudooText(SerializableTudoo tudooSkeleton){
    	//TODO check if uuid is given. If not, wrong!
    	try {
			if(tudooSkeleton.getTudooUUID()==null || JPAConnector.getTudooAppConnection().getTudooByID(tudooSkeleton.getTudooUUID())==null) {
				return Response.status(400).build();
			}
			JPAConnector.getTudooAppConnection().changeTudooContent(tudooSkeleton.getTudooUUID(), tudooSkeleton.getTitle(), tudooSkeleton.getContent());
	        return Response.ok(
	        		JPAConnector.getTudooAppConnection().getTudooByID(tudooSkeleton.getTudooUUID())
	        		).build();
		} catch (NoDBEntryException e) {
			System.out.println("ERROR: " + e.getMessage());
			return Response.status(400).build();
		}
    }
    
    /**
     * Checks for all Tudoos he can see.
     * @return Array of his AND his visible tudoos.
     */
    @GET
    @Path("/userTudoos")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserTudoos(){
    	try {
			ArrayList<Tudoo> tudooList = JPAConnector.getTudooAppConnection().getAllVisibleTudoosOfUser(AuthRealm.instance.getCurrentUser().getLoginName());
			SerializableTudoo[] serializableTudoos = new SerializableTudoo[tudooList.size()];
			for(int i = 0; i<tudooList.size(); i++) {
				Tudoo tempTudoo = tudooList.get(i);
				serializableTudoos[i] = new SerializableTudoo(
						tempTudoo.getTudooUUID(), 
						tempTudoo.getTitle(), 
						tempTudoo.getContent(), 
						tempTudoo.getTudooOwner().getLoginName(), 
						tempTudoo.getCreationDate()
						);
			}
			return Response.ok(serializableTudoos).build();
		} catch (Exception e) {
			System.out.println("ERROR: " + e.getMessage());
			return Response.status(400).build();
		}
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
    public Response updatePermission(TudooPermissionChange permChange) {
    	try {
    		boolean isOwner = JPAConnector.getTudooAppConnection().getTudooByID(permChange.getTudooID()).getTudooOwner().getLoginName()
    				.equals(AuthRealm.instance.getCurrentUser().getLoginName());
    		boolean success = false;
	    	if(permChange.getPermissionLevel() == PermissionLevel.NONE.getValue()) {
	    		//If he is the owner he can't do that
	    		if(isOwner) {
	    			return Response.status(400).build();
	    		}
	    		JPAConnector.getTudooAppConnection().addUserOrGroupToTudoo(permChange.getTudooID(), permChange.getTargetID());
	    		success = true;
	    	}else {
	    		if(permChange.getPermissionLevel() == PermissionLevel.VIEW.getValue()) {
	        		if(isOwner) {
	        			JPAConnector.getTudooAppConnection().changeUserOrGroupPermissionToVisibleByOnly(permChange.getTudooID(), permChange.getTargetID());
	        			success = true;
	        		}
	        	}else {
	        		if(permChange.getPermissionLevel() == PermissionLevel.EDIT.getValue()) {
	        			if(isOwner) {
	            			JPAConnector.getTudooAppConnection().deleteUserOrGroupFromTudoo(permChange.getTudooID(), permChange.getTargetID());
	            			success = true;
	            		}
	            	}
	        	}
	    	}
	    	return success ? 
	    			Response.ok(JPAConnector.getTudooAppConnection().getTudooByID(permChange.getTudooID())).build() 
	    			: Response.status(400).build();
    	
    	}catch(Exception e) {
    		System.out.println("ERROR:" + e.getMessage());
    		return Response.status(400).build();
    	}
    	
    }
    
    @DELETE
    @Path("/remove")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeTudoo(String tudooUUID){
    	try {
    		Tudoo tempTudoo = JPAConnector.getTudooAppConnection().getTudooByID(tudooUUID);
    		if(!tempTudoo.getTudooOwner().getLoginName().equals(AuthRealm.instance.getCurrentUser().getLoginName()) 
    				&& !tempTudoo.getEditableBy().contains(AuthRealm.instance.getCurrentUser().getLoginName())) {
    			return Response.status(400).build();
    		}
			JPAConnector.getTudooAppConnection().deleteTodoo(tudooUUID);
			return Response.ok().build();
		} catch (Exception e) {
			System.out.println("ERROR: " + e.getMessage());
			return Response.status(400).build();
		}
    }
}
