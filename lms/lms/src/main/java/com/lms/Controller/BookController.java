package com.lms.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lms.Entity.Book;
import com.lms.Repository.BookRepository;

@RestController
@RequestMapping("/books")
public class BookController {
    public BookRepository bookRep;

    public BookController(BookRepository bookRep) {
        this.bookRep = bookRep;
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return bookRep.findAll();
    }

    @GetMapping("/byAuthor")
    public List<Book> getBooksByAuthor(@RequestParam String authorName) {
        return bookRep.findByAuthorName(authorName);
    }

    @GetMapping("/byGenre")
    public List<Book> getBooksByGenre(@RequestParam String genreName) {
        return bookRep.findByGenresName(genreName);
    }
    
    @PostMapping
    public ResponseEntity<String> createBook(@RequestBody Book book){
    
    return ResponseEntity.ok("Book added");
    }
    
}
