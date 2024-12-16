package com.lms.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lms.Entity.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    //Find authors by book title
    List<Author> findByBooksTitle(String title);
    //Find author by book id
    Optional<Author> findByBooksId(Long id);
    //Find author by address id
    Optional<Author> findByAddressId(Long id);
}
