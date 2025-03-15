package com.socialbookstore.springboot.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.socialbookstore.springboot.model.BookCategory;

@Repository
public interface BookCategoryDAO extends JpaRepository<BookCategory, Integer>{
	
	public Optional<BookCategory> findByName(String name);
}
