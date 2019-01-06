package fr.univnantes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Entity;

@PersistenceCapable(identityType=IdentityType.APPLICATION)
public class User implements Serializable{
	
	@PrimaryKey
	@Persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
	private Long id;
	@Persistent
	private String login;
	@Persistent
	private String email;
	@Persistent
	private String mdp;
	@Persistent
	private String prenom;
	@Persistent
	private String nom;
	
	@Persistent
	private List<Long> followers;//Liste des gens qui me suivent
/*
    private List<Long> following;//Liste des gens que je suis*/

	public User() {}
	
	public User(Entity e) {
		id = (Long) e.getKey().getId();
		login = (String) e.getProperty("login");
		email = (String) e.getProperty("email");
		mdp = (String) e.getProperty("pw");
		prenom = (String) e.getProperty("firstname");
		nom = (String) e.getProperty("lastname");
		/*followers = new ArrayList<Long>();
		following = new ArrayList<Long>();*/
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMdp() {
		return mdp;
	}
	public void setMdp(String mdp) {
		this.mdp = mdp;
	}
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}

	public List<Long> getFollowers() {
		return followers;
	}
	
	public boolean addFollower(Long idFollower) {
        return this.followers.add(idFollower);
    }
	
	public void setFollowers(List<Long> followers) {
		this.followers = followers;
	}
	/*
	public List<Long> getFollowing() {
        return following;
    }

    public void setFollowing(List<Long> following) {
        this.following = following;
    }

    public boolean addFollowing(Long idFollowing) {
        return this.following.add(idFollowing);
    }

    public boolean containsFollower(Long idFollower) {
        return followers.contains(idFollower);
    }

    public boolean containsFollowing(Long idFollowing) {
        return following.contains(idFollowing);
    }
*/
}