package com.socialbookstore.springboot.modeltests;

import static org.junit.jupiter.api.Assertions.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.socialbookstore.springboot.dao.NotificationDAO;
import com.socialbookstore.springboot.dao.UserProfileDAO;
import com.socialbookstore.springboot.model.Notification;
import com.socialbookstore.springboot.model.UserProfile;

import jakarta.transaction.Transactional;


@SpringBootTest
@TestPropertySource(
		  locations = "classpath:application.properties")
class NotificationsTests {

	@Autowired
	NotificationDAO notificationDAO;
	
	
	@Autowired
	UserProfileDAO userProfileDAO;

	@Test
	@Transactional
	void NotificationTest() {
		
		
		
		
		Notification notification = new Notification();
		notification.setDescription("test");
		
		
		
		notificationDAO.save(notification);
		
		Notification checkNotification = notificationDAO.findById(notification.getId());
		notificationDAO.delete(notification);
		
		assertTrue(checkNotification.getDescription().equals(notification.getDescription()));

		
	}
	
	
	
	
	@Test
	@Transactional
	void LinkNotificationUserProfileTest() {
		
		UserProfile UP = new UserProfile();
		
		UP.setUsername("TestUser1");
		
		
	
		
		Notification notification = new Notification();
		notification.setDescription("test");
		
		
		
		List<Notification> mynotifications = List.of(notification);
		
		UP.setNotifications(mynotifications);
		
		
		
		userProfileDAO.save(UP);
		
		
		UserProfile checkUserProfile = userProfileDAO.findByUsername(UP.getUsername()).get();
		userProfileDAO.delete(UP);
		
		assertTrue(checkUserProfile.getNotifications().get(0).getDescription().equals(notification.getDescription()));
		
		
		
	}
	
	@Test
	@Transactional
	void setDateTest() {
		
		//is just for the date
		String strDate = DateFormat.getInstance().format(new Date());
		String strDate2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
		
		
		System.out.println(strDate);
		System.out.println(strDate2);
		
		
	}
	
	
	@Test
	void notificationGetTest() {
		
		Notification not = new Notification();
		not.setBookTitle("Test1");
		not.setDescription("Info");
		
		
		assertTrue(
				(not.getBookTitle().equals("Test1"))
				&& (not.getDescription().equals("Info"))
				);
	}
	
	
	
	

}
