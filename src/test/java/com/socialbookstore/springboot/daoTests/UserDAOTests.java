package com.socialbookstore.springboot.daoTests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.socialbookstore.springboot.dao.AuthorDAO;
import com.socialbookstore.springboot.dao.BookDAO;
import com.socialbookstore.springboot.dao.NotificationDAO;
import com.socialbookstore.springboot.dao.UserDAO;
import com.socialbookstore.springboot.model.Author;
import com.socialbookstore.springboot.model.Book;
import com.socialbookstore.springboot.model.Notification;
import com.socialbookstore.springboot.model.User;

import jakarta.transaction.Transactional;
@SpringBootTest
@TestPropertySource(
		  locations = "classpath:application.properties")
class UserDAOTests {

	@Autowired 
	UserDAO userDAO;
	
	
	@Test
	@Transactional
	void test() {
		
		User user = new User();
		user.setUsername("TEST");
		user.setEmail("mail@test.gr");
		userDAO.save(user);
		
		Optional<User> checkUser = userDAO.findById(user.getId());
		
		assertTrue(
				(checkUser.isPresent())
				&& (checkUser.get().getUsername().equals(user.getUsername()))
				&& (checkUser.get().getEmail().equals(user.getEmail()))
				);
		
		
		
		
	}

}
