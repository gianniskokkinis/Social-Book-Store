package com.socialbookstore.springboot.daoTests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.socialbookstore.springboot.dao.AuthorDAO;
import com.socialbookstore.springboot.dao.BookDAO;
import com.socialbookstore.springboot.dao.NotificationDAO;
import com.socialbookstore.springboot.model.Author;
import com.socialbookstore.springboot.model.Book;
import com.socialbookstore.springboot.model.Notification;

import jakarta.transaction.Transactional;
@SpringBootTest
@TestPropertySource(
		  locations = "classpath:application.properties")
class NotificationDAOTests {

	@Autowired 
	NotificationDAO notificationDAO;
	
	
	@Test
	@Transactional
	void test() {
		
		Notification not = new Notification();
		not.setBookTitle("TEST");
		not.setDescription("TEST");
		not.setNotDate("DATE_TEST");
		notificationDAO.save(not);
		
		Notification checkNotification = notificationDAO.findById(not.getId());
		
		assertTrue(
				(checkNotification.getBookTitle().equals(not.getBookTitle()))
				&& (checkNotification.getDescription().equals(not.getDescription()))
				&& (checkNotification.getNotDate().equals(not.getNotDate()))
				);
		
		
		
	}

}
