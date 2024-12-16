package com.lms.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lms.Entity.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    // Find genres by book title
    List<Genre> findByBooksTitle(String title);
}

