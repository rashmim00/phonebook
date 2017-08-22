package myexample.phonebook;

import java.util.List;
import lombok.*;

@Data
public class Group {
	private int id;
	private String name;
	private String icon;
	private List<Contact> participants;
}
