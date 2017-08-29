package myexample.phonebook;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/phonebook")
public class PhoneResource {
	protected final PhoneService phoneService;
	
	@Inject
	public PhoneResource(PhoneService phoneService){
		this.phoneService = phoneService;
	}
	
	@DELETE
	@Path("/contacts/{id: \\d+}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response contactDelete(@PathParam("id") Integer id) {
		phoneService.deleteContact(id);
		return Response.noContent().build();
	}
	
	@GET
	@Path("/contacts/{id: \\d+}")
	@Produces(MediaType.APPLICATION_JSON)
	public Contact contactGet(@PathParam("id") Integer id) {
		return phoneService.getContact(id);
	}
	
	@POST
	@Path("/contacts")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response contactPost(Contact contact) {
		contact = phoneService.createContact(contact);
		return Response.ok(contact).build();
	}
	
	@PUT
	@Path("/contacts/{id: \\d+}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response contactPut(Contact contact) {
		phoneService.updateContact(contact);
		return Response.ok(contact).build();
	}
	
	@GET
	@Path("/contacts")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Contact> contactsGet() {
		return phoneService.getAllContacts();
	}
	
	@DELETE
	@Path("/groups/{id: \\d+}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response groupDelete(@PathParam("id") Integer id) {
		phoneService.deleteGroup(id);
		return Response.noContent().build();
	}
	
	
	@GET
	@Path("/groups/{id: \\d+}")
	@Produces(MediaType.APPLICATION_JSON)
	public Group groupGet(@PathParam("id") Integer id) {
		return phoneService.getGroup(id);
	}
	
	@POST
	@Path("/groups")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response groupPost(Group group) {
		group = phoneService.createGroup(group);
		return Response.ok(group).build();
	}
	
	@PUT
	@Path("/groups/{action}/{groupid: \\d+}/{contactid: \\d+}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response groupsPut(@PathParam("action") String action, @PathParam("groupid") Integer groupId, @PathParam("contactid") Integer contactId) {
		Group g = phoneService.addContactToGroup(action, groupId, contactId);
		return Response.ok(g).build();
	}
	
	@GET
	@Path("/groups")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Group> groupsGet() {
		return phoneService.getAllGroups();
	}
}
