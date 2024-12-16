package com.lms.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lms.Entity.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
    //Find books by author's name
    List<Book> findByAuthorName(String authorName);

    //Find books by genre name
    List<Book> findByGenresName(String genreName);

    //Find books by genre id
    List<Book> findByAuthorId(Long authorId);

    //Find books by genre id
    List<Book> findByGenresId(Long genreId);
}

