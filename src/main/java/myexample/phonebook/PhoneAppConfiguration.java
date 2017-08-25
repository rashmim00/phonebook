package myexample.phonebook;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

public class PhoneAppConfiguration extends Configuration {
   
	@Valid
	@NotNull
	protected DataSourceFactory database = new DataSourceFactory();
	
	@JsonProperty("database")
	protected DataSourceFactory getDatabase() {
		return database;
	}
}
