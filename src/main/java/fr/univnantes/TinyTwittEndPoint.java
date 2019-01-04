package fr.univnantes;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.ArrayList;

import javax.persistence.EntityExistsException;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.appengine.api.datastore.Entity;
import com.googlecode.objectify.cmd.Query;

@Api(name = "TinyTwittAPI",version = "1.0")
public class TinyTwittEndPoint {
	
	
	@ApiMethod(name ="createUser")
	public void createUser(@Named("login") String login, @Named("email") String email, @Named("pw") String pw,@Named("firstname") String firstname,@Named("lastname") String lastname) {
		
		Query<User> q1 = ofy().load().type(User.class).filter("login",login);
		Query<User> q2 = ofy().load().type(User.class).filter("email",email);
		
		if(q1.count() == 0 && q2.count() == 0) {
			
			System.out.println("CAAAAAAAAAAAA MAAAAAARCHE");
			Entity newuser = new Entity("User");
			newuser.setProperty("login", login);
			newuser.setProperty("email", email);
			newuser.setProperty("pw", pw);
			newuser.setProperty("firstname", firstname);
			newuser.setProperty("lastname", lastname);
			newuser.setProperty("followers", new ArrayList<Long>());
			
			ofy().save().entity(new User(newuser)).now();
		}else {
			throw new EntityExistsException("User already exist : " + login + " or email : "+ email);
		}
	}

}
