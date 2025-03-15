package com.socialbookstore.springboot.daoTests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.socialbookstore.springboot.dao.AuthorDAO;
import com.socialbookstore.springboot.dao.BookDAO;
import com.socialbookstore.springboot.model.Author;
import com.socialbookstore.springboot.model.Book;

import jakarta.transaction.Transactional;
@SpringBootTest
@TestPropertySource(
		  locations = "classpath:application.properties")
class BookDAOTests {

	@Autowired 
	BookDAO bookDAO;
	
	
	@Test
	@Transactional
	void test() {
		
		Book book = new Book();
		book.setTitle("TITLE");
		book.setSummary("INFO HERE");
		
		bookDAO.save(book);
		
		Book checkBook = bookDAO.findById(book.getId());
		
		assertTrue(
				(checkBook.getTitle().equals(book.getTitle()))
				&& (checkBook.getSummary().equals(book.getSummary()))	
				);
		
		
	}

}
