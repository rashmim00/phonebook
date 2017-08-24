package myexample.phonebook;

import javax.inject.Singleton;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class PhoneApplication extends Application<PhoneAppConfiguration> {
    public static void main(String[] args) throws Exception {
        new PhoneApplication().run(args);
    }

    @Override
    public String getName() {
        return "phone_book";
    }

    @Override
    public void initialize(Bootstrap<PhoneAppConfiguration> bootstrap) {
        // nothing to do yet
    	bootstrap.addBundle(new AssetsBundle("/assets/", "/", "index.html"));
    }

    @Override
	public void run(PhoneAppConfiguration configuration, Environment environment) {

		// register our resources
		environment.jersey().register(new PhoneResource(new PhoneService()));	
	
	}
}