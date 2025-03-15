package com.socialbookstore.springboot.strategies;

import java.util.ArrayList;
import java.util.List;

import com.socialbookstore.springboot.model.Book;
import com.socialbookstore.springboot.model.BookCategory;
import com.socialbookstore.springboot.model.UserProfile;


/**
 * This GOF pattern is motivation 
 * 
 * */

public class RecommendedByFavCategory implements RecommendedStrategy{

	@Override
	public List<Book> getRecommended(UserProfile UP) {
		
		//here we are return the books
		List<Book> finalBooks = new ArrayList<Book>();
		
		
		List<BookCategory> favBookCategory = UP.getFavouriteBookCategories();
		
		for(BookCategory checkBookCategory: favBookCategory) {
			for(Book checkBook: checkBookCategory.getBookList()) {
				finalBooks.add(checkBook);
			}
		}
		
		return finalBooks;
	}

}
