package com.socialbookstore.springboot.strategies;

import java.util.ArrayList;
import java.util.List;

import com.socialbookstore.springboot.model.Book;
import com.socialbookstore.springboot.model.BookCategory;
import com.socialbookstore.springboot.model.UserProfile;

public class SearchingByCategories implements SearchingStrategy{

	@Override
	public List<Book> search(UserProfile UP, String keywords, List<Book> storeBooks) {
		
		List<Book> finalList = new ArrayList<Book>();
		
		List<BookCategory> favBookCategory = UP.getFavouriteBookCategories();
		
		
		
		for (Book checkBook: storeBooks) {
			for (BookCategory checkCategory: favBookCategory) {
				if(checkBook.getCategory().getName().equals(checkCategory.getName())) {
					finalList.add(checkBook);
					break;
				}
			}
			
		}
		
		
		return finalList;
	}

}
