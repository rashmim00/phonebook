package myexample.phonebook;

import java.util.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contact {
	private int id;
	private String address;
	private Date birthdate;
	private String company;
	private String email;
	private int favorite;
	private String firstName;
	private String lastName;
	List<Phone> phones;
}
