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
class SearchingStrategyTests {

	
	@Autowired
	UserProfileDAO UPDAO;
	
	@Autowired
	BookDAO bookDAO;
	
	@Autowired
	BookCategoryDAO bookCategoryDAO;
	
	
	@Test
	@Transactional
	void SimpleSeachKeywordsTest() {
		
		Book book = new Book();
		book.setTitle("book1");
		book.setSummary("info_test");
		bookDAO.save(book);
		
		Book book2 = new Book();
		book2.setTitle("book2");
		book2.setSummary("info_Test");
		bookDAO.save(book2);
		
		List<Book> storeBooks = bookDAO.findAll();
		
		SearchingStrategy SS = new SearchingByKeywords();
		
		List<Book> checkBooks = SS.search(null, "book", storeBooks);
		
		
		
		assertTrue(
				(checkBooks.size()==2)
				&& (checkBooks.get(0).getTitle().equals(book.getTitle()))
				&& (checkBooks.get(0).getSummary().equals(book.getSummary()))
				&& (checkBooks.get(1).getTitle().equals(book2.getTitle()))
				&& (checkBooks.get(1).getSummary().equals(book2.getSummary()))
				);
		
		
		
	}
	
	
	@Test
	@Transactional
	void SeachByFavCategoriesTest() {
		
		UserProfile UP = new UserProfile();
		UP.setUsername("TEST");
		
		BookCategory BC = bookCategoryDAO.findByName("Sports").get();
		List<Book> bookList = new ArrayList<Book>();
		
		UP.setFavouriteBookCategories(List.of(BC));
		
		
		Book book = new Book();
		book.setTitle("book1");
		book.setSummary("info_test");
		book.setAuthors(new ArrayList<Author>());
		book.setCategory(BC);
		bookList.add(book);
		bookDAO.save(book);
		
		Book book2 = new Book();
		book2.setTitle("book2");
		book2.setSummary("info_Test");
		book2.setAuthors(new ArrayList<Author>());
		book2.setCategory(BC);
		bookDAO.save(book2);
		
		
		bookCategoryDAO.save(BC);
		UPDAO.save(UP);
		
		
		List<Book> storeBooks = bookDAO.findAll();
		
		
		SearchingStrategy SS = new SearchingByKeywords();
		
		List<Book> searchedBooks = SS.search(null, "book1", storeBooks);
		

	
		SearchingStrategy SearchByCat = new SearchingByCategories();
		
		List<Book> checkBooks = SearchByCat.search(UP, "", searchedBooks);
		
		
		
		
		assertTrue(
				
				(checkBooks.size()==1)
				&& (checkBooks.get(0).getTitle().equals(book.getTitle()))
				&& (checkBooks.get(0).getSummary().equals(book.getSummary()))
				
				);
		
	}
	
	
	
	@Test
	@Transactional
	void SeachByFavAuthorsTest() {
		
		UserProfile UP = new UserProfile();
		UP.setUsername("TEST");
		
		Author author = new Author();
		author.setName("TESTAUTHOR");
		UP.setFavouriteBookAuthors(List.of(author));
		
		
		List<Book> writenBooks = new ArrayList<Book>();
		
		
		
		Book book = new Book();
		book.setTitle("book1");
		book.setSummary("info_test");
		book.setAuthors(List.of(author));
		writenBooks.add(book);
		bookDAO.save(book);
		
		Book book2 = new Book();
		book2.setTitle("book2");
		book2.setSummary("info_Test");
		book2.setAuthors(List.of(author));
		bookDAO.save(book2);
		
		
		UPDAO.save(UP);
		
		
		List<Book> storeBooks = bookDAO.findAll();
		
		
		SearchingStrategy SS = new SearchingByKeywords();
		
		List<Book> searchedBooks = SS.search(null, "boo", storeBooks);
		

		SearchingStrategy SeachCategory = new SearchingByAuthor();
		
		
		List<Book> checkBooks = SeachCategory.search(UP, "", searchedBooks);
		
		assertTrue(
				(checkBooks.size() == 2)
				&& (checkBooks.get(0).getTitle().equals(book.getTitle()))
				&& (checkBooks.get(0).getSummary().equals(book.getSummary()))
				&& (checkBooks.get(1).getTitle().equals(book2.getTitle()))
				&& (checkBooks.get(1).getSummary().equals(book2.getSummary()))
				
				);
		
		
	}
	
	

}
