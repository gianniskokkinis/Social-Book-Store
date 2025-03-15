package com.socialbookstore.springboot.controllerTests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.socialbookstore.springboot.controller.StoreController;
import com.socialbookstore.springboot.dao.BookDAO;
import com.socialbookstore.springboot.dao.UserProfileDAO;
import com.socialbookstore.springboot.model.Author;
import com.socialbookstore.springboot.model.Book;
import com.socialbookstore.springboot.model.BookCategory;
import com.socialbookstore.springboot.model.User;
import com.socialbookstore.springboot.model.UserProfile;
import com.socialbookstore.springboot.service.BookStoreService;
import com.socialbookstore.springboot.service.UserService;

import jakarta.transaction.Transactional;


@SpringBootTest
@TestPropertySource(
		  locations = "classpath:application.properties")
@AutoConfigureMockMvc
class StoreControllerTests {
	
	
	@Autowired
    private WebApplicationContext context;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private StoreController storeController;
	
	@Autowired
	private BookDAO bookDAO;
	
	@Autowired
	private UserProfileDAO UPDAO;
	
	@MockBean
	private UserService userService;
	
	@MockBean
	private BookStoreService storeService;
	



	@BeforeEach
    public void setup() {
		mockMvc = MockMvcBuilders
          .webAppContextSetup(context)
          .build();
		
		
		//here we are making fake objects for mock testts
		UserProfile mockUserProfile = mock(UserProfile.class);
		mockUserProfile.setBookOffers(new ArrayList<Book>());
		List<UserProfile> requestingUsers = new ArrayList<UserProfile>();
		
		User user = new User();
		user.setEmail("info");
		
		
		UserProfile testUser = new UserProfile();
		testUser.setFullname("test");
		testUser.setAddress("test");
		testUser.setPhoneNumber("test");
		testUser.setUser(user);
		testUser.setBookOffers(new ArrayList<Book>());
		
		
		when(userService.findUserProfile(anyString())).thenReturn(mockUserProfile);
		when(userService.getRequestingUsers(anyInt())).thenReturn(requestingUsers);
		when(storeService.getAllBooks()).thenReturn( new ArrayList<Book>());
		when(storeService.getAllAuthors()).thenReturn(new ArrayList<Author>());
		when(storeService.getAllBookCategories()).thenReturn( new ArrayList<BookCategory>());
		when(storeService.getUserProfileFromBook(anyInt())).thenReturn(mockUserProfile);
		when(mockUserProfile.getBookOffers()).thenReturn(new ArrayList<Book>());
		when(storeService.acceptedUserProfile(anyInt(), anyInt())).thenReturn(testUser);
		when(storeService.getUserProfileFromBook(anyInt())).thenReturn(testUser);
    }	
	
	@Test
	void testEmployeeControllerIsNotNull() {
		Assertions.assertNotNull(storeController);
	}
	
	@Test
	void testMockMvcIsNotNull() {
		Assertions.assertNotNull(mockMvc);
	}
		

	@WithMockUser(value="User")
	@Transactional
	@Test
	void storeTest() throws Exception{
		
		mockMvc.perform(get("/store")).
		andExpect(status().isOk()).
		andExpect(view().name("store/browse-books"));				
		
	}
	
	@WithMockUser(value="User")
	@Transactional
	@Test
	void storeSuccessTest() throws Exception{
		
		
		
		mockMvc.perform(
				get("/store/offersuccess")
				.param("title", "")
				.param("category", "Romance")
				.param("summary","")
				.param("authors", "Author 1")
				).
		andExpect(status().isOk()).
		andExpect(view().name("store/bookoffersucess"));				
		
	}
	
	@WithMockUser(value="User")
	@Transactional
	@Test
	void createBookOfferTest() throws Exception{
		
		mockMvc.perform(get("/store/createbookoffer")).
		andExpect(status().isOk()).
		andExpect(view().name("store/createbookoffer"));			
		
	}
	
	
	@WithMockUser(value="User")
	@Transactional
	@Test
	void deleteBookOfferTest() throws Exception{
		
		UserProfile UP = new UserProfile();
		UP.setUsername("User");
		UP.setRequestedBooks(new ArrayList<Book>());
		
		
		Book book = new Book();
		book.setTitle("Title");
		book.setSummary("Info Test Here");
		book.setRequestingUsers(new ArrayList<UserProfile>());
		UP.setBookOffers(List.of(book));
		UPDAO.save(UP);
		bookDAO.save(book);
		
		
		mockMvc.perform(
				get("/store/delete-bookoffer")
				.param("bookID", ""+book.getId())
				).
		andExpect(status().isOk()).
		andExpect(view().name("/user/dashboard"));			
		
	}
	
	
	@WithMockUser(value="User")
	@Transactional
	@Test
	void requestTest() throws Exception {
	
		Book book = new Book();
		book.setTitle("Title");
		book.setSummary("Info Test Here");
		bookDAO.save(book);
		
		mockMvc.perform(
				get("/store/request")
				.param("bookID", ""+book.getId())
				).
		andExpect(status().isOk()).
		andExpect(view().name("store/RequestSuccess"));	
		
	}
	
	@WithMockUser(value="User")
	@Transactional
	@Test
	void viewProfileTest() throws Exception{
		
		User user = new User();
		user.setEmail("test");
		
		UserProfile UP = new UserProfile();
		UP.setUsername("User");
		UP.setFullname("TEST INFO");
		UP.setAddress("Address Test here");
		UP.setAddress("12");
		UP.setPhoneNumber("23123");
		UP.setPostcode("POSTCODE TEST");
		UP.setState("STATE");
		UP.setUser(user);
		UP.setBookOffers(new ArrayList<Book>());
		
		
		
		Book book = new Book();
		book.setTitle("Title");
		book.setSummary("Info Test Here");
		book.setUserProfile(UP);
		UPDAO.save(UP);
		bookDAO.save(book);
		
		
		mockMvc.perform(
				get("/store/viewprofile")
				.param("bookID", ""+book.getId())
				).
		andExpect(status().isOk()).
		andExpect(view().name("store/view-profile"));	
		
		
		
	}
	
	@WithMockUser(value="User")
	@Transactional
	@Test
	void declineUserTest() throws Exception{

		UserProfile UP = new UserProfile();
		UP.setUsername("User");
		UPDAO.save(UP);
		
		
		Book book = new Book();
		book.setTitle("Title");
		book.setRequestingUsers(new ArrayList<UserProfile>());
		book.setSummary("Info Test Here");
		bookDAO.save(book);
		
		
		
		mockMvc.perform(
				get("/store/decline-user")
				.param("bookID", ""+book.getId())
				.param("userProfileID", ""+UP.getId())
				).
		andExpect(status().is3xxRedirection()).
		andExpect(view().name("redirect:/store"));

		
	}
	
	
	@WithMockUser(value="User")
	@Transactional
	@Test
	void acceptUserTest() throws Exception{
		
		//user1
		UserProfile Owner = new UserProfile();
		Owner.setUsername("User");
		List<Book> bookOffers = new ArrayList<Book>();
		
		//user2
		UserProfile reqUser = new UserProfile();
		reqUser.setUsername("test");
		List<Book> requestedBooks = new ArrayList<Book>();
		
		
		
		
		Book book = new Book();
		book.setTitle("TEST");
		book.setSummary("TEST");
		List<UserProfile> requestingUsers = new ArrayList<UserProfile>();
		
		bookOffers.add(book);
		requestedBooks.add(book);
		requestingUsers.add(reqUser);
		
		UPDAO.save(Owner);
		UPDAO.save(reqUser);
		
		
		
		
		
		
		
		
		
		
		mockMvc.perform(
				get("/store/accept-user")
				.param("bookID", ""+book.getId())
				.param("userProfileID", ""+reqUser.getId())
				).
		andExpect(status().isOk()).
		andExpect(view().name("store/view-profile"));

		
	}
	
	
	@WithMockUser(value="User")
	@Transactional
	@Test
	void searchBookTest() throws Exception{
		
		mockMvc.perform(
				get("/store/search-book")
				.param("search-input", "")
				).
		andExpect(status().isOk()).
		andExpect(view().name("store/browse-books"));
		
	}
	
	
	@WithMockUser(value="User")
	@Transactional
	@Test
	void searchBookByFavCategoriesTest() throws Exception{
		
		mockMvc.perform(
				get("/store/search-book-by-fav-categories")
				.param("search-input", "")
				).
		andExpect(status().isOk()).
		andExpect(view().name("store/browse-books"));
		
	}
	
	
	@WithMockUser(value="User")
	@Transactional
	@Test
	void searchBookByFavAuthorsTest() throws Exception{
		
		mockMvc.perform(
				get("/store/search-book-by-fav-authors")
				.param("search-input", "")
				).
		andExpect(status().isOk()).
		andExpect(view().name("store/browse-books"));
		
	}
	
	@WithMockUser(value="User")
	@Transactional
	@Test
	void recommendedBooksByFavAuthorsTest() throws Exception {
		
		mockMvc.perform(
				get("/store/recommened-book-by-fav-authors")
				).
		andExpect(status().isOk()).
		andExpect(view().name("store/browse-books"));
		
		
	}
	
	
	@WithMockUser(value="User")
	@Transactional
	@Test
	void recommendedBooksByFavCategoriesTest() throws Exception {
		
		mockMvc.perform(
				get("/store/recommened-book-by-fav-categories")
				).
		andExpect(status().isOk()).
		andExpect(view().name("store/browse-books"));
		
		
	}
	
	
	@WithMockUser(value="User")
	@Transactional
	@Test
	void viewMyNotificationsTest() throws Exception {
		
		mockMvc.perform(
				get("/store/view-my-notifications")
				).
		andExpect(status().isOk()).
		andExpect(view().name("store/notifications-list"));
		
		
	}
	
	
	@WithMockUser(value="User")
	@Transactional
	@Test
	void selectRecommendedTest() throws Exception {
		
		mockMvc.perform(
				get("/store/select-recommended")
				).
		andExpect(status().isOk()).
		andExpect(view().name("store/select-recommended"));
		
		
	}
	
	
	@WithMockUser(value="User")
	@Transactional
	@Test
	void getBooksFromCategoryNameTest() throws Exception {
		
		mockMvc.perform(
				get("/store/get-books-from-category-name")
				.param("categoryname", "Romance")
				).
		andExpect(status().isOk()).
		andExpect(view().name("store/browse-books"));
		
		
	}
	
	
	@WithMockUser(value="User")
	@Transactional
	@Test
	void getBooksFromAuthorNameTest() throws Exception {
		
		Author author = new Author();
		author.setName("TEST");
		author.setWrittenBooks(new ArrayList<Book>());
		
		mockMvc.perform(
				get("/store/get-books-from-author-name")
				.param("authorname", author.getName())
				).
		andExpect(status().isOk()).
		andExpect(view().name("store/browse-books"));
		
		
	}
	
	@WithMockUser(value="User")
	@Transactional
	@Test
	void deleteNotificationTest() throws Exception {
		
		
		
		mockMvc.perform(
				get("/store/delete-notification")
				.param("notificationID", "1")
				).
		andExpect(status().is3xxRedirection()).
		andExpect(view().name("redirect:/store/view-my-notifications"));
		
		
	}
	
	

}
