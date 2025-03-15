package com.socialbookstore.springboot.model;

import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="notification")
public class Notification {
	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	
	@Column(name="description")
	private String description;

	
	@Column(name="book_title")
	private String bookTitle;
	
	
	@Column(name="created_date")
	private String notDate;
	
	
	
	//edw thelw ligo eksigisi
	
	
	
	@ManyToMany(mappedBy="notifications", cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH})
	private List<UserProfile> userProfiles;
	


	public List<UserProfile> getUserProfiles() {
		return userProfiles;
	}


	public void setUserProfiles(List<UserProfile> userProfiles) {
		this.userProfiles = userProfiles;
	}


	


	public int getId() {
		return id;
	}



	public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
	}



	public String getBookTitle() {
		return bookTitle;
	}



	public void setBookTitle(String bookTitle) {
		this.bookTitle = bookTitle;
	}


	public String getNotDate() {
		return notDate;
	}


	public void setNotDate(String notDate) {
		this.notDate = notDate;
	}



	
	

	
	
}