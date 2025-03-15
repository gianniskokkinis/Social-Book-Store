package com.socialbookstore.springboot.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.socialbookstore.springboot.dao.AuthorDAO;
import com.socialbookstore.springboot.dao.BookCategoryDAO;
import com.socialbookstore.springboot.dao.BookDAO;
import com.socialbookstore.springboot.dao.NotificationDAO;
import com.socialbookstore.springboot.dao.UserProfileDAO;
import com.socialbookstore.springboot.model.Author;
import com.socialbookstore.springboot.model.Book;
import com.socialbookstore.springboot.model.BookCategory;
import com.socialbookstore.springboot.model.Notification;
import com.socialbookstore.springboot.model.UserProfile;
import com.socialbookstore.springboot.strategies.RecommendedByFavAuthor;
import com.socialbookstore.springboot.strategies.RecommendedByFavCategory;
import com.socialbookstore.springboot.strategies.RecommendedStrategy;
import com.socialbookstore.springboot.strategies.SearchingByAuthor;
import com.socialbookstore.springboot.strategies.SearchingByCategories;
import com.socialbookstore.springboot.strategies.SearchingByKeywords;
import com.socialbookstore.springboot.strategies.SearchingStrategy;

@Service
public class BookStoreServiceImpl implements BookStoreService{

	
	
	@Autowired
	UserProfileDAO userProfileDAO;
	
	@Autowired
	BookCategoryDAO bookCategoryDAO;
	
	@Autowired
	BookDAO bookDAO;
	
	@Autowired 
	AuthorDAO authorDAO;
	
	@Autowired
	NotificationDAO notificationDAO;
	
	@Override
	public void createBookOffer(String Username, String title, String category, String summary, String authors) {
		
		UserProfile UP = userProfileDAO.findByUsername(Username).get();
		BookCategory BC = bookCategoryDAO.findByName(category).get();
		
		Book book = new Book();
		
		book.setTitle(title);
		book.setSummary(summary);
		book.setCategory(BC);
		
		//set create Date 
		book.setPublish_date(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()));
		
		
		//set authors
		String[] authorNames = authors.split(",");

		
		for (String name: authorNames) {
			
			
			Author author = getAuthorIfExists(name);
			
			/* in case  doesn't exists */
			if (author == null) {
				author = new Author();
				author.setName(name);
				
				List<Book> writenBooks = new ArrayList<Book>();
				writenBooks.add(book);
				
			}else {
				/*in case  exists*/
				
				List<Book> writenBooks = author.getWrittenBooks();
				if (writenBooks == null) {
					 writenBooks = new ArrayList<Book>();
				}
				writenBooks.add(book);
			}
			
			
			List<Author> authorsList = book.getAuthors();
			if(authorsList == null) {
				authorsList = new ArrayList<Author>();
			}
			authorsList.add(author);
			book.setAuthors(authorsList);

			
		}
		
		
		
		//add book to category
		List<Book> books = BC.getBookList();
		books.add(book);
		BC.setBookList(books);
		
		
		//add book to user's book offers
		List<Book> bookOffers = UP.getBookOffers();
		bookOffers.add(book);
		UP.setBookOffers(bookOffers);
		
		userProfileDAO.save(UP);

	}
	

	@Override
	public List<BookCategory> getAllBookCategories() {
		return bookCategoryDAO.findAll();
	}


	@Override
	public List<Book> getAllBooks() {
		return bookDAO.findAll();
	}


	@Override
	public void sendRequest(String Username, int bookID) {
		
		
		Book book = bookDAO.findById(bookID);
		UserProfile UP = userProfileDAO.findByUsername(Username).get();
		
	
		
		
		//add useProfile to requesting user of book 
		List<UserProfile> UPList = book.getRequestingUsers();
		if(UPList == null) {
			UPList = new ArrayList<UserProfile>();
		}
		
		
		//in case the request exists
		if(UPList.contains(UP)) {
			return ;
		}
		
		UPList.add(UP);
		book.setRequestingUsers(UPList);
		
		//add book to requested books list 
		List<Book> bookList = UP.getRequestedBooks();
		if(bookList == null) {
			bookList = new ArrayList<Book>();
		}
		
		bookList.add(book);
		UP.setRequestedBooks(bookList);
		
		//adding Category to user's favourite categories
		List<BookCategory> favCategories = UP.getFavouriteBookCategories();
		//in case  exist
		if(favCategories == null) {
			favCategories = new ArrayList<BookCategory>();
		}
		//in case  exist
		if(!(favCategories.contains(book.getCategory()))) {
			favCategories.add(book.getCategory());
		}
		UP.setFavouriteBookCategories(favCategories);
		
		//adding authors to user's favourite authors 
		List<Author> favAuthors = UP.getFavouriteBookAuthors();
		if(favAuthors == null) {
			favAuthors = new ArrayList<Author>();
		}
		for (Author takeAuthor: book.getAuthors()) {
			if(!(favAuthors.contains(takeAuthor))) {
				favAuthors.add(takeAuthor);
			}
		}
		UP.setFavouriteBookAuthors(favAuthors);
		
		
		
		
		
		
		bookDAO.save(book);
		userProfileDAO.save(UP);
	}


	@Override
	public UserProfile getUserProfileFromBook(int bookID) {
		Book book = bookDAO.findById(bookID);
		return book.getUserProfile();
	}


	@Override
	public void declineUserProfile(int bookID, int userProfileID) {
		
		
		Book book = bookDAO.findById(bookID);	
		UserProfile UP = userProfileDAO.findById(userProfileID);
		
		
		//remove from requestedBooks
		List<Book> requestedBooks = UP.getRequestedBooks();
		requestedBooks.remove(book);
		UP.setRequestedBooks(requestedBooks);
		userProfileDAO.save(UP);
		
		
		//remove from requestingUsers
		List<UserProfile> requestingUsers = book.getRequestingUsers();
		requestingUsers.remove(UP);
		book.setRequestingUsers(requestingUsers);
		bookDAO.save(book);
		
		
		//create Notification 
		Notification declnotification = new Notification();
		declnotification.setBookTitle(book.getTitle());
		declnotification.setDescription("Unfortunately, the book was given to someone else :( Better Luck Next Time!");
		declnotification.setNotDate(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()));
		
		
		//send to user 
		List<Notification> mynotifications = UP.getNotifications();
		if(mynotifications == null) {
			mynotifications = new ArrayList<Notification>();
		}
		mynotifications.add(declnotification);
		userProfileDAO.save(UP);
		
		
		
		
	}


	@Override
	public UserProfile acceptedUserProfile(int bookID, int userProfileID) {
	
		
		
		Book book = bookDAO.findById(bookID);
		UserProfile UP = userProfileDAO.findById(userProfileID);
		
		
		/**
		 * 
		 * DELETE THE BOOK FROM EVERYWHERE 	
		 * 
		 * 
		 * */
		
		
		
		//delete from book offers 
		UserProfile Owner = book.getUserProfile();
		List<Book> bookOffers = Owner.getBookOffers();
		bookOffers.remove(book);
		Owner.setBookOffers(bookOffers);
		userProfileDAO.save(Owner);
		
		
		//delete the users from requesting 
		List<UserProfile> requestingUsers = book.getRequestingUsers();
		requestingUsers.remove(UP);
		book.setRequestingUsers(requestingUsers);
		bookDAO.save(book);
		
		//delete requested books from UP
		List<Book> requestedBooks = UP.getRequestedBooks();
		requestedBooks.remove(book);
		UP.setRequestedBooks(requestedBooks);
		userProfileDAO.save(UP);
		
		//delete from book category 
		BookCategory BC = book.getCategory();
		List<Book> hasBooks = BC.getBookList();
		hasBooks.remove(book);
		BC.setBookList(hasBooks);
		bookCategoryDAO.save(BC);
		
		//delete authors here 
		List<Author> authors = book.getAuthors();
		for (Author everyAuthor: authors) {
			List<Book> writenBooks = everyAuthor.getWrittenBooks();
			writenBooks.remove(book);
			everyAuthor.setWrittenBooks(writenBooks);
			authorDAO.save(everyAuthor);
		}
		
		bookDAO.delete(book);
		
		
		//create notifications 
		
		
		//accepted User Notification 
		Notification acceptedUserNotification = new Notification();
		acceptedUserNotification.setBookTitle(book.getTitle());
		acceptedUserNotification.setDescription("Congratulations! You are the chosen one for the book! "
		+"Please contact "	+ Owner.getUsername()+
		" in order to arrange the delivary of the book ( "+
		"Fullname: "+Owner.getFullname()+
		" - Phone: "+ Owner.getPhoneNumber()+
		" - Email: "+ Owner.getUser().getEmail()+" )"
		);
		
		
		
		acceptedUserNotification.setNotDate(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()));
		
		
		//send to accepted User 
		List<Notification> accUserNotif = UP.getNotifications();
		if (accUserNotif == null) {
			accUserNotif = new ArrayList<Notification>();
		}
		accUserNotif.add(acceptedUserNotification);
		userProfileDAO.save(UP);
		
		
		
		//declined User Notification 
		Notification declUserNotification = new Notification();
		declUserNotification.setBookTitle(book.getTitle());
		declUserNotification.setDescription("Unfortunately, the book "
				+book.getTitle()+
				"was given to someone else :( Better Luck Next Time!");
		declUserNotification.setNotDate(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()));
		
		
		//send user to other users 
		for (UserProfile checkUserProfile: requestingUsers) {
			List<Notification> checkUserNotif = checkUserProfile.getNotifications();
			if (checkUserNotif == null) {
				checkUserNotif = new ArrayList<Notification>();
			}
			checkUserNotif.add(declUserNotification);
			userProfileDAO.save(checkUserProfile);
		}
		
		
		return UP;
		
		 
		
	}
	
	@Override
	public void deleteBookOffer(int bookID) {

		
		/**
		 * 
		 * DELETE THE BOOK FROM EVERYWHERE 	
		 * 
		 * 
		 * */
		
		Book book = bookDAO.findById(bookID);
		
		
		//delete from book offers 
		UserProfile Owner = book.getUserProfile();
		List<Book> bookOffers = Owner.getBookOffers();
		bookOffers.remove(book);
		Owner.setBookOffers(bookOffers);
		userProfileDAO.save(Owner);
		
		/* CREATE NOTIFICATION HERE*/
		
		
		//delete requested books from UP
		List<UserProfile> requestingUsers = book.getRequestingUsers();
		for (UserProfile takeUser: requestingUsers) {
			List<Book> requestedBooks = takeUser.getRequestedBooks();
			requestedBooks.remove(book);
			takeUser.setRequestedBooks(requestedBooks);
			userProfileDAO.save(takeUser);
		}
		
		
		//delete the users from requesting 
		book.setRequestingUsers(new ArrayList<UserProfile>());
		bookDAO.save(book);
		
		
		
	

		
		//delete from book category 
		BookCategory BC = book.getCategory();
		List<Book> hasBooks = BC.getBookList();
		hasBooks.remove(book);
		BC.setBookList(hasBooks);
		bookCategoryDAO.save(BC);
		
		//delete authors here 
		List<Author> authors = book.getAuthors();
		for (Author everyAuthor: authors) {
			List<Book> writenBooks = everyAuthor.getWrittenBooks();
			writenBooks.remove(book);
			everyAuthor.setWrittenBooks(writenBooks);
			authorDAO.save(everyAuthor);
		}
		
		bookDAO.delete(book);
		
		
		
		
		
	}


	@Override
	public List<Notification> getUserNotifications(String username) {
		UserProfile UP = userProfileDAO.findByUsername(username).get();
		return UP.getNotifications();
	}


	@Override
	public List<Book> searchBook(String username, String keyWords, List<Book> storeBooks, String type) {
		
		/*this is motivation strategy*/
		SearchingStrategy searchingStrategy;
		
		if (type.equals("KEYWORDS")) {
			searchingStrategy = new SearchingByKeywords();
		}else {
			if (type.equals("CATEGORIES")) {
				searchingStrategy = new SearchingByCategories();
			}else {
				//here we have case with authors 
				searchingStrategy = new SearchingByAuthor();
			}
		}
		
		UserProfile UP = userProfileDAO.findByUsername(username).get();
		List<Book> getBooks = searchingStrategy.search(UP, keyWords, storeBooks);
		
		
		return getBooks;
	}


	@Override
	public List<Book> getRecommendedBooks(String username, String type) {
		RecommendedStrategy recStrategy;
		List<Book> getBooks;
		
		
		
		if(type.equals("FAV_AUTHOR")) {
			recStrategy = new RecommendedByFavAuthor();
		}else {
			recStrategy = new RecommendedByFavCategory();		
		}
		
		
		UserProfile UP = userProfileDAO.findByUsername(username).get();
		
		getBooks = recStrategy.getRecommended(UP);			
		
		
		return getBooks;
	}


	@Override
	public List<Author> getAllAuthors() {
		return authorDAO.findAll();
	}
	
	
	//this method is about getting the author if exists
	private Author getAuthorIfExists(String name) {
		
		List<Author> authorList = authorDAO.findAll();
		
		for (Author checkAuthor: authorList) {
			if (checkAuthor.getName().equals(name)) {
				return checkAuthor;
			}
		}
		
		
		return null;
	}


	@Override
	public List<Book> getBooksFromCategory(String categoryName) {
		BookCategory BC = bookCategoryDAO.findByName(categoryName).get();
		return BC.getBookList();
	}


	@Override
	public List<Book> getBooksFromAuthor(String authorName) {
		Author author = authorDAO.findByName(authorName).get();
		return author.getWrittenBooks();
	}


	@Override
	public void deleteNotification(String username, int notificationID) {
		
		UserProfile UP = userProfileDAO.findByUsername(username).get();
		Notification notification = notificationDAO.findById(notificationID);
		
		
		List<Notification> userProfileNotifications = UP.getNotifications();
		userProfileNotifications.remove(notification);
		UP.setNotifications(userProfileNotifications);
		
		
		notificationDAO.delete(notification);
		userProfileDAO.save(UP);
		
		
		
	}


	@Override
	public void addCategoryToFavouriteCategories(int userProfileID, String categoryName) {
		
		UserProfile UP = userProfileDAO.findById(userProfileID);
		BookCategory BC = bookCategoryDAO.findByName(categoryName).get();
		
		List<BookCategory> favBookCategories = UP.getFavouriteBookCategories();
		if (!(favBookCategories.contains(BC))) {
			favBookCategories.add(BC);
		}
		UP.setFavouriteBookCategories(favBookCategories);
		
		userProfileDAO.save(UP);
		
	}


	@Override
	public void deleteCategoryFromFavouriteCategories(int userProfileID, int categoryID) {
		
		UserProfile UP = userProfileDAO.findById(userProfileID);
		BookCategory BC = bookCategoryDAO.findById(categoryID).get();
		
		//remove from favourite book categories
		List<BookCategory> favBookCategories = UP.getFavouriteBookCategories();
		favBookCategories.remove(BC);
		UP.setFavouriteBookCategories(favBookCategories);
		
		
		userProfileDAO.save(UP);
		
		
	}


	@Override
	public void deleteAuthorFromFavouriteAuthors(int userProfileID, int authorID) {


		UserProfile UP = userProfileDAO.findById(userProfileID);
		Author author = authorDAO.findById(authorID);
		//delete from favourite book authors
		List<Author> favAuthors = UP.getFavouriteBookAuthors();
		favAuthors.remove(author);
		UP.setFavouriteBookAuthors(favAuthors);
		
		userProfileDAO.save(UP);
		
	}


	@Override
	public void deleteMyRequest(String username, int bookID) {


		/*requesting user - requested users*/
		
		UserProfile UP = userProfileDAO.findByUsername(username).get();
		Book book = bookDAO.findById(bookID);
		//delete from requested books
		List<Book> requestedBooks = UP.getRequestedBooks();
		requestedBooks.remove(book);
		UP.setRequestedBooks(requestedBooks);
		//delete from requesting users
		List<UserProfile> requestingUsers = book.getRequestingUsers();
		requestingUsers.remove(UP);
		book.setRequestingUsers(requestingUsers);
		
		
		bookDAO.save(book);
		userProfileDAO.save(UP);
		
	}
	
	
	
}
