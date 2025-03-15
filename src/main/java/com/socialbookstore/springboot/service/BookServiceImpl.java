package com.socialbookstore.springboot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.socialbookstore.springboot.dao.BookDAO;
import com.socialbookstore.springboot.model.Book;

@Service
public class BookServiceImpl implements BookService{

	@Autowired
	BookDAO bookDAO;
	
	

	@Override
	public void saveBook(Book book) {
		
		bookDAO.save(book);
	}

	@Override
	public List<Book> getAllBookOffers() {
		return bookDAO.findAll();
	}

	@Override
	public Book findBookByTitle(String title) {
		return bookDAO.findByTitle(title).get();
	}
}
