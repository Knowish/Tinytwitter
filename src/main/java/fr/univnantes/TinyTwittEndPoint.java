package fr.univnantes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;


import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.CompositeFilterOperator;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;

import fr.univnantes.User;
@Api(name = "tinytwittAPI",namespace = @ApiNamespace(ownerDomain = "mycompany.com", ownerName = "mycompany.com", packagePath = "services"))
public class TinyTwittEndPoint {
	
	@ApiMethod(name = "createUser", httpMethod = ApiMethod.HttpMethod.POST)
	public void createUser(@Named("login") String login, @Named("email") String email, @Named("pw") String pw,@Named("firstname") String firstname,@Named("lastname") String lastname) {
		
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

		Collection<Filter> filters = new ArrayList<Filter>();
		filters.add(new Query.FilterPredicate("login", Query.FilterOperator.EQUAL, login));
		filters.add(new Query.FilterPredicate("email", Query.FilterOperator.EQUAL, email));
		Filter filter = new Query.CompositeFilter(CompositeFilterOperator.OR, filters);
		Query query = new Query("User").setFilter(filter);
		Entity userEntity = ds.prepare(query).asSingleEntity();
		
		if (userEntity != null){
			 throw new IllegalStateException("User already exist");
		}else{
			userEntity = new Entity("User");
			userEntity.setIndexedProperty("login", login);
			userEntity.setProperty("email", email);
			userEntity.setProperty("pw", pw);
			userEntity.setProperty("firstname", firstname);
			userEntity.setProperty("lastname", lastname);
			
			ds.put(userEntity);
			Entity userFollowersEntity = new Entity("UserFollowers",userEntity.getKey());
			userFollowersEntity.setProperty("followers", new ArrayList<Long>());

			ds.put(userFollowersEntity);
			this.addFollowerUser(login);
			
		}
	}
	
	@ApiMethod(name = "getUser",httpMethod = ApiMethod.HttpMethod.GET)
    public User getUser(@Named("login") String login ) {
		
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		Filter filter = new Query.FilterPredicate("login",FilterOperator.EQUAL, login);
		Query query = new Query("User").setFilter(filter);
		Entity userEntity = ds.prepare(query).asSingleEntity();
       // User user = ofy().load().type(User.class).filter("login", login).first().now();
      
		if(userEntity == null) {
            throw new NullPointerException("User not found.");
        }
        User user = new User(userEntity);
        return user;
    }
	
	 @ApiMethod(name = "createTwitt", httpMethod = ApiMethod.HttpMethod.POST)
	    public void createTwitt(@Named("login") String login, @Named("message") String message) {
	        //User twittos = ofy().load().type(User.class).filter("login", login).first().now();
		 	
		 	DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
			Filter filter = new Query.FilterPredicate("login", Query.FilterOperator.EQUAL, login);
			Query query = new Query("User").setFilter(filter);
			Entity userEntity = ds.prepare(query).asSingleEntity();
		 	User userid = this.getUser(login);

		 	if (userid == null){throw new NullPointerException("User not found");}
		 	
		 	Entity twittEntity = new Entity("Twitt");
		 	twittEntity.setProperty("idAuthor", userid.getId());
		 	twittEntity.setProperty("nameAuthor", login);
		 	twittEntity.setProperty("message", message);
		 	twittEntity.setProperty("date", new Date());
		 	ds.put(twittEntity);
		 	
		 	query = new Query("UserFollowers").setAncestor(userEntity.getKey());
			Entity userFollowersEntity = ds.prepare(query).asSingleEntity();
			
			if (userFollowersEntity == null){throw new NullPointerException("No followers found.");}
		 	
		 	@SuppressWarnings("unchecked")
			ArrayList<Long> followers = (ArrayList<Long>) userFollowersEntity.getProperty("followers");
			
			Entity twittIndex = new Entity("TwittIndex", twittEntity.getKey());
			twittIndex.setProperty("receivers", followers);
			ds.put(twittIndex);	
		 	
	    }
	 
	 
	 @ApiMethod(name = "getTimeline", httpMethod = ApiMethod.HttpMethod.GET)
	    public List<Twitt> getTimeline(@Named("login") String login) {

	        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
	        Filter filter = new Query.FilterPredicate("login", Query.FilterOperator.EQUAL, login);
	        Query query = new Query("User").setFilter(filter);
	        Entity userEntity = ds.prepare(query).asSingleEntity();

	        if (userEntity == null)
	            throw  new NullPointerException("User not found." + login);

	        Long id = userEntity.getKey().getId();

	        filter = new Query.FilterPredicate("receivers", Query.FilterOperator.EQUAL, id);
	        query = new Query("TwittIndex").setFilter(filter).setKeysOnly();

	        List<Entity> twittKeysEntity = ds.prepare(query).asList(FetchOptions.Builder.withDefaults());

	        if (twittKeysEntity == null)
	            throw new NullPointerException("No twitt found");

	        List<Key> keys = new ArrayList<Key>();
	        for(Entity e : twittKeysEntity){
	            Key k = e.getParent();
	            keys.add(k);
	        }

	        Map<Key, Entity> map = ds.get(keys);
	        List<Entity> list =  new ArrayList<Entity>(map.values());

	        List<Twitt> result = new ArrayList<Twitt>();
	        for(Entity e : list){
	            result.add(new Twitt(e));
	        }
	        
	        Collections.sort(result);
	        return result;

	    }
	 
	 @ApiMethod(name = "followUser", httpMethod = ApiMethod.HttpMethod.POST)
	    public void followUser(@Named("follower") String followerLogin, @Named("followed") String followedLogin) {
	       
		 DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
			Collection<Filter> filters = new ArrayList<Filter>();
			filters.add(new Query.FilterPredicate("login", Query.FilterOperator.EQUAL, followerLogin));
			filters.add(new Query.FilterPredicate("login", Query.FilterOperator.EQUAL, followedLogin));
			Filter filter = new Query.CompositeFilter(CompositeFilterOperator.OR, filters);
			Query query = new Query("User").setFilter(filter);
			List<Entity> entities = ds.prepare(query).asList(FetchOptions.Builder.withDefaults());
			
			if(entities == null){throw new NullPointerException("Entities null");}
			
			Entity followed = null;
			Entity follower = null;
			
			for(Entity user : entities){
				String login = (String) user.getProperty("login");
				if(login == null){throw new NullPointerException("Login null");}
				if(login.equals(followedLogin)){followed = user;}
				else{follower = user;}
			}
			
			if (follower == null){
				throw new NullPointerException("Follower user not found"+followerLogin);
			}
			if(followed == null){
				throw new NullPointerException("Followed user not found");
			}
			
			query = new Query("UserFollowers").setAncestor(followed.getKey());
			Entity userFollowers = ds.prepare(query).asSingleEntity();
			
			@SuppressWarnings("unchecked")
			ArrayList<Long> followers = (ArrayList<Long>) userFollowers.getProperty("followers");
			
			if(followers == null){followers = new ArrayList<Long>();}
			
			if(followers.contains((Long) follower.getKey().getId())){
				throw new NullPointerException("Already following this user");
			}else{
				followers.add((Long) follower.getKey().getId());
				userFollowers.setProperty("followers", followers);
				ds.put(userFollowers);
			}
	    }
	 
	 @ApiMethod(name = "addFollowerUser")
		public void addFollowerUser(@Named("loginFollowed") String loginFollowedFollower) {
		 
			DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
			Filter filter = new Query.FilterPredicate("login", Query.FilterOperator.EQUAL, loginFollowedFollower);
			Query query = new Query("User").setFilter(filter);
			Entity entity = ds.prepare(query).asSingleEntity();
			
			if(entity == null){throw new NullPointerException("Entity null");}
			
			Entity followed = entity;
			Entity follower = entity;
			query = new Query("UserFollowers").setAncestor(followed.getKey());
			Entity userFollowers = ds.prepare(query).asSingleEntity();
			
			
			@SuppressWarnings("unchecked")
			ArrayList<Long> followers = (ArrayList<Long>) userFollowers.getProperty("followers");
			if(followers == null){followers = new ArrayList<Long>();}
			
			if(followers.contains((Long) follower.getKey().getId())){
				throw new NullPointerException("Already following this user");
			}else{
				followers.add((Long) follower.getKey().getId());
				userFollowers.setProperty("followers", followers);
				ds.put(userFollowers);
			}
		}
	 
	/* @ApiMethod(name = "existUser",httpMethod = ApiMethod.HttpMethod.GET)
	    public Boolean existUser(@Named("login") String login) {
		 
			DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
			Filter filter = new Query.FilterPredicate("login",FilterOperator.EQUAL, login);
			Query query = new Query("User").setFilter(filter);
			Entity userEntity = ds.prepare(query).asSingleEntity();
	      
			if(userEntity == null) {
	            return false;
	        }
	       
	        return true;
	    }*/
	 
	 @ApiMethod(name = "createNbUsers")
		public ArrayList<String> createNbUsers(@Named("nbUsers") int nbUsers) {
			
			String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
			
			ArrayList<String> listLogin = new ArrayList<>();
			
			String login = "";
			String email = "";
			String password = "";
			String firstname = "";
			String lastname = "";
			for(int i = 1; i <= nbUsers; i++){
				
				String rand = "";
				
				for(int j = 1; j <= 10; j++){
					int k = (int)Math.floor(Math.random() * 62);
					rand += chars.charAt(k);
				}
				login =  "user" + rand;
				email = "mail" + rand + "@supermail.com";
				password = "password" + rand;
				firstname = "firtsname" + rand;
				lastname = "lastname" + rand;

				createUser(login, email, password, firstname, lastname);
				
				listLogin.add(login);
			}
			return listLogin;
	}
	 
	 @ApiMethod(name = "createNbFollowers")
		public void createNbFollowers(@Named("nbFollowers") int nbFollowers, @Named("followed") String followed) {
			
			DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
			Filter filter = new Query.FilterPredicate("login", Query.FilterOperator.EQUAL, followed);
			Query query = new Query("User").setFilter(filter);
			Entity userEntity = ds.prepare(query).asSingleEntity();
			
			if (userEntity == null){
				throw new NullPointerException("User not found");
			}
			
			ArrayList<String> listLogin = new ArrayList<>();
			listLogin = createNbUsers(nbFollowers);
			
			for(int i = 0; i < listLogin.size(); i++){
				followUser(followed, listLogin.get(i));
				
				ArrayList<String> listTwitt = new ArrayList<>();
				listTwitt.add("The concept of global warming was created by and for the Chinese in order to make U.S. manufacturing non-competitive.");
				listTwitt.add("Why would Kim Jong-un insult me by calling me \"old,\" when I would NEVER call him \"short and fat?\" Oh well, I try so hard to be his friend - and maybe someday that will happen!");
				listTwitt.add("It's freezing and snowing in New York--we need global warming!");
				listTwitt.add("Healthy young child goes to doctor, gets pumped with massive shot of many vaccines, doesn't feel good and changes - AUTISM. Many such cases!");
				listTwitt.add("An 'extremely credible source' has called my office and told me that @BarackObama's birth certificate is a fraud.");
				int k = (int)Math.floor(Math.random() * 5);
				createTwitt(listLogin.get(i),listTwitt.get(k));
			}
	}

}
