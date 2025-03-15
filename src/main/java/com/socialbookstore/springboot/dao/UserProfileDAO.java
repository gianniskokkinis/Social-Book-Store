package com.socialbookstore.springboot.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.socialbookstore.springboot.model.UserProfile;

public interface UserProfileDAO extends JpaRepository<UserProfile, Integer>{
	
	public UserProfile findById(int id);
	
	public Optional<UserProfile> findByUsername(String username);
}
