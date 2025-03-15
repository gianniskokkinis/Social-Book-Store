package com.socialbookstore.springboot.strategies;

import java.util.List;

import com.socialbookstore.springboot.model.Book;
import com.socialbookstore.springboot.model.UserProfile;

public interface RecommendedStrategy {
	
	
	public List<Book> getRecommended(UserProfile UP);

}
