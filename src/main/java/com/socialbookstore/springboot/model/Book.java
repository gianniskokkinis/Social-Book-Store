package com.socialbookstore.springboot.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;


@Entity
@Table(name="Book")
public class Book {
	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	
	@Column(name="title")
	private String title;
	
	@Column(name="publish_date")
	private String publish_date;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH})
	@JoinColumn(name="category_id")
	private BookCategory category;
	
	@Column(name="summary")
	private String summary;
	
	@ManyToMany(fetch = FetchType.EAGER, 
			cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH})
    @JoinTable(
            name = "authors_book",
            joinColumns = @JoinColumn(
                    name = "book_id", referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
            		name = "author_id", referencedColumnName = "id"
            )
    )
	private List<Author> Authors;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="userprofile_id")
	private UserProfile userProfile;
	
	@ManyToMany(mappedBy="requestedBooks", cascade={CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH})
	private List<UserProfile> requestingUsers;
	
	
	
	
	
	
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public BookCategory getCategory() {
		return category;
	}

	public void setCategory(BookCategory category) {
		this.category = category;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public List<Author> getAuthors() {
		return Authors;
	}

	public void setAuthors(List<Author> authors) {
		this.Authors = authors;
	}

	public int getId() {
		return id;
	}

	public UserProfile getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}

	public List<UserProfile> getRequestingUsers() {
		return requestingUsers;
	}

	public void setRequestingUsers(List<UserProfile> requestingUsers) {
		this.requestingUsers = requestingUsers;
	}

	public String getPublish_date() {
		return publish_date;
	}

	public void setPublish_date(String publish_date) {
		this.publish_date = publish_date;
	}

	
	
	
}
