package com.socialbookstore.springboot.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.socialbookstore.springboot.model.Notification;

public interface NotificationDAO extends JpaRepository<Notification, Integer>{

	public Notification findById(int id);
	
	
}
