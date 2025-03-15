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

import com.socialbookstore.springboot.model.*;
import com.socialbookstore.springboot.model.UserProfile;
import com.socialbookstore.springboot.service.BookStoreService;
import com.socialbookstore.springboot.service.UserService;


@Controller
public class UserController { //this is controller for main user
	
	//this is field to get username
	String username;
	
	@Autowired
    UserService userService;
	
	@Autowired
	BookStoreService storeService;
	
	
    @RequestMapping("/user/dashboard")
    public String getUserHome(Model model){
    	
    	//we are getting the username from authentication
    	username = SecurityContextHolder.getContext().getAuthentication().getName();
    	
    	
    	//we are taking the profile from database 
    	UserProfile checkUserProfile = userService.findUserProfile(username); //get the user profile
    	
    	
    	if (checkUserProfile == null) {
    		/*in case profile doesn't exist*/
    		
    		UserProfile newUserProfile = new UserProfile();
    		newUserProfile.setUsername(username);
    		userService.saveProfile(newUserProfile); //save to database
    		
    		
    		List<BookCategory> favouriteBookCategories = new ArrayList<BookCategory>();
        	List<Author> favouriteAuthors = new ArrayList<Author>();
        	List<BookCategory> getAllBookCategories = storeService.getAllBookCategories();
        	
        	model.addAttribute("bookcategories", getAllBookCategories);
        	model.addAttribute("favcategories",favouriteBookCategories);
        	model.addAttribute("favauthors",favouriteAuthors);
    		model.addAttribute("usernametext", username);
    		model.addAttribute("userprofile", newUserProfile);
    		return "user/editprofile";
    	}
    	
    	if(checkUserProfile.getFullname() == null) {
    		
    		/*if user decides to logout on edit profile view and login again  */
    		List<BookCategory> favouriteBookCategories = new ArrayList<BookCategory>();
        	List<Author> favouriteAuthors = new ArrayList<Author>();
        	List<BookCategory> getAllBookCategories = storeService.getAllBookCategories();
        	model.addAttribute("bookcategories", getAllBookCategories);
        	model.addAttribute("favcategories",favouriteBookCategories);
        	model.addAttribute("favauthors",favouriteAuthors);
    		model.addAttribute("usernametext", username);
    		model.addAttribute("userprofile", checkUserProfile);
    		return "user/editprofile";
    	}
    	
    	
    	model.addAttribute("displayusername", "BACK "+checkUserProfile.getUsername());
    	
        return "user/dashboard";
    }
    
    
    
    @RequestMapping("/user/editprofile")
    public String EditProfile(Model model) {
    	
    	//we are getting the username from authentication
    	username = SecurityContextHolder.getContext().getAuthentication().getName();
    	
    	//we are gettig user's user profile from database 
    	UserProfile checkUserProfile = userService.findUserProfile(username); //get the user profile
    	
    	
    	//need for html page 
    	List<BookCategory> favouriteBookCategories = checkUserProfile.getFavouriteBookCategories();
    	List<Author> favouriteAuthors = checkUserProfile.getFavouriteBookAuthors();
    	List<BookCategory> getAllBookCategories = storeService.getAllBookCategories();
    	
    	model.addAttribute("bookcategories", getAllBookCategories);
    	model.addAttribute("favcategories",favouriteBookCategories);
    	model.addAttribute("favauthors",favouriteAuthors);
    	model.addAttribute("userprofile", checkUserProfile);
    	model.addAttribute("usernametext", checkUserProfile.getUsername());
    	
    	return "user/editprofile";
    	
    }
    
    
    @RequestMapping("/user/saveprofile")
    public String saveProfile(@ModelAttribute("userprofile") UserProfile UP, Model model) {
    	
    	//getting username from database
    	username = SecurityContextHolder.getContext().getAuthentication().getName();
    	
    	//find the userprofile from database to update it 
    	UserProfile userProfile = userService.findUserProfile(username);
    	
    	//in case something went wrong with model 
    	if (userProfile==null) {
    		userProfile = new UserProfile();
    	}
    	
    	//update here the info
    	userProfile.setUser(userService.findUserByUsername(username));
    	userProfile.setUsername(username);
    	userProfile.setFullname(UP.getFullname());
    	userProfile.setAddress(UP.getAddress());
    	userProfile.setAge(UP.getAge());
    	userProfile.setPhoneNumber(UP.getPhoneNumber());
    	userProfile.setState(UP.getState());
    	userProfile.setPostcode(UP.getPostcode());
    	 
    	model.addAttribute("displayusername", username);
    	
    	
    	//store or update profile to database
    	userService.saveProfile(userProfile);
    	
    	return "/user/dashboard";
    }
    
    
    
    
    @RequestMapping("/user/send-requests")
    public String viewRequests(Model model) {
    	
    	//getting username from authentication
    	username = SecurityContextHolder.getContext().getAuthentication().getName();
    	
    	//get requested books from user
    	List<Book> requestedBooks = userService.getSendRequests(username);    	
    	

    	model.addAttribute("requests", requestedBooks);
    	
    	return "user/requests-list";	
    }
    
    
    
    @RequestMapping("/user/view-my-bookoffers")
    public String viewMyBookOffers(Model model) {
    	
    	
    	//getting my book offers
    	List<Book> usersBookOffer = userService.getAllBookOffers(username);
    	
    	
    	//here we are import our list to html
    	model.addAttribute("myoffers", usersBookOffer);
    	
    	
    	
    	
    	return "store/my-bookoffer-list";
    }
    
    
    
    @RequestMapping("/user/view-receive-requests")
   	public String viewRequestingUsers(@RequestParam("bookID") int bookID, Model model) {

    	
   		
   		//this is to view requesting users about selected book    	
       	List<UserProfile> requestingUsers = userService.getRequestingUsers(bookID);
       	

       	model.addAttribute("requestingusers",requestingUsers);
       	model.addAttribute("bookID", bookID);
       	
       	
   		return "store/receive-requests-list";//return some page here with something
   	}
    
    
    @RequestMapping("user/save-category-to-favourite-categories")
    public String addCategoryToUserFavCat(@RequestParam("userProfileID") int userProfileID, @RequestParam("category") String nameCategory) {
    	
    	/*
    	 * Here we are adding the book category to userProfile's favourite book categories
    	 * 
    	 * */
    	
    	storeService.addCategoryToFavouriteCategories(userProfileID, nameCategory);
    	
    	return "redirect:/user/editprofile";
    }
    
	
    
    @RequestMapping("user/delete-category-from-favourite-categories")
    public String deleteCategoryFromUserFavCat(@RequestParam("userProfileID") int userProfileID, @RequestParam("categoryID") int categoryID) {
    	
    	
    	storeService.deleteCategoryFromFavouriteCategories(userProfileID, categoryID);
    	
    	
    	return "redirect:/user/editprofile";
    }
    
    
    @RequestMapping("user/delete-author-from-favourite-authors")
    public String deleteAuthorFromUserFavAuth(@RequestParam("userProfileID") int userProfileID, @RequestParam("authorID") int authorID) {
    	
    	storeService.deleteAuthorFromFavouriteAuthors(userProfileID, authorID);
    	
    	
    	return "redirect:/user/editprofile";
    }
    
	
    
    @RequestMapping("/store/delete/my-request")
    public String deleteMyRequest(@RequestParam("bookID") int bookID) {
    	
    	username = SecurityContextHolder.getContext().getAuthentication().getName();
    	
    	storeService.deleteMyRequest(username, bookID);
    	
    	
    	return "redirect:/user/send-requests";
    }
    
    
   
	
	
    
    
}
