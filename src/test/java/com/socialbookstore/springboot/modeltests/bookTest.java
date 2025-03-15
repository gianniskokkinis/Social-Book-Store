package com.socialbookstore.springboot.modeltests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.socialbookstore.springboot.model.Author;
import com.socialbookstore.springboot.model.Book;
import com.socialbookstore.springboot.model.BookCategory;

@SpringBootTest
@TestPropertySource(
		  locations = "classpath:application.properties")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class bookTest {

	@Test
	
	void SetTitletest() {
		Book book = new Book();
		book.setTitle("Title");
		book.setSummary("info here");
		assertTrue(book.getTitle().equals("Title"));
	}
	
	@Test
	void SetSummarytest() {
		Book book = new Book();
		book.setTitle("Title");
		book.setSummary("info here");
		assertTrue(book.getSummary().equals("info here"));
	}
	
	@Test
	void TestCategorytest() {
		Book book = new Book();
		book.setTitle("Title");
		book.setSummary("info here");
		
		BookCategory BC = new BookCategory();
		BC.setName("Category");
		book.setCategory(BC);
		
		assertTrue(book.getCategory().getName().equals("Category"));
	}
	
	@Test
	void SetAuthortest() {
		Book book = new Book();
		book.setTitle("Title");
		book.setSummary("info here");
		
		Author author = new Author();
		author.setName("Author 1");
		book.setAuthors(List.of(author));
	
		assertTrue(book.getAuthors().get(0).getName().equals(author.getName()));
		
	}




}
