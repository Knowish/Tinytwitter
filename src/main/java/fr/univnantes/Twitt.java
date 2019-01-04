package fr.univnantes;

import java.io.Serializable;
import java.util.Date;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

//import com.google.appengine.api.datastore.*;

@Entity
public class Twitt implements Serializable, Comparable<Twitt> {
	
	@Id
	private Long id;
	
	private String author;
	
	private String message;
	
	private Date date;
	
	public Twitt() {}
	
	public Twitt(com.google.appengine.api.datastore.Entity e) {
		
		this.author =  (String) e.getProperty("author");
		this.message = (String) e.getProperty("message");
		this.date = (Date) e.getProperty("date");
		
		
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public int compareTo(Twitt o) {
	   return getDate().compareTo(o.getDate());
    }
	
	

}
