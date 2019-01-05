package fr.univnantes;

import static com.googlecode.objectify.ObjectifyService.ofy;

//import javax.persistence.EntityExistsException;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.googlecode.objectify.cmd.Query;

@Api(name = "tinytwittAPI",namespace = @ApiNamespace(ownerDomain = "mycompany.com", ownerName = "mycompany.com", packagePath = "services"))
public class TinyTwittEndPoint {
	
	
	
	@ApiMethod(name ="createUser")
	public void createUser(@Named("login") String login, @Named("email") String email, @Named("pw") String pw,@Named("firstname") String firstname,@Named("lastname") String lastname) {
		
		Query<User> q1 = ofy().load().type(User.class).filter("login",login);
		Query<User> q2 = ofy().load().type(User.class).filter("email",email);
		
		if(q1.count() == 0 && q2.count() == 0) {
			
			System.out.println("CAAAAAAAAAAAA MAAAAAARCHE");
				
			ofy().save().entity(new User(login,email,pw,firstname,lastname)).now();
		}else {
			//throw new EntityExistsException("User already exist : " + login + " or email : "+ email);
		}
	}
	
/*	@ApiMethod(name ="retourneUn")
	public String retournTaDaronne() {
		return "TaDaronne dans l'API";
	}
*/
}
