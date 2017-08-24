package myexample.phonebook;

import java.util.Date;
import java.util.List;

import lombok.*;

@Data
@AllArgsConstructor
public class Phone {
	private String type;
	private String number;
}
