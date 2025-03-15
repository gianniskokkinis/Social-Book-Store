package com.socialbookstore.springboot.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.socialbookstore.springboot.model.Book;


@Repository
public interface BookDAO extends JpaRepository<Book, Integer>{
	public Book findById(int id);
	public Optional<Book> findByTitle(String Title);
}
