package com.lms.Controller;

import java.time.LocalDate;
import java.util.List;

import com.lms.Entity.Genre;
import com.lms.Repository.BookRepository;
import com.lms.Repository.GenreRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.lms.Entity.Address;
import com.lms.Entity.Author;
import com.lms.Entity.Book;
import com.lms.Repository.AddressRepository;
import com.lms.Repository.AuthorRepository;
//Imports
//-------------------------------------
@RestController
@RequestMapping("/authors")
public class AuthorController {
    public AuthorRepository authorRepository;
    public GenreRepository genreRepository;
    public BookRepository bookRepository;
    public AddressRepository addressRepository;

    public AuthorController(AuthorRepository authorRepository, GenreRepository genreRepository, BookRepository bookRepository, AddressRepository addressRepository) {
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.bookRepository = bookRepository;
        this.addressRepository = addressRepository;
    }

    //GET METHODS
    //--------------------------------------------------------------------------------------------------------

    @GetMapping
    public String getAllAuthors() {
        String result = "";
        for (Author a : authorRepository.findAll()) {
            result += a.getId() + ". " + a.getName() + "(" + a.getBirthDate() + ")" + ", Address : " + a.getAddress().adress + ". " + "\nBooks:\n";
            for (Book b : a.getBooks()) {
                int count = 1;
                result += b.getTitle() + "\nGenres : \n";
                for(Genre g : b.getGenres()){
                    result += count + ". " + g.getName() + "\n";
                    count++;
                }
            }
            result += "--------------------------------------------------------------------\n";
        }
        return result;
    }

    @GetMapping("/{id}")
    public String getAuthorsById(@PathVariable Long id){
        String result = "";
        if(authorRepository.existsById(id)){
        Author a = authorRepository.findById(id).get();
        result += "Author name : " + a.getName() + ", born on " + a.getBirthDate();
        result += "\n-----------------------------------------------\nBooks : \n";
        int counter = 1;
        for (Book b : a.getBooks()) {
                int count = 1;
                result += counter + ". " + b.getTitle() + "\nGenres : \n";
                counter++;
                for(Genre g : b.getGenres()){
                    result += count + ". " + g.getName() + "\n";
                    count++;
                }
                result += "\n";
            }
            result += "--------------------------------------------------------------------\n";

        return result;
        }
        else return ("Author does not exist");
    }

    //---------------------------------------------------------------------------------------------------------
    
    //POST METHODS
    //---------------------------------------------------------------------------------------------------------     
     @PostMapping
    public ResponseEntity<String> saveAuthor(@RequestBody Author author) {
        author.setId(null);
        if(!isValidAuthor(author)){
            return ResponseEntity.badRequest().body("Invalid input");
        }
        else {
            author.getAddress().setAuthor(author);
            authorRepository.save(author);
            return ResponseEntity.ok("SUCCESSFUL");
        }
    }
       
      //--------------------------------------------------------------------------------------------------------

      //DELETE METHODS
      //--------------------------------------------------------------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAuthor(@PathVariable Long id) {
        if(authorRepository.existsById(id)){
            List<Book> books = bookRepository.findByAuthorId(id);
            for(Book b : books){
                bookRepository.deleteById(b.getId());
            }
            Address a = addressRepository.findByAuthorId(id).get();
            addressRepository.delete(a);
            authorRepository.deleteById(id);

            return ResponseEntity.ok("SUCCESSFUL");
        } else {
             return ResponseEntity.badRequest().body("Author ID is incorrect");
        }
    }

    //----------------------------------------------------------------------------------------------------

    //PUT METHODS
    //----------------------------------------------------------------------------------------------------
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateAuthor(@PathVariable Long id, @RequestBody Author author) {
        if(authorRepository.existsById(id)) {
            if (isValidAuthor(author)) {
                author.setId(id);
                Long existingAddressId = addressRepository.findByAuthorId(id).get().getId();
                author.getAddress().setId(existingAddressId);
                author.getAddress().setAuthor(author);
                authorRepository.save(author);
                return ResponseEntity.ok("SUCCESSFUL");
            } else {
                return ResponseEntity.badRequest().body("Invalid input");
            }
        } else {
             return ResponseEntity.badRequest().body("Author ID is incorrect");
        }
    }

    //--------------------------------------------------------------------------------------------------------

    //DATA AUTHENTICATION FUNCTIONS

    static boolean isValidAuthor(Author author){
        if(author.getName().isEmpty()){
            return false;
        }
        else if (author.getAddress().adress.isEmpty()){
            return false;
        }
        try {
            String birthdate = author.getBirthDate();
            LocalDate.parse(birthdate);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

   
}


