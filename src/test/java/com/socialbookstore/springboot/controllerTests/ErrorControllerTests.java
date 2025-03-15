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
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import jakarta.transaction.Transactional;


@SpringBootTest
@TestPropertySource(
		  locations = "classpath:application.properties")
@AutoConfigureMockMvc
class ErrorControllerTests {
	
	@Autowired
    private WebApplicationContext context;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ErrorController errorController;

	@BeforeEach
    public void setup() {
		mockMvc = MockMvcBuilders
          .webAppContextSetup(context)
          .build();
	}
	
	
	
	@Test
	void testEmployeeControllerIsNotNull() {
		Assertions.assertNotNull(errorController);
	}
	
	
	
	@Test
	void testMockMvcIsNotNull() {
		Assertions.assertNotNull(mockMvc);
	}
	
	@WithMockUser(value="User")
	@Transactional
	@Test
	void errorPageTest() throws Exception{
		
		mockMvc.perform(get("/error")).
		andExpect(status().isOk()).
		andExpect(view().name("error/errorpage"));
		
	}
	

}
