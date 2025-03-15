package com.socialbookstore.springboot.controllerTests;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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

import com.socialbookstore.springboot.controller.AuthController;
import com.socialbookstore.springboot.service.UserService;

import jakarta.transaction.Transactional;


@SpringBootTest
@TestPropertySource(
		  locations = "classpath:application.properties")
@AutoConfigureMockMvc
class AuthControllerTests {
	
	
	@Autowired
    private WebApplicationContext context;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private AuthController authController;
	
	@MockBean
	private UserService userService;
	
	
	@BeforeEach
    public void setup() {
		mockMvc = MockMvcBuilders
          .webAppContextSetup(context)
          .build();
		
	}
	
	@Test
	void testEmployeeControllerIsNotNull() {
		Assertions.assertNotNull(authController);
	}
	
	@Test
	void testMockMvcIsNotNull() {
		Assertions.assertNotNull(mockMvc);
	}
		

	
	@WithMockUser(value="User")
	@Transactional
	@Test
	void loginTest() throws Exception{

		mockMvc.perform(get("/login")).
		andExpect(status().isOk()).
		andExpect(view().name("auth/signin"));	
		
	}
	
	@WithMockUser(value="User")
	@Transactional
	@Test
	void registerTest() throws Exception{

		mockMvc.perform(get("/register")).
		andExpect(status().isOk()).
		andExpect(view().name("auth/signup"));	
		
	}
	
	
	@WithMockUser(value="User")
	@Transactional
	@Test
	void saveTest() throws Exception{

		mockMvc.perform(get("/save")).
		andExpect(status().isOk()).
		andExpect(view().name("auth/signin"));	
		
	}
	

}
