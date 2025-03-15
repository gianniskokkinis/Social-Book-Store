package com.socialbookstore.springboot.strategies;

import java.util.ArrayList;
import java.util.List;

import com.socialbookstore.springboot.model.Author;
import com.socialbookstore.springboot.model.Book;
import com.socialbookstore.springboot.model.UserProfile;

public class RecommendedByFavAuthor implements RecommendedStrategy{

	@Override
	public List<Book> getRecommended(UserProfile UP) {
		
		List<Book> finalBooks = new ArrayList<Book>();
		
		List<Author> favAuthors = UP.getFavouriteBookAuthors();
		
		for(Author author: favAuthors) {
			for(Book writenBook: author.getWrittenBooks()) {
				if(!(finalBooks.contains(writenBook))) {
					finalBooks.add(writenBook);
				}
			}
		}
		
		return finalBooks;
	}

}
