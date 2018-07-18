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

import webtech2.jpa.entities.TudooGroup;
import webtech2.jpa.exceptions.NoDBEntryException;
import webtech2.rest.auth.AuthRealm;
import webtech2.rest.temporary.SerializableGroup;
import webtech2.rest.temporary.params.GroupMoveAction;

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
    		if(!tempGroup.getGroupOwner().getLoginName().equals(AuthRealm.instance.getCurrentUser().getLoginName()) 
    				&& tempGroup.getGroupMembers().contains(AuthRealm.instance.getCurrentUser().getLoginName())) {
    			return Response.status(400).build();
    		}
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
			TudooGroup tempGroup = JPAConnector.getGroupAppConnection().registerNewTudooGroup(groupName, AuthRealm.instance.getCurrentUser().getLoginName());
			return Response.ok(
					new SerializableGroup(
							tempGroup.getGroupUUID(),
							tempGroup.getGroupName(),
							tempGroup.getGroupOwner().getLoginName(),
							tempGroup.getGroupMembers().toArray(new String[0])
					)
			).build();
		} catch (Exception e) {
			System.out.println("ERROR: " + e.getMessage());
			return Response.status(400).build();
		}
    }
    
    @PUT
    @Path("/addUser")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addUserToGroup(GroupMoveAction actionObject) {
    	try {
    		TudooGroup tempGroup = JPAConnector.getGroupAppConnection().getGroupByID(actionObject.getGroupID());
    		if(!tempGroup.getGroupOwner().getLoginName().equals(AuthRealm.instance.getCurrentUser().getLoginName())) {
    			return Response.status(400).build();
    		}
			JPAConnector.getGroupAppConnection().addMemberToGroup(actionObject.getGroupID(),actionObject.getLoginName());
			tempGroup = JPAConnector.getGroupAppConnection().getGroupByID(actionObject.getGroupID());
			return Response.ok(
					new SerializableGroup(
							tempGroup.getGroupUUID(),
							tempGroup.getGroupName(),
							tempGroup.getGroupOwner().getLoginName(),
							tempGroup.getGroupMembers().toArray(new String[0])
					)
			).build();
		} catch (Exception e) {
			System.out.println("ERROR: " + e.getMessage());
			return Response.status(400).build();
		}
    }
    
    @DELETE
    @Path("/removeUser")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeUserFromGroup(GroupMoveAction actionObject) {
    	try {
    		TudooGroup tempGroup = JPAConnector.getGroupAppConnection().getGroupByID(actionObject.getGroupID());
    		if(!tempGroup.getGroupOwner().getLoginName().equals(AuthRealm.instance.getCurrentUser().getLoginName())) {
    			return Response.status(400).build();
    		}
    		JPAConnector.getGroupAppConnection().deleteUserFromGroup(actionObject.getGroupID(),actionObject.getLoginName());
			tempGroup = JPAConnector.getGroupAppConnection().getGroupByID(actionObject.getGroupID());
			return Response.ok(
					new SerializableGroup(
							tempGroup.getGroupUUID(),
							tempGroup.getGroupName(),
							tempGroup.getGroupOwner().getLoginName(),
							tempGroup.getGroupMembers().toArray(new String[0])
					)
			).build();
		} catch (Exception e) {
			System.out.println("ERROR: " + e.getMessage());
			return Response.status(400).build();
		}
    }
    
    @GET
    @Path("/userGroups")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserGroups(){
    	try {
			ArrayList<TudooGroup> groups = JPAConnector.getGroupAppConnection().getGroupsWhereOwnerIs(AuthRealm.instance.getCurrentUser().getLoginName());
			SerializableGroup[] serializableGroups = new SerializableGroup[groups.size()];
			for(int i = 0; i<groups.size(); i++) {
				TudooGroup tempGroup = groups.get(i);
				serializableGroups[i] = new SerializableGroup(
						tempGroup.getGroupUUID(),
						tempGroup.getGroupName(),
						tempGroup.getGroupOwner().getLoginName(),
						tempGroup.getGroupMembers().toArray(new String[0])
						);
			}
			return Response.ok(serializableGroups).build();
		} catch (Exception e) {
			System.out.println("ERROR: " + e.getMessage());
			return Response.status(400).build();
		}
    }
    
    @DELETE
    @Path("/remove")
    public Response removeGroup(@QueryParam("id") String groupID){
    	try {
    		TudooGroup tempGroup = JPAConnector.getGroupAppConnection().getGroupByID(groupID);
    		if(!tempGroup.getGroupOwner().getLoginName().equals(AuthRealm.instance.getCurrentUser().getLoginName())) {
    			return Response.status(400).build();
    		}
			JPAConnector.getGroupAppConnection().deleteGroup(groupID);
			return Response.ok().build();
		} catch (Exception e) {
			System.out.println("ERROR: " + e.getMessage());
			return Response.status(400).build();
		}
    }
}
