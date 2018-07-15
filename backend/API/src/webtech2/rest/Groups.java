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

import webtech2.jpa.entities.TudooGroup;
import webtech2.jpa.exceptions.DuplicateDBEntryException;
import webtech2.jpa.exceptions.NoDBEntryException;
import webtech2.rest.auth.AuthRealm;
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
    public Response getGroup(@QueryParam("id") String groupID){
        try {
			TudooGroup tempGroup = JPAConnector.getGroupAppConnection().getGroupByID(groupID);
			return Response.ok(
					new SerializableGroup(
							tempGroup.getGroupUUID(),
							tempGroup.getGroupName(),
							tempGroup.getGroupOwner().getLoginName(),
							tempGroup.getGroupMembers().toArray(new String[0])
					)
			).build();
		} catch (Exception e) {
			if(e instanceof NoDBEntryException)
				System.out.println("Someone tried getting a wrong group!");
			else {
				System.out.println("ERROR: " + e.getMessage());
			}
			return Response.status(400).build();
		}
    }
    
    @POST
    @Path("/create")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createGroup(String groupName){
    	try {
			TudooGroup tempGroup = JPAConnector.getGroupAppConnection().registerNewTodooGroup(groupName, AuthRealm.instance.getCurrentUser().getLoginName());
			return Response.ok(
					new SerializableGroup(
							tempGroup.getGroupUUID(),
							tempGroup.getGroupName(),
							tempGroup.getGroupOwner().getLoginName(),
							tempGroup.getGroupMembers().toArray(new String[0])
					)
			).build();
		} catch (Exception e) {
			if(e instanceof NoDBEntryException)
				System.out.println("Someone tried getting a wrong group!");
			else {
				System.out.println("ERROR: " + e.getMessage());
			}
			return Response.status(400).build();
		}
    }
    
    @PUT
    @Path("/addUser")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addUserToGroup(String loginName) {
    	// jpa
    	return Response.ok(new SerializableGroup()).build();
    }
    
    @DELETE
    @Path("/removeUser")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeUserFromGroup(GroupKick kickEvent) {
    	// jpa
    	return Response.ok(new SerializableGroup()).build();
    }
    
    @GET
    @Path("/userGroups")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserGroups(){
    	return Response.ok(new SerializableGroup[] {new SerializableGroup()}).build();
    }
    
    @DELETE
    @Path("/remove")
    public Response removeGroup(String groupID){
    	//return Response.status(401).build();
        return Response.ok().build();
    }
}
