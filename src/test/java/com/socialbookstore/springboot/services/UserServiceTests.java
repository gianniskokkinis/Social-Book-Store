package com.socialbookstore.springboot.services;


import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.socialbookstore.springboot.dao.BookDAO;
import com.socialbookstore.springboot.dao.UserProfileDAO;
import com.socialbookstore.springboot.model.Book;
import com.socialbookstore.springboot.model.UserProfile;
import com.socialbookstore.springboot.service.BookStoreService;
import com.socialbookstore.springboot.service.UserService;

import jakarta.transaction.Transactional;


@SpringBootTest
@TestPropertySource(
		  locations = "classpath:application.properties")
class UserServiceTests {
	
	
	@Autowired
	UserService userService;

	
	@Autowired
	BookDAO BDAO;
	
	@Autowired
	UserProfileDAO UPDAO;
	
	@Autowired
	BookDAO bookDAO;
	
	/*!!! THIS SERVICE IS ONLY HERE TO TEST deleteBookOffer() !!! */
	@Autowired
	BookStoreService BSS;
	
	@Test
	@Transactional
	void saveDeleteUserProfileServiceTest() {
		UserProfile UP = new UserProfile();
		UP.setUsername("testuser1");
		
		userService.saveProfile(UP);
		
		UserProfile checkProfile = userService.findUserProfile(UP.getUsername());
		UPDAO.delete(UP);
		
		
		assertTrue(checkProfile.getUsername().equals(UP.getUsername()));
		
	}
	
	
	@Test
	@Transactional
	void getSendRequestsTest() {
		
		UserProfile UP = new UserProfile();
		UP.setUsername("TEST");
		
		Book book = new Book();
		book.setTitle("TEST_BOOK");
		book.setSummary("info here test");
		
		List<Book> requestedBooks = List.of(book);
		UP.setRequestedBooks(requestedBooks);
		UPDAO.save(UP);
		
		List<Book> checkRequestedBooks = userService.getSendRequests(UP.getUsername());
		
		
		UP.setRequestedBooks(new ArrayList<Book>());
		UPDAO.save(UP);
		UPDAO.delete(UP);
		bookDAO.delete(book);
		
		assertTrue(checkRequestedBooks.get(0).getTitle().equals(book.getTitle()));
		
		
		
	}
	
	
	@Test
	@Transactional
	void getBookOfferstest() {
		
		
		UserProfile UP = new UserProfile();
		UP.setUsername("testuser1");
		
		
		Book book = new Book();
		Book book2 = new Book();
		
		
		book.setTitle("Book1");
		book.setSummary("HELLO WORLD");
		book2.setTitle("Book2");
		book2.setSummary("HELLO WORLD");
		
		book.setUserProfile(UP);
		book2.setUserProfile(UP);
		
		List<Book> myBooks = List.of(book,book2);
		
		UP.setBookOffers(myBooks);
		
		
		
		userService.saveProfile(UP);
		
		
		List<Book> testOfferBooks = userService.getAllBookOffers("testuser1");
		userService.deleteProfile(UP);
		
		
		
		assertTrue(testOfferBooks.get(0).getTitle().equals(book.getTitle()));
		assertTrue(testOfferBooks.get(1).getTitle().equals(book2.getTitle()));
		
		
		
	}
	
	
	@Test
	@Transactional
	void getRequestingUsersTest() {
		
		UserProfile UP = new UserProfile();
		UP.setUsername("TEST");
		
		
		Book book = new Book();
		book.setTitle("TEST");
		book.setSummary("TEST");
		book.setRequestingUsers(List.of(UP));
		bookDAO.save(book);
		
		
		List<UserProfile> checkUserProfiles = userService.getRequestingUsers(book.getId());
		
		
		assertTrue(
					(checkUserProfiles.size() == 1)
					&& (checkUserProfiles.get(0).getUsername().equals(UP.getUsername()))
				);
		
		
	}
	
	
	@Test
	@Transactional
	void deleteBookOfferTest() {
		
		UserProfile UP = new UserProfile();
		UP.setUsername("Test");
		UP.setBookOffers(new ArrayList<Book>());
		
		
		UPDAO.save(UP);
		
		
		BSS.createBookOffer(UP.getUsername(), "Title 1", "Ebooks", "Info here", "Author 1");	
		
		Book book = bookDAO.findByTitle("Title 1").get();
		book.setUserProfile(UP);
		bookDAO.save(book);
		
		
		userService.deleteBookOffer(book.getId());
		
		
		UserProfile checkUserProfile = UPDAO.findById(UP.getId());
		
		assertTrue(checkUserProfile.getBookOffers().isEmpty());
		
		
		
		
		
	}
	
	

}
