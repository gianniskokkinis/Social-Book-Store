package com.socialbookstore.springboot.modeltests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.socialbookstore.springboot.dao.AuthorDAO;
import com.socialbookstore.springboot.model.Author;
import com.socialbookstore.springboot.model.Book;

import jakarta.transaction.Transactional;

@SpringBootTest
@TestPropertySource(
		  locations = "classpath:application.properties")
class AuthorTests {
	
	@Autowired
	AuthorDAO authorDAO;

	@Test
	void Authortest() {
		Author author = new Author();
		author.setName("TEST");
		
		assertTrue(author.getName().equals("TEST"));
		
	}
	
	
	@Test
	@Transactional
	void testSaveToDB() {
		
		Author author = new Author();
		author.setName("TEST");
		
		Book book = new Book();
		book.setTitle("book1");
		book.setSummary("info_test");
		
		Book book2 = new Book();
		book2.setTitle("book2");
		book2.setSummary("info_Test");
		
		
		author.setWrittenBooks(List.of(book,book2));
		
		authorDAO.save(author);
		
		Author checkAuthor = authorDAO.findById(author.getId());
		
		assertTrue(
				(checkAuthor.getName().equals(author.getName()))
				&& (checkAuthor.getWrittenBooks().size() == 2)
				&& (checkAuthor.getWrittenBooks().get(0).getTitle().equals(book.getTitle()))
				&& (checkAuthor.getWrittenBooks().get(0).getSummary().equals(book.getSummary()))
				&& (checkAuthor.getWrittenBooks().get(1).getTitle().equals(book2.getTitle()))
				&& (checkAuthor.getWrittenBooks().get(1).getSummary().equals(book2.getSummary()))
				
				);
		
		
		
	}

}
