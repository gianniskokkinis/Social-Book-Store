package com.socialbookstore.springboot.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.socialbookstore.springboot.dao.AuthorDAO;
import com.socialbookstore.springboot.dao.BookCategoryDAO;
import com.socialbookstore.springboot.dao.BookDAO;
import com.socialbookstore.springboot.dao.UserProfileDAO;
import com.socialbookstore.springboot.model.Author;
import com.socialbookstore.springboot.model.Book;
import com.socialbookstore.springboot.model.BookCategory;
import com.socialbookstore.springboot.model.Notification;
import com.socialbookstore.springboot.model.User;
import com.socialbookstore.springboot.model.UserProfile;
import com.socialbookstore.springboot.service.BookStoreService;

import jakarta.transaction.Transactional;
@SpringBootTest
@TestPropertySource(
		  locations = "classpath:application.properties")
@ExtendWith(SpringExtension.class)
class BookStoreServiceTests {

	@Autowired
	BookStoreService BSS;
	
	@Autowired
	UserProfileDAO UPDAO;
	
	@Autowired
	BookDAO bookDAO;
	
	@Autowired
	BookCategoryDAO bookCategoryDAO;
	
	@Autowired 
	AuthorDAO authorDAO;
	
	
	@Test
	@Transactional
	void createBookOffer() {
		
		UserProfile UP = new UserProfile();
		UP.setUsername("Test");
		UP.setBookOffers(new ArrayList<Book>());
		
		
		UPDAO.save(UP);
		
		
		BSS.createBookOffer(UP.getUsername(), "Title 1", "Ebooks", "Info here", "Author 1");
		
		
		UserProfile checkUserProfile = UPDAO.findByUsername(UP.getUsername()).get();
		
		assertTrue(
				//check book here
				(checkUserProfile.getBookOffers().get(0).getTitle().equals("Title 1"))
				&& (checkUserProfile.getBookOffers().get(0).getSummary().equals("Info here"))
				&& (checkUserProfile.getBookOffers().get(0).getAuthors().get(0).getName().equals("Author 1"))

				
				);
		
	}
	
	
	@Test
	@Transactional
	void getBookCategoriesTests() {
		
		List<BookCategory> bookCategories = BSS.getAllBookCategories();
		
		assertTrue(
				(bookCategories.get(0).getName().equals("Art"))
				);
		
	}
	
	
	@Test
	@Transactional
	void getAllBooksFromStoreTests() {
		
		UserProfile UP = new UserProfile();
		UP.setUsername("Test");
		UP.setBookOffers(new ArrayList<Book>());
		
		
		UPDAO.save(UP);
		
		
		BSS.createBookOffer(UP.getUsername(), "Title 1", "Ebooks", "Info here", "Author 1");
		
		
		List<Book> storeBooks = BSS.getAllBooks();
		
		assertTrue(storeBooks.get(0).getTitle().equals("Title 1"));
		
		
	}
	
	@Test
	@Transactional
	void sendRequestTest() {
		
		//create book offer first 
		UserProfile UP = new UserProfile();
		UP.setUsername("Test");
		UP.setBookOffers(new ArrayList<Book>());
		
		
		UPDAO.save(UP);
		
		
		BSS.createBookOffer(UP.getUsername(), "Title 1", "Ebooks", "Info here", "Author 1");
		
		
		//send requestNow
		
		UserProfile requestUser = new UserProfile();
		requestUser.setUsername("req_user");
		UPDAO.save(requestUser);
		
		
		BSS.sendRequest(requestUser.getUsername(), bookDAO.findByTitle("Title 1").get().getId());
		
		UserProfile checkUserProfile = UPDAO.findByUsername(requestUser.getUsername()).get();
		
		assertTrue(checkUserProfile.getRequestedBooks().get(0).getTitle().equals("Title 1"));
		
	}
	
	
	@Test
	@Transactional
	void getUserProfileFromBookTest() {
		
		//create book offer first 
		UserProfile UP = new UserProfile();
		UP.setUsername("Test");
		UP.setFullname("TEST");
		UP.setAddress("testGreece");
		
		UP.setBookOffers(new ArrayList<Book>());
		
				
		UPDAO.save(UP);
				
		Book book = new Book();
		book.setTitle("Title 1");
		book.setSummary("Info text here");
		book.setUserProfile(UP);
		
		bookDAO.save(book);
		
		
		
		UserProfile checkUserProfile = BSS.getUserProfileFromBook(book.getId());
		
		assertTrue(
				(checkUserProfile.getUsername().equals(UP.getUsername()))
				&& (checkUserProfile.getFullname().equals(UP.getFullname()))
				&& (checkUserProfile.getAddress().equals(UP.getAddress()))
				
				);
		
	}
	
	
	
	
	@Test
	@Transactional
	void declineUserProfileTest() {
		
		//create user
		UserProfile UP = new UserProfile();
		UP.setUsername("Test");
		UP.setBookOffers(new ArrayList<Book>());
		
		
		UPDAO.save(UP);
		
		
		BSS.createBookOffer(UP.getUsername(), "Title 1", "Ebooks", "Info here", "Author 1");
		
		UserProfile reqUser = new UserProfile();
		reqUser.setUsername("reqUser");
		reqUser.setRequestedBooks(new ArrayList<Book>());
		
		UPDAO.save(reqUser);
		int bookID = bookDAO.findByTitle("Title 1").get().getId();
		
		
		BSS.sendRequest(reqUser.getUsername(), bookID);
		
		BSS.declineUserProfile(bookID, reqUser.getId());
		
		Book checkBook = bookDAO.findById(bookID);
		
		assertTrue(!(checkBook.getRequestingUsers().contains(reqUser)));
		
		
	}
	
	
	
	@Test
	@Transactional
	void acceptedUserProfileTest() {
		
		User user = new User();
		user.setEmail("info@mail.gr");
		
		
		//create user
		UserProfile UP = new UserProfile();
		UP.setUsername("Test");
		UP.setBookOffers(new ArrayList<Book>());
		UP.setUser(user);
		
		UPDAO.save(UP);
		
		
		BSS.createBookOffer(UP.getUsername(), "Title 1", "Ebooks", "Info here", "Author 1");
		
		UserProfile reqUser = new UserProfile();
		reqUser.setUsername("reqUser");
		reqUser.setRequestedBooks(new ArrayList<Book>());
		
		
		Book book = bookDAO.findByTitle("Title 1").get();
		book.getAuthors().get(0).setWrittenBooks(new ArrayList<>());
		UPDAO.save(reqUser);
		
		
		book.setUserProfile(UP);
		bookDAO.save(book);
		
		
		
		
		BSS.sendRequest(reqUser.getUsername(), book.getId());
		
		BSS.acceptedUserProfile(book.getId(), reqUser.getId());
		
		UserProfile checkUserProfile = UPDAO.findById(reqUser.getId());
		
		Book checkBook = bookDAO.findById(book.getId());
		
		assertTrue(
				
				(checkUserProfile.getRequestedBooks().isEmpty())
				&& (checkBook == null)
				
				);
		
		
		
	}
	
	
	@Test
	@Transactional
	void getUserNotificationsTest() {
		
		
		//create profile
		UserProfile UP = new UserProfile();
		UP.setUsername("Test");
		
		//Create notification 
		Notification not = new Notification();
		not.setBookTitle("test");
		not.setDescription("test_description");
		
		List<Notification> notifications = List.of(not);
		UP.setNotifications(notifications);
				
		UPDAO.save(UP);
		
		List<Notification> checkNotifications = BSS.getUserNotifications(UP.getUsername());
		
		assertTrue(
				
				(checkNotifications.get(0).getBookTitle().equals(not.getBookTitle()))
				&& (checkNotifications.get(0).getDescription().equals(not.getDescription()))
				
				);
			
	}
	
	
	@Test
	@Transactional
	void searchBooksTests1() {
		
		UserProfile UP = new UserProfile();
		UP.setUsername("TEST");
		UPDAO.save(UP);
		
		Author author = new Author();
		author.setName("testAuthor");
		
		
		Book book = new Book();
		book.setTitle("TEST1");
		book.setSummary("TEST_SUMARRY1");
		book.setAuthors(List.of(author));
		bookDAO.save(book);
		
		
		Book book2 = new Book();
		book2.setTitle("TEST2");
		book2.setSummary("TEST_SUMARRY2");
		book2.setAuthors(List.of(author));
		bookDAO.save(book2);
		
		
		Book book3 = new Book();
		book3.setTitle("TEST3");
		book3.setSummary("TEST_SUMARRY3");
		book3.setAuthors(List.of(author));
		bookDAO.save(book3);
		
		
		List<Book> checkBooks = BSS.searchBook(UP.getUsername(), "TEST3", bookDAO.findAll(), "KEYWORDS");
		
		
		
		assertTrue(
				(checkBooks.size() == 1)
				&& (checkBooks.get(0).getTitle().equals(book3.getTitle()))
				&& (checkBooks.get(0).getSummary().equals(book3.getSummary()))
				);
		
	}
	
	
	
	@Test
	@Transactional
	void searchBooksTests2() {
		
		UserProfile UP = new UserProfile();
		UP.setUsername("TEST");
		UPDAO.save(UP);
		
		Author author = new Author();
		author.setName("testAuthor");
		
		
		Book book = new Book();
		book.setTitle("TEST1");
		book.setSummary("TEST_SUMARRY1");
		book.setAuthors(List.of(author));
		bookDAO.save(book);
		
		
		Book book2 = new Book();
		book2.setTitle("TEST2");
		book2.setSummary("TEST_SUMARRY2");
		book2.setAuthors(List.of(author));
		bookDAO.save(book2);
		
		
		Book book3 = new Book();
		book3.setTitle("TEST3");
		book3.setSummary("TEST_SUMARRY3");
		book3.setAuthors(List.of(author));
		bookDAO.save(book3);
		
		
		List<Book> checkBooks = BSS.searchBook(UP.getUsername(), "TEST2", bookDAO.findAll(), "KEYWORDS");
		
		
		
		assertTrue(
				(checkBooks.size() == 1)
				&& (checkBooks.get(0).getTitle().equals(book2.getTitle()))
				&& (checkBooks.get(0).getSummary().equals(book2.getSummary()))
				);
		
	}
	


	@Test
	@Transactional
	void searchBooksTests3() {
		
		UserProfile UP = new UserProfile();
		UP.setUsername("TEST");
		UPDAO.save(UP);
		
		Author author = new Author();
		author.setName("testAuthor");
		
		
		Book book = new Book();
		book.setTitle("TEST1");
		book.setSummary("TEST_SUMARRY1");
		book.setAuthors(List.of(author));
		bookDAO.save(book);
		
		
		Book book2 = new Book();
		book2.setTitle("TEST2");
		book2.setSummary("TEST_SUMARRY2");
		book2.setAuthors(List.of(author));
		bookDAO.save(book2);
		
		
		Book book3 = new Book();
		book3.setTitle("TEST3");
		book3.setSummary("TEST_SUMARRY3");
		book3.setAuthors(List.of(author));
		bookDAO.save(book3);
		
		
		List<Book> checkBooks = BSS.searchBook(UP.getUsername(), "TEST1", bookDAO.findAll(), "KEYWORDS");
		
		
		
		assertTrue(
				(checkBooks.size() == 1)
				&& (checkBooks.get(0).getTitle().equals(book.getTitle()))
				&& (checkBooks.get(0).getSummary().equals(book.getSummary()))
				);
		
	}
	
	
	


	@Test
	@Transactional
	void searchBooksTestsByFavouriteCategoriesTest() {
		
		UserProfile UP = new UserProfile();
		UP.setUsername("TEST");
		BookCategory BC = bookCategoryDAO.findByName("Biography").get();
		UP.setFavouriteBookCategories(List.of(BC));
		UPDAO.save(UP);
		
		Author author = new Author();
		author.setName("testAuthor");
		
		
		BookCategory BookCategory1 = bookCategoryDAO.findByName("Contemporary").get();
		Book book = new Book();
		book.setTitle("TEST1");
		book.setSummary("TEST_SUMARRY1");
		book.setCategory(BookCategory1);
		book.setAuthors(List.of(author));
		bookDAO.save(book);
		
		
		Book book2 = new Book();
		book2.setTitle("TEST2");
		book2.setSummary("TEST_SUMARRY2");
		book2.setCategory(BookCategory1);
		book2.setAuthors(List.of(author));
		bookDAO.save(book2);
		
		
		Book book3 = new Book();
		book3.setTitle("TEST3");
		book3.setSummary("TEST_SUMARRY3");
		book3.setCategory(BC);
		book3.setAuthors(List.of(author));
		bookDAO.save(book3);
		
		
		List<Book> checkBooks = BSS.searchBook(UP.getUsername(), "TEST3", bookDAO.findAll(), "CATEGORIES");
		
		
		
		assertTrue(
				(checkBooks.size() == 1)
				&& (checkBooks.get(0).getTitle().equals(book3.getTitle()))
				&& (checkBooks.get(0).getSummary().equals(book3.getSummary()))
				);
		
	}
	
	
	
	@Test
	@Transactional
	void getRecommendedBooksTest() {
		
		UserProfile UP = new UserProfile();
		UP.setUsername("TEST");
		
		BookCategory BC = bookCategoryDAO.findByName("Sports").get();
		List<Book> bookList = new ArrayList<Book>();
		
		UP.setFavouriteBookCategories(List.of(BC));
		
		
		
		Book book = new Book();
		book.setTitle("TEST1");
		book.setSummary("TEST_SUMARRY1");
		bookList.add(book);
		
		Book book2 = new Book();
		book2.setTitle("TEST2");
		book2.setSummary("TEST_SUMARRY2");
		bookList.add(book2);
		
		Book book3 = new Book();
		book3.setTitle("TEST3");
		book3.setSummary("TEST_SUMARRY3");
		bookList.add(book3);
		
		BC.setBookList(bookList);
		UPDAO.save(UP);
		
		List<Book> checkBooks = BSS.getRecommendedBooks(UP.getUsername(), "FAV_CATEGORY");
		
		assertTrue(
				(checkBooks.size()==3)
				&& (checkBooks.get(0).getTitle().equals(book.getTitle()))
				&& (checkBooks.get(0).getSummary().equals(book.getSummary()))
				&& (checkBooks.get(1).getTitle().equals(book2.getTitle()))
				&& (checkBooks.get(1).getSummary().equals(book2.getSummary()))
				&& (checkBooks.get(2).getTitle().equals(book3.getTitle()))
				&& (checkBooks.get(2).getSummary().equals(book3.getSummary()))
				
				);
		
	}
	

	@Test
	@Transactional
	void getRecommendedBooksFavTest() {
		
		UserProfile UP = new UserProfile();
		UP.setUsername("TEST");
		
		Author author = new Author();
		author.setName("test");
		
		List<Book> bookList = new ArrayList<Book>();
		
		UP.setFavouriteBookAuthors(List.of(author));
		
		
		
		Book book = new Book();
		book.setTitle("TEST1");
		book.setSummary("TEST_SUMARRY1");
		bookList.add(book);
		
		Book book2 = new Book();
		book2.setTitle("TEST2");
		book2.setSummary("TEST_SUMARRY2");
		bookList.add(book2);
		
		Book book3 = new Book();
		book3.setTitle("TEST3");
		book3.setSummary("TEST_SUMARRY3");
		bookList.add(book3);
		
		author.setWrittenBooks(bookList);
		UPDAO.save(UP);
		
		List<Book> checkBooks = BSS.getRecommendedBooks(UP.getUsername(), "FAV_AUTHOR");
		
		assertTrue(
				(checkBooks.size()==3)
				&& (checkBooks.get(0).getTitle().equals(book.getTitle()))
				&& (checkBooks.get(0).getSummary().equals(book.getSummary()))
				&& (checkBooks.get(1).getTitle().equals(book2.getTitle()))
				&& (checkBooks.get(1).getSummary().equals(book2.getSummary()))
				&& (checkBooks.get(2).getTitle().equals(book3.getTitle()))
				&& (checkBooks.get(2).getSummary().equals(book3.getSummary()))
				
				);
		
	}

	
	
	
	
	@Test
	@Transactional
	void getAllAuthorsTest() {
		
		
		
		Author author = new Author();
		author.setName("TEST");
		
		Book book = new Book();
		book.setTitle("title1");
		book.setSummary("info here");
		
		book.setAuthors(List.of(author));
		
		bookDAO.save(book);
		
		List<Author> checkAuthors = BSS.getAllAuthors();
		
		assertTrue(
				(checkAuthors.size()==1)
				&& (checkAuthors.get(0).getName().equals(author.getName()))
				);
		
		
	}
	
	
	
	@Test
	@Transactional
	void getBooksFromCategoryTest() {
		
		UserProfile UP = new UserProfile();
		UP.setUsername("TEST");
		BookCategory BC = bookCategoryDAO.findByName("Biography").get();
		UP.setFavouriteBookCategories(List.of(BC));
		UPDAO.save(UP);
		
		Author author = new Author();
		author.setName("testAuthor");
		
		
		BookCategory BookCategory1 = bookCategoryDAO.findByName("Contemporary").get();
		Book book = new Book();
		book.setTitle("TEST1");
		book.setSummary("TEST_SUMARRY1");
		book.setCategory(BookCategory1);
		book.setAuthors(List.of(author));
		bookDAO.save(book);
		
		
		Book book2 = new Book();
		book2.setTitle("TEST2");
		book2.setSummary("TEST_SUMARRY2");
		book2.setCategory(BookCategory1);
		book2.setAuthors(List.of(author));
		bookDAO.save(book2);
		
		
		Book book3 = new Book();
		book3.setTitle("TEST3");
		book3.setSummary("TEST_SUMARRY3");
		book3.setCategory(BC);
		BC.setBookList(List.of(book3));
		book3.setAuthors(List.of(author));
		bookDAO.save(book3);
		
		
		
		
		List<Book> checkBooks = BSS.getBooksFromCategory(BC.getName());
		
		
		//test
		
		assertTrue(
				(checkBooks.size() == 1)
				&& (checkBooks.get(0).getTitle().equals(book3.getTitle()))
				&& (checkBooks.get(0).getSummary().equals(book3.getSummary()))
				);
		
	}


	
	@Test
	@Transactional
	void getBooksFromAuthorTest() {
		
		UserProfile UP = new UserProfile();
		UP.setUsername("TEST");
		
		Author author = new Author();
		author.setName("testAuthor");
		List<Author> favAuthors = new ArrayList<Author>();
		favAuthors.add(author);
		UP.setFavouriteBookAuthors(favAuthors);
		UPDAO.save(UP);
		
		List<Book> writenBooks = new ArrayList<Book>();
		
		BookCategory BookCategory1 = bookCategoryDAO.findByName("Contemporary").get();
		Book book = new Book();
		book.setTitle("TEST1");
		book.setSummary("TEST_SUMARRY1");
		writenBooks.add(book);
		book.setCategory(BookCategory1);
		
		
		
		Book book2 = new Book();
		book2.setTitle("TEST2");
		book2.setSummary("TEST_SUMARRY2");
		writenBooks.add(book2);
		book2.setCategory(BookCategory1);
		
		
		
		Book book3 = new Book();
		book3.setTitle("TEST3");
		book3.setSummary("TEST_SUMARRY3");
		writenBooks.add(book3);
		
		author.setWrittenBooks(writenBooks);
		
		authorDAO.save(author);
		
		
		
		
		List<Book> checkBooks = BSS.getBooksFromAuthor(author.getName());
		
		
		
		//test
		
		assertTrue(
				(checkBooks.size() == 3)
				&& (checkBooks.get(0).getTitle().equals(book.getTitle()))
				&& (checkBooks.get(0).getSummary().equals(book.getSummary()))
				&& (checkBooks.get(1).getTitle().equals(book2.getTitle()))
				&& (checkBooks.get(1).getSummary().equals(book2.getSummary()))
				&& (checkBooks.get(2).getTitle().equals(book3.getTitle()))
				&& (checkBooks.get(2).getSummary().equals(book3.getSummary()))
				);
		
	}
	
	
	@Test
	@Transactional
	void deleteNotificationTest() {
		
		UserProfile UP = new UserProfile();
		UP.setUsername("TEST");
		
		Notification not = new Notification();
		not.setBookTitle("TEST");
		not.setDescription("INFO test");
		List<Notification> userNotifications = new ArrayList<Notification>();
		userNotifications.add(not);
		UP.setNotifications(userNotifications);
		UPDAO.save(UP);
		
		BSS.deleteNotification(UP.getUsername(), not.getId());
		
		UserProfile checkUserProfile = UPDAO.findById(UP.getId());
		
		
		assertTrue(
				checkUserProfile.getNotifications().isEmpty()
				);

		
	}
	
	@Test
	@Transactional
	void addCategoryToFavouriteCategoriesTest() {
		
		UserProfile UP = new UserProfile();
		UP.setUsername("TEST");
		UP.setFavouriteBookCategories(new ArrayList<BookCategory>());
		UPDAO.save(UP);
		
		
		BSS.addCategoryToFavouriteCategories(UP.getId(), "Suspense");
		
		
		UserProfile checkUserProfile = UPDAO.findById(UP.getId());
		
		assertTrue(checkUserProfile.getFavouriteBookCategories().get(0).getName().equals("Suspense"));
		
	}
	
	
	@Test
	@Transactional
	void deleteCategoryToFavouriteCategoriesTest() {
		
		UserProfile UP = new UserProfile();
		UP.setUsername("TEST");
		UP.setFavouriteBookCategories(new ArrayList<BookCategory>());
		UPDAO.save(UP);
		
		
		BSS.addCategoryToFavouriteCategories(UP.getId(), "Suspense");
		
		
		UserProfile userProfile = UPDAO.findById(UP.getId());
		
		BSS.deleteCategoryFromFavouriteCategories(UP.getId(), userProfile.getFavouriteBookCategories().get(0).getId());
		
		UserProfile checkUserProfile = UPDAO.findById(UP.getId());
		
		assertTrue(
				checkUserProfile.getFavouriteBookCategories().isEmpty()
				);
		
		
		
	}
	
	
	@Test
	@Transactional
	void deleteAuthorFromFavouriteAuthorsTest() {
		
		List<Author> favAuthors = new ArrayList<Author>();
		Author author = new Author();
		author.setName("TEST");
		favAuthors.add(author);
		
		UserProfile UP = new UserProfile();
		UP.setUsername("TEST");
		UP.setFavouriteBookAuthors(favAuthors);
		UPDAO.save(UP);
		
		
		BSS.deleteAuthorFromFavouriteAuthors(UP.getId(), author.getId());
		
		UserProfile checkUserProfile = UPDAO.findById(UP.getId());
		
		
		assertTrue(checkUserProfile.getFavouriteBookAuthors().isEmpty());
		
	}

	
	@Test
	@Transactional
	void deleteMyRequestTest() {
		
		//Owner
		UserProfile UP = new UserProfile();
		UP.setUsername("TEST");
		UP.setBookOffers(new ArrayList<Book>());
		UPDAO.save(UP);
		
		//reqUser
		UserProfile reqUser = new UserProfile();
		reqUser.setUsername("TEST_REQ");
		UPDAO.save(reqUser);
		
		
		BSS.createBookOffer(UP.getUsername(), "Title 1", "Ebooks", "Info here", "Author 1");
		
			
		int bookID = bookDAO.findByTitle("Title 1").get().getId();
		
		BSS.sendRequest(reqUser.getUsername(), bookID);
		
		BSS.deleteMyRequest(reqUser.getUsername(), bookID);
		
		assertTrue(reqUser.getRequestedBooks().isEmpty());
		
	}
	
	

}
