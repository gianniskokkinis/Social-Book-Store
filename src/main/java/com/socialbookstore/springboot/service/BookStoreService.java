package com.socialbookstore.springboot.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.socialbookstore.springboot.model.Author;
import com.socialbookstore.springboot.model.Book;
import com.socialbookstore.springboot.model.BookCategory;
import com.socialbookstore.springboot.model.Notification;
import com.socialbookstore.springboot.model.UserProfile;

@Service
public interface BookStoreService {
	public void createBookOffer(String Username, String title, String category, String summary, String authors);
	
	public void deleteBookOffer(int bookID);
	
	public List<BookCategory> getAllBookCategories();
	
	public List<Book> getAllBooks();
	
	public void sendRequest(String Username, int boodID);
	
	public UserProfile getUserProfileFromBook(int bookID);
	
	public void declineUserProfile(int bookID, int userProfileID);
	
	public UserProfile acceptedUserProfile(int bookID, int userProfileID);

	public List<Notification> getUserNotifications(String username);
	
	public List<Book> searchBook(String username, String keyWords, List<Book> storeBooks, String type);
	
	public List<Book> getRecommendedBooks(String username, String type);

	public List<Author> getAllAuthors();
	
	public List<Book> getBooksFromCategory(String categoryName);
	
	
	public List<Book> getBooksFromAuthor(String authorName);
	
	public void deleteNotification(String username, int notificationID);
	
	
	public void addCategoryToFavouriteCategories(int userProfileID, String categoryName);
	
	public void deleteCategoryFromFavouriteCategories(int userProfileID, int categoryID);
	
	public void deleteAuthorFromFavouriteAuthors(int userProfileID, int authorID);
	
	public void deleteMyRequest(String username, int bookID);
	
}
