package com.socialbookstore.springboot.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.socialbookstore.springboot.model.Author;
import com.socialbookstore.springboot.model.Book;
import com.socialbookstore.springboot.model.BookCategory;
import com.socialbookstore.springboot.model.Notification;
import com.socialbookstore.springboot.model.User;
import com.socialbookstore.springboot.model.UserProfile;
import com.socialbookstore.springboot.service.BookStoreService;
import com.socialbookstore.springboot.service.UserService;

@Controller
public class StoreController {

	
	@Autowired
	UserService userService;
	
	@Autowired
	BookStoreService storeService;
	
	//this is for searched books from user's input
	List<Book> searchedBooks;
	
	//this is the user's input 
	String searchKeyWords = "";
	

	@RequestMapping("/store")
	public String getStore(Model model) { 
		
		searchKeyWords = ""; //clean if typed previous
		
		//get username from authentication
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("myusername",username);
		
		//get all book offers of the social book store and put them into html page
		List<Book> bookOffers = storeService.getAllBooks();
		model.addAttribute("bookList", bookOffers);
		
		//get all author from database and put them into html page 
		List<Author> authors = storeService.getAllAuthors();
		model.addAttribute("authors", authors);
		
		//get all book categories and put them into html page
		List<BookCategory> bookCategories = storeService.getAllBookCategories();
		model.addAttribute("bookCategories",bookCategories);
		
		
		return "store/browse-books";
		
	}
	
	
	@RequestMapping("/store/offersuccess")
	public String publicBookOffer(@ModelAttribute("userprofile") UserProfile userProfile ,@RequestParam("title") String title,
			@RequestParam("category") String category, @RequestParam("summary") String summary,
			@RequestParam("authors") String authors) {
		
		
		//get username here
		String username = SecurityContextHolder.getContext().getAuthentication().getName();

		//store to database
		storeService.createBookOffer(username, title, category, summary, authors);
		
		return "store/bookoffersucess";
	}
	
	@RequestMapping("/store/createbookoffer")
	public String createBookOffer(Model model) {
		
		//we need this for option section 
		List<BookCategory> bookCategories = storeService.getAllBookCategories();
		
		model.addAttribute("bookcategories", bookCategories);
		
		return "store/createbookoffer";
	}
	
	
	
	@RequestMapping("/store/delete-bookoffer")
	public String deleteBookOffer(@RequestParam("bookID") int bookID, Model model) {
		
		
    	
		storeService.deleteBookOffer(bookID);
		
		
		
		return "/user/dashboard";
	}
	
	
	@RequestMapping("/store/request")
	public String requestBook(@RequestParam("bookID") int bookID) {
		
		//getting username from authentication
		String username = SecurityContextHolder.getContext().getAuthentication().getName();

		storeService.sendRequest(username, bookID);

		
		
		return "store/RequestSuccess";
	}
	
	
	@RequestMapping("/store/viewprofile")
	public String viewProfile(@RequestParam("bookID") int bookID, Model model) {
		
		/*  for when we are getting userProfile from book when the user clicks view profile
		 *  And for when user accepts a request
		 * */
		
		//get Owner of the book
		 UserProfile userProfile = storeService.getUserProfileFromBook(bookID);	
		
		 //if Owner has other book offers 
		 List<Book> userBookOffers = userProfile.getBookOffers();
		
		 model.addAttribute("profile", userProfile);
		 model.addAttribute("bookOffers", userBookOffers);
		 model.addAttribute("contactmessage","");		 
		
		 
		return "store/view-profile";
	}
	
	
	
	@RequestMapping("/store/decline-user")
	public String declineUserProfile(@RequestParam("bookID") int bookID, @RequestParam("userProfileID") int userProfileID) {
		
		storeService.declineUserProfile(bookID, userProfileID);
		
		
		return "redirect:/store";
	}
	
	
	@RequestMapping("/store/accept-user")
	public String AcceptUserProfile(@RequestParam("bookID") int bookID, @RequestParam("userProfileID") int userProfileID, Model model) {
		
		//getting UserProfile
		UserProfile UP = storeService.acceptedUserProfile(bookID, userProfileID);
		//if the user has othe book offers
		List<Book> UPBookOffers = UP.getBookOffers();
		
		
		model.addAttribute("profile",UP);
		model.addAttribute("bookOffers",UPBookOffers);
		model.addAttribute("contactmessage","The user has been informed that you have accepted the request! Please contact the user to send the book.");
		
		
		
		
		return "store/view-profile";
	}
	
	
	
	
	
	@RequestMapping("/store/search-book")
	public String searchBook(@RequestParam("search-input") String searchInput, Model model) {
		
		
		
		String username = SecurityContextHolder.getContext().getAuthentication().getName();

		searchKeyWords=searchInput;
		
		
		//if user try to search without keywords 
		if (searchInput.length()==0) {
			model.addAttribute("warning_message","Please enter something on search bar");
			return "store/browse-books";
		}
		
		
		model.addAttribute("myusername",username);
		
		
		//get the authors
		List<Author> authors = storeService.getAllAuthors();
		model.addAttribute("authors", authors);
		
		//get the categories
		List<BookCategory> bookCategories = storeService.getAllBookCategories();
		model.addAttribute("bookCategories",bookCategories);
		
		//get all list
		List<Book> storeBooks = storeService.getAllBooks();
		
		searchedBooks = storeService.searchBook(username, searchInput, storeBooks, "KEYWORDS");
		model.addAttribute("bookList", searchedBooks);
		
		if(searchedBooks == null || (searchedBooks.size()==0)) {
			model.addAttribute("warning_message", "No results for the '"+searchKeyWords+"'");
		}
		
		
		model.addAttribute("keywords",searchKeyWords);
		
		return "store/browse-books";
	}
	
	@RequestMapping("/store/search-book-by-fav-categories")
	public String searchBookByFavCategories(Model model) {
		
		
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("myusername",username);
		
		//get the authors
		List<Author> authors = storeService.getAllAuthors();
		model.addAttribute("authors", authors);
		
		//get the categories
		List<BookCategory> bookCategories = storeService.getAllBookCategories();
		model.addAttribute("bookCategories",bookCategories);
		
		
		//if user try to click search without type anything on search bar 
		if (searchKeyWords.length()==0) {
			model.addAttribute("warning_message", "Please first enter something on searchbar!");
			return "store/browse-books";
		}
		
		
		if (searchedBooks != null) {
			searchedBooks = storeService.searchBook(username, "", searchedBooks, "CATEGORIES");
			model.addAttribute("bookList", searchedBooks);
			model.addAttribute("warning_message", searchedBooks.size()+" results for the "+searchKeyWords);
			
		}else {
			//in case if nothing searched before
			List<Book> storeBooks = storeService.getAllBooks();
			List<Book> getBooks = storeService.searchBook(username, "", storeBooks, "CATEGORIES");
			model.addAttribute("bookList", getBooks);	
			model.addAttribute("warning_message", getBooks.size()+" results for the "+searchKeyWords);
			
		}
		
		
		model.addAttribute("keywords",searchKeyWords);
		return "store/browse-books";
	}
	
	
	@RequestMapping("/store/search-book-by-fav-authors")
	public String searchBookByFavAuthors(Model model) {
		
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("myusername",username);
		
		//get the authors
		List<Author> authors = storeService.getAllAuthors();
		model.addAttribute("authors", authors);
		
		//get the categories
		List<BookCategory> bookCategories = storeService.getAllBookCategories();
		model.addAttribute("bookCategories",bookCategories);
		
		//if user try to click search without type anything on search bar 
		if (searchKeyWords.length()==0) {
			model.addAttribute("warning_message", "Please first enter something on searchbar!");
			return "store/browse-books";
		}
		
		if (searchedBooks != null) {
			searchedBooks = storeService.searchBook(username, "", searchedBooks, "AUTHORS");
			model.addAttribute("bookList", searchedBooks);
			model.addAttribute("warning_message", searchedBooks.size()+" results for the "+searchKeyWords);
		}else {
			//in case nothing searched before
			List<Book> storeBooks = storeService.getAllBooks();
			List<Book> getBooks = storeService.searchBook(username, "", storeBooks, "AUTHORS");
			model.addAttribute("bookList", getBooks);	
			model.addAttribute("warning_message", getBooks.size()+" results for the "+searchKeyWords);
		}
		
		
		
		model.addAttribute("keywords",searchKeyWords);
		return "store/browse-books";
	}
	
	
	
	@RequestMapping("/store/recommened-book-by-fav-authors")
	public String recommendedBooksByFavAuthors(Model model) {

		String username = SecurityContextHolder.getContext().getAuthentication().getName();

		//get the authors
		List<Author> authors = storeService.getAllAuthors();
		model.addAttribute("authors", authors);
		
		//get the categories
		List<BookCategory> bookCategories = storeService.getAllBookCategories();
		model.addAttribute("bookCategories",bookCategories);

		
		List<Book> getRecommendedBooks = storeService.getRecommendedBooks(username, "FAV_AUTHOR");
		model.addAttribute("bookList", getRecommendedBooks);	
		
		
		if(getRecommendedBooks == null || (getRecommendedBooks.size()==0)) {
			model.addAttribute("warning_message", "No results for favourite authors");
		}
		
		return "store/browse-books";
	}
	
	
	@RequestMapping("/store/recommened-book-by-fav-categories")
	public String recommendedBooksByFavCategories(Model model) {
		
		
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		
		
		//get the authors
		List<Author> authors = storeService.getAllAuthors();
		model.addAttribute("authors", authors);
		
		//get the categories
		List<BookCategory> bookCategories = storeService.getAllBookCategories();
		model.addAttribute("bookCategories",bookCategories);

		
		List<Book> getRecommendedBooks = storeService.getRecommendedBooks(username, "FAV_CATEGORIES");
		model.addAttribute("bookList", getRecommendedBooks);	
		
		
		if(getRecommendedBooks == null || (getRecommendedBooks.size()==0)) {
			model.addAttribute("warning_message", "No results for favourite categories");
		}
		
		
		
		return "store/browse-books";
	}
	
	
	@RequestMapping("/store/view-my-notifications")
	public String viewMyNotifications(Model model) {
		
		
		//get username here
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		
		//get user's notifications from database
		List<Notification> myNotifications = storeService.getUserNotifications(username);
		
		
		model.addAttribute("mynotifications", myNotifications);
		
		
		return "store/notifications-list";
	}
	
	
	@RequestMapping("/store/select-recommended")
	public String selectRecommended() {
		return "store/select-recommended";
	}
	
	
	
	@RequestMapping("/store/get-books-from-category-name")
	public String getBooksFromCategoryName(@RequestParam("categoryname") String categoryname,Model model) {

		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("myusername",username);
		
		//get the authors
		List<Author> authors = storeService.getAllAuthors();
		model.addAttribute("authors", authors);
		
		//get the categories
		List<BookCategory> bookCategories = storeService.getAllBookCategories();
		model.addAttribute("bookCategories",bookCategories);

		List<Book> getRecommendedBooks = storeService.getBooksFromCategory(categoryname);
		model.addAttribute("bookList", getRecommendedBooks);	
		
		if(getRecommendedBooks == null || (getRecommendedBooks.size()==0)) {
			model.addAttribute("warning_message", "No results for this category");
		}
		
		
		return "store/browse-books";
	}
	
	
	@RequestMapping("/store/get-books-from-author-name")
	public String getBooksFromAuthorName(@RequestParam("authorname") String authorName,Model model) {

		
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		model.addAttribute("myusername",username);
		
		//get the authors
		List<Author> authors = storeService.getAllAuthors();
		model.addAttribute("authors", authors);
		
		//get the categories
		List<BookCategory> bookCategories = storeService.getAllBookCategories();
		model.addAttribute("bookCategories",bookCategories);

		List<Book> getRecommendedBooks = storeService.getBooksFromAuthor(authorName);
		model.addAttribute("bookList", getRecommendedBooks);	
		
		
		return "store/browse-books";
	}
	
	
	
	@RequestMapping("/store/delete-notification")
	public String deleteNotification(@RequestParam("notificationID") int notificationID) {
		
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
		
		storeService.deleteNotification(username,notificationID);
		
		return "redirect:/store/view-my-notifications";
	}
	
	
	
	
	
}
