package com.socialbookstore.springboot.daoTests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

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
class AuthorDAOTests {

	@Autowired 
	AuthorDAO authorDAO;
	
	
	@Test
	@Transactional
	void test() {
		
		Author author = new Author();
		author.setName("TEST");
		author.setWrittenBooks(new ArrayList<Book>());
		authorDAO.save(author);
		
		Author checkAuthor = authorDAO.findById(author.getId());
		
		assertTrue(
				(checkAuthor != null)
				&& (checkAuthor.getName().equals(author.getName()))
				&& (checkAuthor.getWrittenBooks().isEmpty())
				);
	}

}
