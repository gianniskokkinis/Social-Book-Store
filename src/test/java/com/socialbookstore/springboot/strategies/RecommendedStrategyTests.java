package com.socialbookstore.springboot.strategies;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.socialbookstore.springboot.dao.BookCategoryDAO;
import com.socialbookstore.springboot.dao.BookDAO;
import com.socialbookstore.springboot.dao.UserProfileDAO;
import com.socialbookstore.springboot.model.Author;
import com.socialbookstore.springboot.model.Book;
import com.socialbookstore.springboot.model.BookCategory;
import com.socialbookstore.springboot.model.UserProfile;

import jakarta.transaction.Transactional;


@SpringBootTest
@TestPropertySource(
		  locations = "classpath:application.properties")
class RecommendedStrategyTests {

	@Autowired
	BookDAO bookDAO;
	
	@Autowired
	UserProfileDAO UPDAO;
	
	@Autowired
	BookCategoryDAO bookCategoryDAO;
	
	@Test
	@Transactional
	void RecommendedByFavAuthorTest() {
		
		UserProfile UP = new UserProfile();
		UP.setUsername("TEST");
		
		List<Author> favAuthors = new ArrayList<Author>();
		
		Author author = new Author();
		author.setName("Author");
		favAuthors.add(author);
		
		List<Book> writenBooks = new ArrayList<Book>();
		
		
		Book book = new Book();
		book.setTitle("book1");
		book.setSummary("info_test");
		writenBooks.add(book);
		
		
		Book book2 = new Book();
		book2.setTitle("book2");
		book2.setSummary("info_Test");

		author.setWrittenBooks(writenBooks);
		UP.setFavouriteBookAuthors(favAuthors);
		UPDAO.save(UP);
		
		
		//test
		RecommendedStrategy RS = new RecommendedByFavAuthor();
		
		List<Book> checkBooks = RS.getRecommended(UP);
		
		
		assertTrue(
				(checkBooks.size()==1)
				&& (checkBooks.get(0).getTitle().equals(book.getTitle()))
				&& (checkBooks.get(0).getSummary().equals(book.getSummary()))
				);
		
		
		
	}
	
	
	@Test
	@Transactional
	void RecommendedByFavCategoryTest() {
		
		UserProfile UP = new UserProfile();
		UP.setUsername("TEST");
		
		List<BookCategory> favBookCategories = new ArrayList<BookCategory>();
		BookCategory BC = bookCategoryDAO.findByName("Science").get();
		
		List<Book> bookList = new ArrayList<Book>();
		
		Book book = new Book();
		book.setTitle("book1");
		book.setSummary("info_test");
		bookList.add(book);
		bookDAO.save(book);
		
		Book book2 = new Book();
		book2.setTitle("book2");
		book2.setSummary("info_Test");
		bookDAO.save(book2);
		
		BC.setBookList(bookList);
		favBookCategories.add(BC);
		UP.setFavouriteBookCategories(favBookCategories);
		UPDAO.save(UP);
		
		
		
		
		//test
		RecommendedStrategy RS = new RecommendedByFavCategory();
		
		List<Book> checkBooks = RS.getRecommended(UP);
		
		assertTrue(
				(checkBooks.size()==1) 
				&& (checkBooks.get(0).getTitle().equals(book.getTitle()))
				&& (checkBooks.get(0).getSummary().equals(book.getSummary()))
				
				);
		
	}

}
