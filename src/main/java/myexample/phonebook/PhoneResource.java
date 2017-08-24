package myexample.phonebook;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/phonebook")
public class PhoneResource {
	protected final PhoneService phoneService;
	
	@Inject
	public PhoneResource(PhoneService phoneService){
		this.phoneService = phoneService;
	}
	
	@GET
	@Path("/contacts")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Contact> contactsGet() {
		return phoneService.getAllContacts();
	}
	
}
