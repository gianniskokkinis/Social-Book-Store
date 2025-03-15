package com.socialbookstore.springboot.modeltests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.socialbookstore.springboot.dao.UserDAO;
import com.socialbookstore.springboot.model.User;

import jakarta.transaction.Transactional;


@SpringBootTest
@TestPropertySource(
		  locations = "classpath:application.properties")
class userTests {
	
	
	@Autowired
	UserDAO userDAO;

	@Test
	@Transactional
	void SaveTest() {
		
		
		User user = new User();
		
		user.setUsername("user1");
		user.setPassword("password1");
		
		userDAO.save(user);
		
		Optional<User> checkUser = userDAO.findById(user.getId());
		assertTrue(checkUser.isPresent());
		
		userDAO.delete(user);
		
		
		
		
	}
	
	@Test
	void TestUser(){
		
		User user = new User();
		user.setUsername("test");
		user.setEmail("email@papi.gr");
		
		assertTrue(
				user.getUsername().equals("test")
				&& (user.getEmail().equals("email@papi.gr"))
				);
			
	}
	
	
	
	

}
