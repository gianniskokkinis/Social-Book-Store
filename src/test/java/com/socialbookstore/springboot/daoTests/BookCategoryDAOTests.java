package com.socialbookstore.springboot.daoTests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.socialbookstore.springboot.dao.AuthorDAO;
import com.socialbookstore.springboot.dao.BookCategoryDAO;
import com.socialbookstore.springboot.dao.BookDAO;
import com.socialbookstore.springboot.model.Author;
import com.socialbookstore.springboot.model.Book;
import com.socialbookstore.springboot.model.BookCategory;

import jakarta.transaction.Transactional;
@SpringBootTest
@TestPropertySource(
		  locations = "classpath:application.properties")
class BookCategoryDAOTests {



	@Autowired 
	BookCategoryDAO bookCategoryDAO;
	
	
	@Test
	@Transactional
	void test() {
		
		Optional<BookCategory> checkBookCategory = bookCategoryDAO.findByName("Science");
		
		assertTrue(checkBookCategory.isPresent());
		
	}

	

}
