package com.lms.Controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lms.Entity.Genre;
import com.lms.Repository.GenreRepository;

@RestController
@RequestMapping("/genres")
public class GenreController {
    public GenreRepository genreRepository;

    public GenreController(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @GetMapping
    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    @GetMapping("/byBook")
    public List<Genre> getGenresByBook(@RequestParam String title) {
        return genreRepository.findByBooksTitle(title);
    }


}
