package com.socialbookstore.springboot.modeltests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.socialbookstore.springboot.dao.UserProfileDAO;
import com.socialbookstore.springboot.model.Author;
import com.socialbookstore.springboot.model.Book;
import com.socialbookstore.springboot.model.BookCategory;
import com.socialbookstore.springboot.model.User;
import com.socialbookstore.springboot.model.UserProfile;

import jakarta.transaction.Transactional;


@SpringBootTest
@TestPropertySource(
		  locations = "classpath:application.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class userProfileTests {

	
	@Autowired
	UserProfileDAO UPDAO;

	
	
	@Test
	@Transactional
	void CreateUserProfileTest() {
		
		User user = new User();
		user.setUsername("TEST");
		user.setEmail("TEST");
		
		UserProfile UP = new UserProfile();
		UP.setFullname("TEST1");
		UP.setAddress("TEST1");
		UP.setAge(12);
		UP.setState("TEST");
		UP.setPostcode("1111");
		UP.setUser(user);
		
		
		
		assertTrue(
				(UP.getUser().getUsername().equals(user.getUsername())) &&
				(UP.getFullname()=="TEST1") && (UP.getAddress().equals("TEST1")) &&
				(UP.getAge() == 12) && (UP.getState().equals("TEST")) &&
				(UP.getPostcode().equals("1111")) && (UP.getUser().getEmail().equals("TEST"))
				);
		
	}
	

	@Test
	@Transactional
	void favouriteAuthorTest() {
		
		UserProfile UP = new UserProfile();
		Author author = new Author();
		author.setName("Test");
		List<Author> favAuthors = List.of(author);
		UP.setFavouriteBookAuthors(favAuthors);
		
		assertTrue(UP.getFavouriteBookAuthors().get(0).getName().equals(author.getName()));
		
		
	}
	
	@Test
	@Transactional
	void favouriteCategoryTest() {
		
		UserProfile UP = new UserProfile();
		BookCategory BC = new BookCategory();
		BC.setName("TEST");
		List<BookCategory> favBookCategory = List.of(BC);
		UP.setFavouriteBookCategories(favBookCategory);
		
		assertTrue(UP.getFavouriteBookCategories().get(0).getName().equals(BC.getName()));
		
	}
	
	
	@Test
	@Transactional
	void BookOfferTest() {

		UserProfile UP = new UserProfile();
		Book book = new Book();
		book.setTitle("Book");
		
		List<Book> bookOffers = List.of(book);
		UP.setBookOffers(bookOffers);
		
		assertTrue(UP.getBookOffers().get(0).getTitle().equals(book.getTitle()));
		
	}
	
	@Test
	@Transactional
	void RequestedBooksTest() {
		UserProfile UP = new UserProfile();
		UP.setFullname("TEST");
		
		Book book = new Book();
		book.setTitle("Title");
		book.setSummary("info test");
		
		List<Book> requestedBooks = List.of(book);
		UP.setRequestedBooks(requestedBooks);
		
		assertTrue(UP.getRequestedBooks().get(0).getTitle().equals(book.getTitle()));
		
	}
	
	
	@Test
	@Transactional
	void requestingUsersTest() {
		
		UserProfile UP = new UserProfile();
		UP.setFullname("TEST");
		
		Book book = new Book();
		book.setTitle("Title");
		book.setSummary("info test");
		
		
		List<UserProfile> requestingUsers = List.of(UP);
		book.setRequestingUsers(requestingUsers);
		
		assertTrue(book.getRequestingUsers().get(0).getFullname().equals(UP.getFullname()));//test 
		
	}
	
	@Test
	@Transactional
	void UserProfieltest() {
		
		User user = new User();
		user.setUsername("something here");
		user.setPassword("");
		
		
		UserProfile UP = new UserProfile();
		
		UP.setUser(user);
		UP.setUsername(user.getUsername());
		UP.setFullname("Diomidis Papadopoulos");
		UP.setAge(15);
		UP.setAddress("Ela na me vreis");
		UP.setPhoneNumber("6958745287");
		
		UPDAO.save(UP);
		
		
		UserProfile checkUP = UPDAO.findById(UP.getId());
		UPDAO.delete(checkUP);
		
		assertTrue(checkUP != null);
		
		
		
		
	}
	
	
	@Test
	@Transactional
	void UserProfileRequestedBooksTest() {
		
		
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
		
		UP.setRequestedBooks(myBooks);
		
		UPDAO.save(UP);
		
		
		UserProfile checkUserProfile = UPDAO.findByUsername("testuser1").get();
		UPDAO.delete(UP);
		
		
		
		
		assertTrue(checkUserProfile.getRequestedBooks().get(0).getTitle().equals(book.getTitle()));
		assertTrue(checkUserProfile.getRequestedBooks().get(1).getTitle().equals(book2.getTitle()));
		
		
		
		//test 
		
		
		
		
		
		
		
		
		
	}
	
	
	@Test
	@Transactional
	void deleteFromBookOffer() {
		
		
		//here we are going to fix test
		
		
		UserProfile UP = new UserProfile();
		UP.setUsername("TEST_USER_DELETE_AFTER");
		
		Book book = new Book();
		
		book.setTitle("testBookHereDeleteAfter");
		book.setSummary("infotesthere");
		
		List<Book> bookOffers = new ArrayList<Book>();
		bookOffers.add(book);
		UP.setBookOffers(bookOffers);
		
		UPDAO.save(UP);
		
		//HERE DELETE BOOK OFFER 
		
		UserProfile checkUP = UPDAO.findById(UP.getId());
		
		List<Book> getBookOffers = checkUP.getBookOffers();
		getBookOffers.remove(book);
		
		UPDAO.save(checkUP);
		
		
		
		
		
	}
	
	@Test
	@Transactional
	void testFavCategory() {
		
		List<Book> vivlia = new ArrayList<Book>();
		
		
		Book book1 = new Book();
		book1.setTitle("crime on the frontier");
		book1.setSummary("sd");
		vivlia.add(book1);
		
		Book book2 = new Book();
		book2.setTitle("and then there were none");
		book2.setSummary("sd");
		vivlia.add(book2);
		
		Book book3 = new Book();
		book3.setTitle("death on the nile");
		book3.setSummary("sds");
		vivlia.add(book3);
		
		Book book4 = new Book();
		book4.setTitle("haunted venice");
		book4.setSummary("s");
		vivlia.add(book4);
		
		
		String keyword = "who on you";
		
		String keys[] = keyword.split(" ");
		
		for (String checkWord: keys) {
			for (Book checkBook: vivlia) {
				if (checkBook.getTitle().contains(checkWord)) {
					System.out.println(checkBook.getTitle());
				}
			}
		}
		
		
		
		
		
	
		
		
	}
	
	
	

}
