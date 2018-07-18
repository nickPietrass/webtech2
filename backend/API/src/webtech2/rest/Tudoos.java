package webtech2.rest;

import java.util.ArrayList;

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
			return Response.ok(createSendableTudoo(JPAConnector
						.getTudooAppConnection()
						.registerNewTudoo(
								tudooSkeleton.getTitle(), 
								tudooSkeleton.getContent(), 
								AuthRealm.instance.getCurrentUser().getLoginName()
								)
						)
					).build();
		} catch (Exception e) {
			System.out.println("Error in remove: " + e.getClass().getSimpleName());
			return Response.status(400).build();
		}
    }
    
    @PUT
    @Path("/editText")
    @Produces(MediaType.APPLICATION_JSON)
    public Response editTudooText(SerializableTudoo tudooSkeleton){
    	try {
			if(tudooSkeleton.getTudooUUID()==null || JPAConnector.getTudooAppConnection().getTudooByID(tudooSkeleton.getTudooUUID())==null) {
				return Response.status(400).build();
			}
			JPAConnector.getTudooAppConnection().changeTudooContent(tudooSkeleton.getTudooUUID(), tudooSkeleton.getTitle(), tudooSkeleton.getContent());
	        return Response.ok(createSendableTudoo(
	        		JPAConnector.getTudooAppConnection().getTudooByID(tudooSkeleton.getTudooUUID())
	        		)).build();
		} catch (Exception e) {
			if(e instanceof NoDBEntryException)
				System.out.println("ERROR: there is no Tudoo with that ID");
			else
				System.out.println("Error in remove: " + e.getClass().getSimpleName());
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
				serializableTudoos[i] = createSendableTudoo(tudooList.get(i));
			}
			return Response.ok(serializableTudoos).build();
		} catch (Exception e) {
			if(e instanceof NoDBEntryException)
				System.out.println("ERROR: there is no Tudoo with that ID");
			else
				System.out.println("Error in remove: " + e.getClass().getSimpleName());
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
    		boolean isOwner = JPAConnector.getTudooAppConnection().getTudooByID(permChange.getTudooUUID()).getTudooOwner().getLoginName()
    				.equals(AuthRealm.instance.getCurrentUser().getLoginName());
    		
    		//Only owners can change perms.
    		if(!isOwner) return Response.status(400).build();
    		
	    	if(permChange.getPermissionLevel() == PermissionLevel.NONE.getValue()) {
	    		//If he is the owner he can't remove himself from the Tudoo.
	    		if(permChange.getTargetID().equals(AuthRealm.instance.getCurrentUser().getLoginName())) {
	    			return Response.status(400).build();
	    		}
	    		JPAConnector.getTudooAppConnection().deleteUserOrGroupFromTudoo(permChange.getTudooUUID(), permChange.getTargetID());
	    	}else {
	    		if(permChange.getPermissionLevel() == PermissionLevel.VIEW.getValue()) {
	        			JPAConnector.getTudooAppConnection().changeUserOrGroupPermissionToVisibleByOnly(permChange.getTudooUUID(), permChange.getTargetID());
	        	}else {
	        		if(permChange.getPermissionLevel() == PermissionLevel.EDIT.getValue()) {
	        			JPAConnector.getTudooAppConnection().addUserOrGroupToTudoo(permChange.getTudooUUID(), permChange.getTargetID());
	            	}
	        	}
	    	}
	    	return Response.ok(createSendableTudoo(JPAConnector.getTudooAppConnection().getTudooByID(permChange.getTudooUUID()))).build();
    	
    	}catch(Exception e) {
    		if(e instanceof NoDBEntryException)
				System.out.println("ERROR: there is no Tudoo with that ID");
    		else
				System.out.println("Error in remove: " + e.getClass().getSimpleName());
			return Response.status(400).build();
    	}
    	
    }
    
    @DELETE
    @Path("/remove")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeTudoo(@QueryParam("id") String tudooUUID){
    	try {
    		Tudoo tempTudoo = JPAConnector.getTudooAppConnection().getTudooByID(tudooUUID);
    		if(!tempTudoo.getTudooOwner().getLoginName().equals(AuthRealm.instance.getCurrentUser().getLoginName()) 
    				&& !tempTudoo.getEditableBy().contains(AuthRealm.instance.getCurrentUser().getLoginName())) {
    			return Response.status(400).build();
    		}
			JPAConnector.getTudooAppConnection().deleteTudoo(tudooUUID);
			return Response.ok().build();
		} catch(Exception e) {
    		if(e instanceof NoDBEntryException)
				System.out.println("ERROR: there is no Tudoo with that ID");
			else
				System.out.println("Error in remove: " + e.getClass().getSimpleName());
			return Response.status(400).build();
    	}
    }
    
    /**
     * Helper method
     */
    private SerializableTudoo createSendableTudoo(Tudoo tudoo) {
    	return new SerializableTudoo(
    			tudoo.getTudooUUID(), 
    			tudoo.getTitle(), 
    			tudoo.getContent(), 
    			tudoo.getTudooOwner().getLoginName(), 
    			tudoo.getCreationDate(),
    			tudoo.getVisibleBy().toArray(new String[0]),
    			tudoo.getEditableBy().toArray(new String[0])
			);
    }
}
