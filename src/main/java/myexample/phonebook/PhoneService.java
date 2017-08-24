package myexample.phonebook;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PhoneService {

	// returns a list of all contacts
	public List<Contact> getAllContacts() {
		ArrayList<Contact> all = new ArrayList<Contact>();
		List<Phone> phones = new ArrayList<Phone>();
		phones.add(new Phone("home", "213-234-3456"));
		phones.add(new Phone("Mobile", "213-245-6789"));
		
		all.add(new Contact(1,"12345 West Niles Dr, Los Angeles, CA", new Date(),"Mixer Records Ltd","alicia.keyes@gmail.com",1,"Alicia","Keyes",phones));

		all.add(new Contact(2,"636 Chelsea Crossing, San Jose, CA", null,"","rrrr@gmail.com",1,"Rashmi","Goyal",phones));
	
		return all;
	}

	// returns a single user by id
	public Contact getContact(int id) {
		if (id == 0)
			return null;
		return null;
	}

	// creates a new contact
	public Contact createContact(String name, String email) {
		return new Contact();
	}

	// updates an existing user
	public Contact updateContact(int id, String name, String email) {
		return null;
	}
}
