package com.socialbookstore.springboot.strategies;

import java.util.ArrayList;
import java.util.List;

import com.socialbookstore.springboot.model.Author;
import com.socialbookstore.springboot.model.Book;
import com.socialbookstore.springboot.model.UserProfile;

public class SearchingByKeywords implements SearchingStrategy{

	@Override
	public List<Book> search(UserProfile UP, String keywords, List<Book> storeBooks) {
		
		List<Book> finalBooks = new ArrayList<Book>();
		
		//get keywords here
		String keys[] = keywords.split(" ");
		
		//find the relative books
		for (Book checkBook: storeBooks) {
			
			for (String key: keys) {
				if (checkBook.getTitle().contains(key) || checkifAuthorMatch(key,checkBook)) {
					finalBooks.add(checkBook);
					break;
				}
			}
		}
		
		return finalBooks;
	}

	
	private boolean checkifAuthorMatch(String key, Book book) {
		for (Author checkAuthor: book.getAuthors()) {
			if (checkAuthor.getName().contains(key)) {
				return true;
			}
		}
		
		return false;
	}

}
