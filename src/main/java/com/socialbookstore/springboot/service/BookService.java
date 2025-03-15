package com.socialbookstore.springboot.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.socialbookstore.springboot.model.Book;

@Service
public interface BookService {
	public void saveBook(Book book);
	
	public List<Book> getAllBookOffers();
	
	public Book findBookByTitle(String title);
}
