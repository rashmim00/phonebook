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
	
	protected DBI jdbi;

	@Inject
	PhoneService(DBI jdbi) {
		this.jdbi = jdbi;
	}

	// returns a list of all contacts
	public List<Contact> getAllContacts() {
		List<Contact> all = new ArrayList<Contact>();
		Handle h = jdbi.open();
		all = h.createQuery("select * from contact").map(new ContactMapper()).list();
		h.createQuery("select * from phonenumber").map(new PhoneMapper(all)).list();	
		jdbi.close(h);
		return all;
	}

	// returns a single user by id
	public Contact getContact(int id) {
		if (id == 0) return null;
	
		Handle h = jdbi.open();
		Contact c = h.createQuery("select * from contact where id =" + id).map(new ContactMapper()).first();
	
		// get contact's phone numbers
		h.createQuery("select * from phonenumber where contact_id = :contact") //
		.bind("contact", c.getId()).map(new PhoneMapper(c)).list();
		
		jdbi.close(h);
		return c;
	}
	
	public boolean deleteContact(int id){
		if (id == 0) return false;
		
		Handle h = jdbi.open();
	// add transaction thing
		h.createStatement("delete from phonenumber where contact_id = :id") //
		.bind("id", id) //
		.execute();
		
		h.createStatement("delete from contact where id = :id") //
		.bind("id", id) //
		.execute();
		
		jdbi.close(h);
		return true;
	}

	// creates a new contact
	public Contact createContact(Contact contact) {
		Handle h = jdbi.open();
		Integer id = h.createStatement("insert into contact ( " //
				+ "address, birthdate, company, email, favorite, firstName, lastName) values ("
				+ ":address, :birthdate, :company, :email, :favorite, :firstName, :lastName") //
				.bind("address", contact.getAddress())
				.bind("birthdate", contact.getBirthdate())
				.bind("company", contact.getCompany())
				.bind("email", contact.getEmail())
				.bind("favorite", contact.getFavorite())
				.bind("firstName", contact.getFirstName())
				.bind("lastName", contact.getLastName()).executeAndReturnGeneratedKeys(IntegerColumnMapper.PRIMITIVE) //
				.first();
		
		// add phone number		
			if(!contact.getPhones().isEmpty()){
				contact.getPhones().forEach(p -> {
					h.createStatement("insert into phonenumber ( " //
					+ "contact_id, phone_type, phone_number) values (:contact_id, :phoneType, :phoneNumber)") //
					.bind("contact_id", id)
					.bind("phoneType", p.getType())
					.bind("phoneNumber", p.getNumber()).execute();
				});
			}
		contact.setId(id);
		jdbi.close(h);
		return contact;
	}

	// updates an existing user
	public Contact updateContact(Contact contact) {
		Handle h = jdbi.open();
		h.createStatement("update contact set " //
				+ "address = :address, birthdate = :birthdate, company = :company, email =:email, favorite = :favorite," //
				+ "firstName =:firstName, lastName =:lastName where id = :id") //
				.bind("address", contact.getAddress())
				.bind("birthdate", contact.getBirthdate())
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
				+ "phone_type =:phoneType, phone_number =:phoneNumber where contact_id =:id) ") //
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
	
	/**
	 * Map a result set to a Contact object
	 */
	protected class ContactMapper implements ResultSetMapper<Contact> {
		@Override
		public Contact map(int index, ResultSet r, StatementContext ctx) throws SQLException {

			Contact rv = new Contact(r.getInt("id"), r.getString("address"), new Date(r.getInt("birthdate")), r.getString("company"), 
					r.getString("email"), r.getInt("favorite"), r.getString("firstName"),r.getString("lastName"), new ArrayList<Phone>());

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
}
