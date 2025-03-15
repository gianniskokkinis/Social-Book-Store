package com.socialbookstore.springboot.controllerTests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.socialbookstore.springboot.controller.UserController;
import com.socialbookstore.springboot.dao.BookDAO;
import com.socialbookstore.springboot.dao.UserDAO;
import com.socialbookstore.springboot.dao.UserProfileDAO;
import com.socialbookstore.springboot.model.Book;
import com.socialbookstore.springboot.model.BookCategory;
import com.socialbookstore.springboot.model.User;
import com.socialbookstore.springboot.model.UserProfile;
import com.socialbookstore.springboot.service.UserService;

import jakarta.transaction.Transactional;

@SpringBootTest
@TestPropertySource(
		  locations = "classpath:application.properties")
@AutoConfigureMockMvc
class UserControllerTests {

	@Autowired
    private WebApplicationContext context;
	
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private UserController userController;
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private UserProfileDAO userProfileDAO;
	
	@Autowired
	private BookDAO bookDAO;
	
	@MockBean
	private UserService userService;
	
	
	
	
	

	@BeforeEach
    public void setup() {
		mockMvc = MockMvcBuilders
          .webAppContextSetup(context)
          .build();
		
		
		//here we are making fake objects for mock testts
		UserProfile mockUserProfile = mock(UserProfile.class);
		List<UserProfile> requestingUsers = new ArrayList<UserProfile>();
		
		
		when(userService.findUserProfile(anyString())).thenReturn(mockUserProfile);
		when(userService.getRequestingUsers(anyInt())).thenReturn(requestingUsers);
	
		
    }
	
	@Test
	void testEmployeeControllerIsNotNull() {
		Assertions.assertNotNull(userController);
	}
	
	@Test
	void testMockMvcIsNotNull() {
		Assertions.assertNotNull(mockMvc);
	}
	
	@WithMockUser(value="User")
	@Transactional
	@Test
	void UserDashboardTest() throws Exception {
		
		
		mockMvc.perform(get("/user/dashboard")).
		andExpect(status().isOk()).
		andExpect(view().name("user/editprofile"));			

	}
	
	
	
	
	
	@WithMockUser(value="user")
	@Transactional
	@Test
	void editProfileTest() throws Exception {
		
		mockMvc.perform(get("/user/editprofile")).
		andExpect(status().isOk()).
		andExpect(view().name("user/editprofile"));
		
	}

	
	@WithMockUser(value="user")
	@Transactional
	@Test
	void saveProfileTest() throws Exception{
		
		mockMvc.perform(get("/user/saveprofile")).
		andExpect(status().isOk()).
		andExpect(view().name("/user/dashboard"));
	
	}
	
	
	@WithMockUser(value="user")
	@Transactional
	@Test
	void sendRequestsTest() throws Exception{
		
		mockMvc.perform(get("/user/send-requests")).
		andExpect(status().isOk()).
		andExpect(view().name("user/requests-list"));
	
	}
	
	
	
	@WithMockUser(value="user")
	@Transactional
	@Test
	void viewMyBookoffersTests() throws Exception{
		
		mockMvc.perform(get("/user/view-my-bookoffers")).
		andExpect(status().isOk()).
		andExpect(view().name("store/my-bookoffer-list"));
	
	}
	
	
	
	@WithMockUser(value="user")
	@Transactional
	@Test
	void viewMyReceiveRequestsTest() throws Exception{
		
		
		mockMvc.perform(get("/user/view-receive-requests").param("bookID", "1")).
		andExpect(status().isOk()).
		andExpect(view().name("store/receive-requests-list"));
	
	}
	
	
	@WithMockUser(value="user")
	@Transactional
	@Test
	void saveCategoryToFavCategoriesTest() throws Exception{
		
		UserProfile UP = new UserProfile();
		UP.setUsername("TEST");
		UP.setFavouriteBookCategories(new ArrayList<BookCategory>());
		userProfileDAO.save(UP);
		
		
		mockMvc.perform(get("/user/save-category-to-favourite-categories").param("userProfileID", ""+UP.getId()).param("category", "Romance")).
		andExpect(status().is3xxRedirection()).
		andExpect(view().name("redirect:/user/editprofile"));
	
	}
	
	
	@WithMockUser(value="user")
	@Transactional
	@Test
	void deleteCategoryToFavCategoriesTest() throws Exception{
		
		UserProfile UP = new UserProfile();
		UP.setUsername("TEST");
		UP.setFavouriteBookCategories(new ArrayList<BookCategory>());
		userProfileDAO.save(UP);
		
		
		mockMvc.perform(get("/user/delete-category-from-favourite-categories").param("userProfileID", ""+UP.getId()).param("categoryID", "1")).
		andExpect(status().is3xxRedirection()).
		andExpect(view().name("redirect:/user/editprofile"));
	
	}	
	
	
	@WithMockUser(value="user")
	@Transactional
	@Test
	void deleteMyRequestTest() throws Exception{
		
		
		UserProfile UP = new UserProfile();
		UP.setUsername("user");
		userProfileDAO.save(UP);
		UP.setRequestedBooks(new ArrayList<Book>());
		
		Book book = new Book();
		book.setTitle("TEST");
		book.setSummary("INFO_TEST");
		book.setRequestingUsers(new ArrayList<UserProfile>());
		bookDAO.save(book);
		
		
		mockMvc.perform(get("/store/delete/my-request").param("bookID", ""+book.getId())).
		andExpect(status().is3xxRedirection()).
		andExpect(view().name("redirect:/user/send-requests"));
		
	}
	
	
	
	
	

}
