package fr.univnantes;

import java.io.Serializable;
import java.util.List;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class User implements Serializable{
	
	@Id
	private Long id;
	
	private String login;
	
	private String email;
	
	private String pw;
	
	private String firstname;
	
	private String lastname;
	
	private List<Long> followers;
	
	public User() {}
	
	public User(com.google.appengine.api.datastore.Entity e) {
		
		this.setId((Long) e.getKey().getId());
		this.setLogin((String) e.getProperty("login"));
		this.email = (String) e.getProperty("login");
		this.setPw((String) e.getProperty("login"));
		this.setFirstname((String) e.getProperty("login"));
		this.setLastname((String) e.getProperty("login"));
		
	}

	private Long getId() {
		return id;
	}

	private void setId(Long id) {
		this.id = id;
	}

	private String getLogin() {
		return login;
	}

	private void setLogin(String login) {
		this.login = login;
	}

	private List<Long> getFollowers() {
		return followers;
	}

	private void setFollowers(List<Long> followers) {
		this.followers = followers;
	}

	private String getPw() {
		return pw;
	}

	private void setPw(String pw) {
		this.pw = pw;
	}

	private String getFirstname() {
		return firstname;
	}

	private void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	private String getLastname() {
		return lastname;
	}

	private void setLastname(String lastname) {
		this.lastname = lastname;
	}

}