package com.socialbookstore.springboot.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name="Author")
public class Author {

	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name="name")
	private String name;
	
	
	@ManyToMany(fetch= FetchType.EAGER ,mappedBy = "Authors", cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH})
	private List<Book> writtenBooks;
	
	
	@ManyToMany(mappedBy="favouriteBookAuthors")
	private List<UserProfile> userProfiles;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Book> getWrittenBooks() {
		return writtenBooks;
	}

	public void setWrittenBooks(List<Book> writtenBooks) {
		this.writtenBooks = writtenBooks;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<UserProfile> getUserProfiles() {
		return userProfiles;
	}

	public void setUserProfiles(List<UserProfile> userProfiles) {
		this.userProfiles = userProfiles;
	}
	
	
	
	
	
}
