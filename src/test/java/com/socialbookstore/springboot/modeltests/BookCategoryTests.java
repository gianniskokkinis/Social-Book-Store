package com.socialbookstore.springboot.modeltests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.socialbookstore.springboot.dao.BookCategoryDAO;
import com.socialbookstore.springboot.model.Book;
import com.socialbookstore.springboot.model.BookCategory;

import jakarta.transaction.Transactional;


@SpringBootTest
@TestPropertySource(
		  locations = "classpath:application.properties")
class BookCategoryTests {

	@Autowired
	BookCategoryDAO bookCategoryDAO;
	
	@Test
	@Transactional
	void BookCategorytest() {
		
		BookCategory BC = bookCategoryDAO.findByName("Thriller").get();
		
		Book book = new Book();
		book.setTitle("book1");
		book.setSummary("info_test");
		
		Book book2 = new Book();
		book2.setTitle("book2");
		book2.setSummary("info_Test");
		List<Book> bookList = new ArrayList<Book>();
		bookList.add(book);
		bookList.add(book2);
		BC.setBookList(bookList);
		bookCategoryDAO.save(BC);
		
		BookCategory checkBookCategory = bookCategoryDAO.findById(BC.getId()).get();
		
		assertTrue(
				(checkBookCategory.getName().equals(BC.getName()))
				&& (checkBookCategory.getBookList().size() == 2)
				&& (checkBookCategory.getBookList().get(0).getTitle().equals(book.getTitle()))
				&& (checkBookCategory.getBookList().get(0).getSummary().equals(book.getSummary()))
				&& (checkBookCategory.getBookList().get(1).getTitle().equals(book2.getTitle()))
				&& (checkBookCategory.getBookList().get(1).getSummary().equals(book2.getSummary()))
				
				);
		
		
	}

}
