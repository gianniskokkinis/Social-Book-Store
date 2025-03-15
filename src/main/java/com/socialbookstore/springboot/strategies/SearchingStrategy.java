package com.socialbookstore.springboot.strategies;

import java.util.List;

import com.socialbookstore.springboot.model.Book;
import com.socialbookstore.springboot.model.UserProfile;

public interface SearchingStrategy {
	
	public List<Book> search(UserProfile UP, String keywords, List<Book> storeBooks);

}
