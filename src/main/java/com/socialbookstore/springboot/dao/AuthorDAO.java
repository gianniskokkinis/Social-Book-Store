package com.socialbookstore.springboot.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.socialbookstore.springboot.model.Author;
import com.socialbookstore.springboot.model.Book;

@Repository
public interface AuthorDAO extends JpaRepository<Author, Integer>{
	public Author findById(int id);
	public Optional<Author> findByName(String Name);
}
