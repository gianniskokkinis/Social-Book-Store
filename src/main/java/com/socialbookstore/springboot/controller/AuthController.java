package com.socialbookstore.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.socialbookstore.springboot.dao.AuthorDAO;
import com.socialbookstore.springboot.dao.BookDAO;
import com.socialbookstore.springboot.model.Author;
import com.socialbookstore.springboot.model.Book;
import com.socialbookstore.springboot.model.User;
import com.socialbookstore.springboot.model.UserProfile;
import com.socialbookstore.springboot.service.UserService;

@Controller
public class AuthController {
	
    @Autowired
    UserService userService;
    

    @RequestMapping("/login")
    public String login(){
    	
    	
        return "auth/signin";
    }

    @RequestMapping("/register")
    public String register(Model model){
        model.addAttribute("user", new User());
        return "auth/signup";
    }
    

    @RequestMapping("/save")
    public String registerUser(@ModelAttribute("user") User user, Model model){
       
    	
    	//in case user exists
        if(userService.isUserPresent(user)){
            model.addAttribute("successMessage", "User already registered!");
            return "auth/signin";
        }
       
        
        userService.saveUser(user);
       
       

        return "auth/signin";
        
        
    }
    
    
}
