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

    /**
     * Creates a new user in the datastore with several informations related to connection
     * @param login the chosen nickname
     * @param email the user's email
     * @param pw the chosen password
     * @param firstname the user's firstname
     * @param lastname the user's lastname
     */
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

    /**
     * A simple method to connect using only a username
     * @param login the nickname who wants to connect
     * @return the user that just connected
     */
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

    /**
     * Sends a tweet from a user to the datastore
     * @param login The nickname of the user who tweets
     * @param message The content of the tweet
     * @throws EntityNotFoundException
     */
    @ApiMethod(name = "createTwitt", httpMethod = ApiMethod.HttpMethod.POST)
    public void createTwitt(@Named("login") String login, @Named("message") String message) throws EntityNotFoundException {
        //User twittos = ofy().load().type(User.class).filter("login", login).first().now();

        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        Filter filter = new Query.FilterPredicate("login", Query.FilterOperator.EQUAL, login);
        Query query = new Query("User").setFilter(filter);
        Entity userEntity = ds.prepare(query).asSingleEntity();
        User userid = this.getUser(login);

        Entity twittEntity = new Entity("Twitt");
        twittEntity.setProperty("idAuthor", userid.getId());
        twittEntity.setProperty("nameAuthor", login);
        twittEntity.setProperty("message", message);
        twittEntity.setProperty("date", new Date());
        ds.put(twittEntity);

        query = new Query("UserFollowers").setAncestor(userEntity.getKey());
        Entity userFollowersEntity = ds.prepare(query).asSingleEntity();

        if (userFollowersEntity == null){throw new EntityNotFoundException(null);}

        @SuppressWarnings("unchecked")
        ArrayList<Long> followers = (ArrayList<Long>) userFollowersEntity.getProperty("followers");

        Entity twittIndex = new Entity("TwittIndex", twittEntity.getKey());
        twittIndex.setProperty("receivers", followers);
        ds.put(twittIndex);

    }

    /**
     * To obtain the tweets of a certain user
     * @param login the nickname of the desired user
     * @return
     */
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

    /**
     * The calling user will follow the second user
     * @param followerLogin the caller's nickname
     * @param followedLogin the other twitto's nickname
     */
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

        basicFollow(ds, follower, userFollowers, followers);
    }

    private void basicFollow(DatastoreService ds, Entity follower, Entity userFollowers, ArrayList<Long> followers) {
        if(followers.contains((Long) follower.getKey().getId())){
            throw new NullPointerException("Already following this user");
        }else{
            followers.add((Long) follower.getKey().getId());
            userFollowers.setProperty("followers", followers);
            ds.put(userFollowers);
        }
    }

    /**
     * A method to make a user follow him/herself, so he/she can see his/her tweets
     * @param loginFollowedFollower the user's nickname
     */
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

        basicFollow(ds, follower, userFollowers, followers);
    }

}
