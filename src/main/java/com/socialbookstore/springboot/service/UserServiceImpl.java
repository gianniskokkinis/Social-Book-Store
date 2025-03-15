package com.socialbookstore.springboot.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.socialbookstore.springboot.dao.BookDAO;
import com.socialbookstore.springboot.dao.UserDAO;
import com.socialbookstore.springboot.dao.UserProfileDAO;
import com.socialbookstore.springboot.model.Book;
import com.socialbookstore.springboot.model.User;
import com.socialbookstore.springboot.model.UserProfile;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private UserProfileDAO userProfileDAO;
	
	@Autowired
	private BookDAO bookDAO;
	
	@Override
	public void saveUser(User user) {
		String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword()); //encrypt password here
        user.setPassword(encodedPassword); //set password here
        userDAO.save(user);	
    }

	@Override
	public boolean isUserPresent(User user) {
		Optional<User> storedUser = userDAO.findByUsername(user.getUsername());
		return storedUser.isPresent();
	}

	// Method defined in Spring Security UserDetailsService interface
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// orElseThrow method of Optional container that throws an exception if Optional result  is null
		return userDAO.findByUsername(username).orElseThrow(
	                ()-> new UsernameNotFoundException(
	                        String.format("USER_NOT_FOUND %s", username)
	                ));
	}

	@Override
	public User findUserByUsername(String Username) {
		User user = userDAO.findByUsername(Username).get();
		return user;
	}

	@Override
	public UserProfile findUserProfile(String username) {
		
		if(userProfileDAO.findByUsername(username).isPresent()) {
			return userProfileDAO.findByUsername(username).get();
		}
		
		
		return null;
	}

	@Override
	public void saveProfile(UserProfile userProfile) {
		userProfileDAO.save(userProfile); //save to database
		
	}

	@Override
	public void deleteProfile(UserProfile userProfile) {
		
		userProfileDAO.delete(userProfile);
		
	}

	@Override
	public List<Book> getSendRequests(String Username) {
		UserProfile UP = userProfileDAO.findByUsername(Username).get();
		List<Book> bookRequestList = UP.getRequestedBooks();
		return bookRequestList;
	}

	@Override
	public List<Book> getAllBookOffers(String Username) {
		UserProfile UP = userProfileDAO.findByUsername(Username).get();
		List<Book> bookOfferList = UP.getBookOffers();
		return bookOfferList;
	}

	@Override
	public List<UserProfile> getRequestingUsers(int bookID) {
		Book book = bookDAO.findById(bookID);
		return book.getRequestingUsers();
	}

	

	@Override
	public void deleteBookOffer(int bookID) {
		Book book = bookDAO.findById(bookID);
		UserProfile UP = book.getUserProfile();
		
		UP.getBookOffers().remove(book);
		
		bookDAO.delete(book);
	}

	

	
}
