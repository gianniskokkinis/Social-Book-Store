package com.socialbookstore.springboot;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.socialbookstore.springboot.dao.AuthorDAO;
import com.socialbookstore.springboot.dao.BookDAO;
import com.socialbookstore.springboot.dao.UserDAO;
import com.socialbookstore.springboot.dao.UserProfileDAO;
import com.socialbookstore.springboot.model.Author;
import com.socialbookstore.springboot.model.Book;
import com.socialbookstore.springboot.model.BookCategory;
import com.socialbookstore.springboot.model.User;
import com.socialbookstore.springboot.model.UserProfile;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale.Category;
import java.util.Optional;



@SpringBootTest
@TestPropertySource(
		  locations = "classpath:application.properties")
class SocialBookstoreApplicationTests {
	
	
	
	@Test
	void Allest() {
		
	}
	
	
	
}
