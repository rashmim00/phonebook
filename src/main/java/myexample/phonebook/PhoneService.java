package myexample.phonebook;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.Handle;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;
import org.skife.jdbi.v2.util.IntegerColumnMapper;

public class PhoneService {
	
	protected class ContactGroupMapper implements ResultSetMapper<Integer> {
		protected List<Contact> contacts = null;
		protected Contact contact = null;
		
		public ContactGroupMapper(Contact contact) {
			this.contact = contact;
		}
		
		public ContactGroupMapper(List<Contact> contacts) {
			this.contacts = contacts;
		}
		@Override
		public Integer map(int index, ResultSet r, StatementContext ctx) throws SQLException {
			Integer rv = r.getInt("groupId");

			if (contacts != null) {
				Integer id = r.getInt("contactId");
				contacts.stream() //
						.filter(contact -> contact.getId() == id) //
						.findFirst() //
						.ifPresent(contact -> contact.getGroups().add(rv));
			} else if (contact != null) {
				contact.getGroups().add(rv);
			}

			return rv;
	
		}
	}

	/**
	 * Map a result set to a Contact object
	 */
	protected class ContactMapper implements ResultSetMapper<Contact> {
		@Override
		public Contact map(int index, ResultSet r, StatementContext ctx) throws SQLException {

			Contact rv = new Contact(r.getInt("id"), r.getString("address"), new Date(r.getInt("birthdate")), r.getString("company"), 
					r.getString("email"), r.getInt("favorite"), r.getString("firstName"),r.getString("lastName"), new ArrayList<Phone>(), new ArrayList<Integer>());

			return rv;
		}
	}
	
	protected class GroupMapper implements ResultSetMapper<Group> {
		@Override
		public Group map(int index, ResultSet r, StatementContext ctx) throws SQLException {
			Group rv = new Group(r.getInt("id"), r.getString("groupName"), r.getString("groupIcon"), new ArrayList<Integer>());
			return rv;
		}
	}

	protected class GroupParticipantMapper implements ResultSetMapper<Integer> {
		protected List<Group> groups = null;
		protected Group group = null;
		
		public GroupParticipantMapper(Group group) {
			this.group = group;
		}
		
		public GroupParticipantMapper(List<Group> groups) {
			this.groups = groups;
		}
		@Override
		public Integer map(int index, ResultSet r, StatementContext ctx) throws SQLException {
			Integer rv = r.getInt("contactId");

			if (groups != null) {
				Integer id = r.getInt("groupId");
				groups.stream() //
						.filter(group -> group.getId() == id) //
						.findFirst() //
						.ifPresent(group -> group.getParticipants().add(rv));
			} else if (group != null) {
				if (group.getParticipants() == null) group.setParticipants(new ArrayList<Integer>());
				group.getParticipants().add(rv);
			}

			return rv;
	
		}
	}
	
	/**
		 * Map a result set to a Contact object
		 */
		protected class PhoneMapper implements ResultSetMapper<Phone> {
			protected List<Contact> contacts = null;
			protected Contact contact = null;
			
			public PhoneMapper(Contact contact) {
				this.contact = contact;
			}
			
			public PhoneMapper(List<Contact> contacts) {
				this.contacts = contacts;
			}
			
			@Override
			public Phone map(int index, ResultSet r, StatementContext ctx) throws SQLException {

				Phone rv = new Phone(r.getInt("contact_id"), r.getString("phone_type"), r.getString("phone_number"));
				Integer id = r.getInt("contact_id");
				
				if (contacts != null) {
				contacts.stream() //
						.filter(s -> s.getId() == id.intValue()) //
						.findFirst() //
						.ifPresent(s -> s.getPhones().add(rv));
				} else if (contact != null) {
					contact.getPhones().add(rv);
				}
				return rv;
			}
		}

	protected DBI jdbi;
	
	@Inject
		PhoneService(DBI jdbi) {
			this.jdbi = jdbi;
		}
	
	public Group addContactToGroup(String action, Integer groupId, Integer contactId){
		Handle h = jdbi.open();
		Group g = h.createQuery("select * from groups where id =" + groupId).map(new GroupMapper()).first();
		
		// get contact's phone numbers
		List<Integer> contactIds = h.createQuery("select * from group_participants where groupId = :group") //
		.bind("group", g.getId()).map(new GroupParticipantMapper(g)).list();	
		
		if ("add".equalsIgnoreCase(action) && !contactIds.contains(contactId)){
			 h.createStatement("insert into group_participants (groupId, contactId) values (:groupId, :contactId)") //
			 .bind("groupId", groupId) 
			 .bind("contactId", contactId).execute();
		}
		else if ("remove".equalsIgnoreCase(action) && contactIds.contains(contactId)){
			 h.createStatement("delete from group_participants where groupId=:groupId and contactId =:contactId") //
			 .bind("groupId", groupId) 
			 .bind("contactId", contactId).execute();
		}
		jdbi.close(h);
		return g;
	}
	
	
	// creates a new contact
	public Contact createContact(Contact contact) {
		Handle h = jdbi.open();
		Integer id = h.createStatement("insert into contact ( " //
				+ "address, birthdate, company, email, favorite, firstName, lastName) values ("
				+ ":address, :birthdate, :company, :email, :favorite, :firstName, :lastName)") //
				.bind("address", contact.getAddress())
				.bind("birthdate", (contact.getBirthdate() != null) ? contact.getBirthdate().getTime() : 0)
				.bind("company", contact.getCompany())
				.bind("email", contact.getEmail())
				.bind("favorite", contact.getFavorite())
				.bind("firstName", contact.getFirstName())
				.bind("lastName", contact.getLastName()).executeAndReturnGeneratedKeys(IntegerColumnMapper.PRIMITIVE) //
				.first();

		// add phone number		
			if(contact.getPhones() != null && contact.getPhones().size() > 0){
				contact.getPhones().forEach(p -> {
					if (p.getType() != null && !p.getType().equals("") && p.getNumber() != null){
						h.createStatement("insert into phonenumber ( " //
						+ "contact_id, phone_type, phone_number) values (:contact_id, :phoneType, :phoneNumber)") //
						.bind("contact_id", id)
						.bind("phoneType", p.getType())
						.bind("phoneNumber", p.getNumber()).execute();
					}
				});
			}
			
		// add as group participant
			if(contact.getGroups()!= null && contact.getGroups().size() > 0) {
				contact.getGroups().forEach(gp -> {
					h.createStatement("insert into group_participants (groupId, contactId) values (:groupId, :contactId)") //
					 .bind("groupId", gp) 
					 .bind("contactId", id).execute();
				});
			}
			 
		contact.setId(id);
		jdbi.close(h);
		return contact;
	}

	// creates a new group
	public Group createGroup(Group group) {
		Handle h = jdbi.open();
		Integer id = h.createStatement("insert into groups ( " //
				+ "groupName, groupIcon) values ("
				+ ":groupName, :groupIcon)") //
				.bind("groupName", group.getName())
				.bind("groupIcon", group.getIcon())
				.executeAndReturnGeneratedKeys(IntegerColumnMapper.PRIMITIVE) //
				.first();			
		
		group.setId(id);
		jdbi.close(h);
		return group;
	}
	
	public boolean deleteContact(int id){
			if (id == 0) return false;
			
			Handle h = jdbi.open();
		// add transaction thing
			h.createStatement("delete from phonenumber where contact_id = :id") //
			.bind("id", id) //
			.execute();
			
			h.createStatement("delete from group_participants where contactId = :id") //
			.bind("id", id) //
			.execute();
			
			h.createStatement("delete from contact where id = :id") //
			.bind("id", id) //
			.execute();
			
			jdbi.close(h);
			return true;
		}
	

	public boolean deleteGroup(int id){
		if (id == 0) return false;
		
		Handle h = jdbi.open();
	// add transaction thing
		h.createStatement("delete from group_participants where groupId = :id") //
		.bind("id", id) //
		.execute();
		
		h.createStatement("delete from groups where id = :id") //
		.bind("id", id) //
		.execute();
		
		jdbi.close(h);
		return true;
	}
	
	// returns a list of all contacts
	public List<Contact> getAllContacts() {
		List<Contact> all = new ArrayList<Contact>();
		Handle h = jdbi.open();
		all = h.createQuery("select * from contact order by firstName asc").map(new ContactMapper()).list();
		h.createQuery("select * from phonenumber").map(new PhoneMapper(all)).list();	
		h.createQuery("select * from group_participants").map(new ContactGroupMapper(all)).list();	
		jdbi.close(h);
		return all;
	}
	
	// returns a list of all contacts
	public List<Group> getAllGroups() {
		List<Group> all = new ArrayList<Group>();
		Handle h = jdbi.open();
		all = h.createQuery("select * from groups order by groupName asc").map(new GroupMapper()).list();
		h.createQuery("select * from group_participants").map(new GroupParticipantMapper(all)).list();	
		jdbi.close(h);
		return all;			
	}
	
	// returns a single user by id
	public Contact getContact(int id) {
		if (id == 0) return null;
	
		Handle h = jdbi.open();
		Contact c = h.createQuery("select * from contact where id =:id").bind("id", id).map(new ContactMapper()).first();
	
		// get contact's phone numbers
		h.createQuery("select * from phonenumber where contact_id = :contact") //
		.bind("contact", c.getId()).map(new PhoneMapper(c)).list();
		
		jdbi.close(h);
		return c;
	}	

	// returns a single user by id
	public Group getGroup(int id) {
		if (id == 0) return null;
	
		Handle h = jdbi.open();
		Group g = h.createQuery("select * from groups where id =:id").bind("id", id).map(new GroupMapper()).first();
	
		// get contact's phone numbers
		List<Integer> contactIds = h.createQuery("select * from group_participants where groupId =:group") //
		.bind("group", g.getId()).map(new GroupParticipantMapper(g)).list();
		g.setParticipants(contactIds);
		jdbi.close(h);
		return g;
	}	
	
	// updates an existing user
	public Contact updateContact(Contact contact) {
		Handle h = jdbi.open();
		h.createStatement("update contact set " //
				+ "address = :address, birthdate = :birthdate, company = :company, email =:email, favorite = :favorite," //
				+ "firstName =:firstName, lastName =:lastName where id = :id") //
				.bind("address", contact.getAddress())
				.bind("birthdate", contact.getBirthdate().getTime())
				.bind("company", contact.getCompany())
				.bind("email", contact.getEmail())
				.bind("favorite", contact.getFavorite())
				.bind("firstName", contact.getFirstName())
				.bind("lastName", contact.getLastName())
				.bind("id", contact.getId()).execute();
	
		// update phone number		
		if(!contact.getPhones().isEmpty()){
			contact.getPhones().forEach(p -> {			
				if(p.getId() == -1) {
					//insert new numbers
					h.createStatement("insert into phonenumber ( " //
							+ "contact_id, phone_type, phone_number) values (:contact_id, :phoneType, :phoneNumber)") //
							.bind("contact_id", contact.getId())
							.bind("phoneType", p.getType())
							.bind("phoneNumber", p.getNumber()).execute();
				}
				else {					
				h.createStatement("update phonenumber set " //
				+ "phone_number =:phoneNumber where contact_id =:contact_id and phone_type =:phoneType") //
				.bind("contact_id", contact.getId())
				.bind("phoneType", p.getType())
				.bind("phoneNumber", p.getNumber())
				.execute();
				}
			});
		}
		
		jdbi.close(h);
		return contact;
	}
}
