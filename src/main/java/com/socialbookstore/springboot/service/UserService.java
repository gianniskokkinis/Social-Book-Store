package com.socialbookstore.springboot.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.socialbookstore.springboot.model.Book;
import com.socialbookstore.springboot.model.User;
import com.socialbookstore.springboot.model.UserProfile;

@Service
public interface UserService {
	public void saveUser(User user);
    
	public boolean isUserPresent(User user);
    
    public User findUserByUsername(String Username);
    
    public UserProfile findUserProfile(String username);
    
    public void saveProfile(UserProfile userProfile);
    
    public void deleteProfile(UserProfile userProfile);
    
    public List<Book> getSendRequests(String Username);
    
    public List<Book> getAllBookOffers(String Username);
    
    public List<UserProfile> getRequestingUsers(int bookID);
    
    public void deleteBookOffer(int bookID);
    
    
}
