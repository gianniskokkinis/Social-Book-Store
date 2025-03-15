package com.socialbookstore.springboot.strategies;

import java.util.ArrayList;
import java.util.List;

import com.socialbookstore.springboot.model.Author;
import com.socialbookstore.springboot.model.Book;
import com.socialbookstore.springboot.model.UserProfile;

public class SearchingByAuthor implements SearchingStrategy{

	@Override
	public List<Book> search(UserProfile UP, String keywords, List<Book> storeBooks) {
		
		List<Book> finalList = new ArrayList<Book>();
		
		List<Author> favAuthors = UP.getFavouriteBookAuthors();
		
		for(Book checkBook: storeBooks) {
			for (Author checkAuthor: checkBook.getAuthors()) {
				if (checkIfAuthorInFavAuthors(favAuthors,checkAuthor)) {
					finalList.add(checkBook);
					break;
				}
			}
		}
		
		
		return finalList;
	}
	
	
	private boolean checkIfAuthorInFavAuthors(List<Author> favAuthors, Author author) {
		
		for(Author checkAuthor: favAuthors) {
			if (checkAuthor.getName().equals(author.getName())) {
				return true;
			}
		}
		
		return false;
	}

	
	
}
