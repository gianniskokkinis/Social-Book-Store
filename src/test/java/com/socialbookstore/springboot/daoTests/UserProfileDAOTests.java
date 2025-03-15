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
import com.socialbookstore.springboot.dao.UserProfileDAO;
import com.socialbookstore.springboot.model.Author;
import com.socialbookstore.springboot.model.Book;
import com.socialbookstore.springboot.model.Notification;
import com.socialbookstore.springboot.model.User;
import com.socialbookstore.springboot.model.UserProfile;

import jakarta.transaction.Transactional;
@SpringBootTest
@TestPropertySource(
		  locations = "classpath:application.properties")
class UserProfileDAOTests {

	@Autowired 
	UserProfileDAO userProfileDAO;
	
	
	@Test
	@Transactional
	void test() {
		
		UserProfile UP = new UserProfile();
		UP.setUsername("TEST");
		UP.setFullname("TEST");
		UP.setAddress("test");
		UP.setAge(12);
		UP.setState("TEST");
		UP.setPostcode("TEST");
		userProfileDAO.save(UP);
		
		UserProfile checkUserProfile = userProfileDAO.findById(UP.getId());
		
		
		assertTrue(
				(checkUserProfile.getUsername().equals(UP.getUsername()))
				&& (checkUserProfile.getFullname().equals(UP.getFullname()))
				&& (checkUserProfile.getAddress().equals(UP.getAddress()))
				&& (checkUserProfile.getAge()==UP.getAge())
				&& (checkUserProfile.getState().equals(UP.getState()))
				&& (checkUserProfile.getPostcode().equals(UP.getPostcode()))
				);
		
		
		
		
		
	}

}
