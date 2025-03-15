package com.socialbookstore.springboot.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity 
@Table(name = "user_profile")
public class UserProfile {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="username")
	private String username;
	
	@Column(name="fullname")
	private String fullname;
	
	@Column(name="address")
	private String address;
	
	@Column(name="age")
	private int age;
	
	@Column(name="phone_number")
	private String phoneNumber;
	
	@Column(name="postcode")
	private String postcode;
	
	@Column(name="state")
	private String state;
	
	
	@OneToOne(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinColumn(name="user_id")
	private User user;
	
	
	
	@ManyToMany(fetch = FetchType.EAGER, 
			cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH})
    @JoinTable(
            name = "userprofiles_authors",
            joinColumns = @JoinColumn(
                    name = "userprofile_id", referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "author_id", referencedColumnName = "id"
            )
    )
	private List<Author> favouriteBookAuthors;
	
	@ManyToMany(fetch = FetchType.EAGER, 
			cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH})
    @JoinTable(
            name = "userprofiles_favebookcateg",
            joinColumns = @JoinColumn(
                    name = "userprofile_id", referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "bookcategory_id", referencedColumnName = "id"
            )
    )
	List<BookCategory> favouriteBookCategories;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name="userprofile_id")
	private List<Book> bookOffers;
	
	
	@ManyToMany(fetch = FetchType.EAGER, 
		cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REFRESH})
	@JoinTable(
	    name = "request_books",
	    joinColumns = @JoinColumn(
	    		 name = "user_profile_id", referencedColumnName = "id"
	    ),
	    inverseJoinColumns = @JoinColumn(
	    		name = "book_id", referencedColumnName = "id"
	    )
	)
	private List<Book> requestedBooks;
	
	
	
	@ManyToMany(fetch = FetchType.EAGER, 
			cascade = CascadeType.ALL)
    @JoinTable(
            name = "notification_userprofiles",
            joinColumns = @JoinColumn(
                    name = "user_profile_id", referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
            		name = "notification_id", referencedColumnName = "id"
            )
    )
	private List<Notification> notifications;
	

	

	public int getId() {
		return id;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Author> getFavouriteBookAuthors() {
		return favouriteBookAuthors;
	}

	public void setFavouriteBookAuthors(List<Author> favouriteBookAuthors) {
		this.favouriteBookAuthors = favouriteBookAuthors;
	}

	public List<BookCategory> getFavouriteBookCategories() {
		return favouriteBookCategories;
	}

	public void setFavouriteBookCategories(List<BookCategory> favouriteBookCategories) {
		this.favouriteBookCategories = favouriteBookCategories;
	}

	public List<Book> getBookOffers() {
		return bookOffers;
	}

	public void setBookOffers(List<Book> bookOffers) {
		this.bookOffers = bookOffers;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public List<Book> getRequestedBooks() {
		return requestedBooks;
	}

	public void setRequestedBooks(List<Book> requestedBooks) {
		this.requestedBooks = requestedBooks;
	}

	public List<Notification> getNotifications() {
		return notifications;
	}

	public void setNotifications(List<Notification> notifications) {
		this.notifications = notifications;
	}
	
	
	
	
	
	
}
