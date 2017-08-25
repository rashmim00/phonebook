package myexample.phonebook;

import lombok.*;

@Data
@AllArgsConstructor
public class Phone {
	private int id;
	private String type;
	private String number;
}
