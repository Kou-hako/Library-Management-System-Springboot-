package com.lms.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lms.Entity.Book;
import com.lms.Entity.Genre;
import com.lms.Repository.AddressRepository;
import com.lms.Repository.AuthorRepository;
import com.lms.Repository.BookRepository;
import com.lms.Repository.GenreRepository;
//Imports
//-------------------------------------------------------
@RestController
@RequestMapping("/genres")
public class GenreController {
 
   public AuthorRepository authorRepository;
    public GenreRepository genreRepository;
    public BookRepository bookRepository;
    public AddressRepository addressRepository;

    public GenreController(AuthorRepository authorRepository, GenreRepository genreRepository, BookRepository bookRepository, AddressRepository addressRepository) {
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.bookRepository = bookRepository;
        this.addressRepository = addressRepository;
    }

    //GET METHODS
    //------------------------------------------------------------------------------------------
    @GetMapping
    public String getAllGenres(){
        String result = "";
        int count = 1;
        for (Genre g : genreRepository.findAll()){
             result += count + ". " + g.getName() + "\nDescription : " + g.getDescription() + "\n";
                    count++;
        }
        result += "------------------------\n";
        return result;
    }

    @GetMapping("/{id}")
    public String getGenresById(@PathVariable Long id){
        String result = "";
        if(genreRepository.existsById(id)){
        Genre g = genreRepository.findById(id).get();
        result += "Genre Name : " + g.getName() + "\nDescription : " + g.getDescription();
        result += "\n--------------------------------------";
        return result;
        }
        else return ("Genre does not exist");
    }
    
    //Find Books by Genre ID Function
     @GetMapping("/BooksByGenreId/{id}")
    public String getBooksByGenreId(@PathVariable Long id)
    {
    String result = "";
    if (genreRepository.existsById(id)){
        Genre g = genreRepository.findById(id).get();
        result += "Genre name : " + g.getName() + "\nDescription : " + g.getDescription()  + ".\nBooks of this Genre : \n";
        int count = 1;
        List<Book> books = g.getBooks();
            if (books.isEmpty()){
            return result + ("There are currently no books of this genre");
            }
            else {
            for(Book b : g.getBooks()){
                    result += count + ". " + b.getTitle() + " by " + b.getAuthor().getName() + "\n";
                    count++;
                    result += "------------------------------------------------------------------\n";
                }
                return result;
            }
    }
    else {
        return ("Genre ID does not exist");
    }
    }
    
    //Find Authors by Genre ID Function
    @GetMapping("/AuthorsByGenreId/{id}")
    public String getAuthorsByGenreId(@PathVariable Long id){
        String result = "";
        if (genreRepository.existsById(id)){
            Genre g = genreRepository.findById(id).get();
            result += "Genre name : " +g.getName() + "\nDescription : " + g.getDescription() + ".\nAuthors with Books of this Genre : \n";
            int count = 1;
            List<Book> books = g.getBooks();
            if (books.isEmpty()){
            return result + ("There are currently no authors of this genre");
            }
            else {
                for(Book b : g.getBooks()){
                    result += count + ". " + b.getAuthor().getName() + "(" + b.getAuthor().getBirthDate() + ")" + "\n";
                    result += "Book Title : " + b.getTitle();
                    count++;
                    result += "\n------------------------------------------------------------------\n";
                }
                return result;
            }
        } else {
            return "Genre ID does not exist";
        }
    }

    //------------------------------------------------------------------------------------------------------------

    //POST METHODS
    //------------------------------------------------------------------------------------------------------------
    @PostMapping
    public ResponseEntity<String> saveGenre(@RequestBody Genre genre){
        if (genre.name.isEmpty()){
        return ResponseEntity.badRequest().body("Name cannot be empty");
        }
        else if (genre.description.isEmpty()){
           return ResponseEntity.badRequest().body("Description cannot be empty");
        }
       for(Genre g : genreRepository.findAll()){
       if (genre.getName().equals(g.getName())){
        return ResponseEntity.badRequest().body("Genre with this name already exists");
       }
       }
       genre.setId(null);
       genreRepository.save(genre);
       return ResponseEntity.ok("SUCCESSFUL");
       }

       //---------------------------------------------------------------------------------------------------------

       //DELETE METHODS
       //---------------------------------------------------------------------------------------------------------
       @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGenre(@PathVariable Long id) {
        if(genreRepository.existsById(id)){
            List<Book> books = bookRepository.findByGenresId(id);
            for(Book b : books){
                b = bookRepository.findByGenresId(id).get(0);
                Genre genre = genreRepository.findById(id).get();
                b.removeGenre(genre);
                bookRepository.save(b);
            }
            genreRepository.deleteById(id);

            return ResponseEntity.ok("SUCCESSFUL");
        } else {
             return ResponseEntity.badRequest().body("Genre ID is incorrect");
        }
    }

    //------------------------------------------------------------------------------------------------------------

    //PUT METHODS
    //------------------------------------------------------------------------------------------------------------
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateGenre(@PathVariable Long id, @RequestBody Genre genre){
        if (genreRepository.existsById(id)){
        if (isValidGenre(genre)){
         genre.setId(id);
         genreRepository.save(genre);
         return ResponseEntity.ok("SUCCESSFUL");
        }
        else return ResponseEntity.badRequest().body("The genre object definition is incorrect");
        }
        return ResponseEntity.badRequest().body("Genre ID does not exist");
    }

    //-------------------------------------------------------------------------------------------------------------

    //DATA AUTHENTICATION FUNCTIONS

    private boolean isValidGenre(Genre genre){
        if (genre.getName().isEmpty()){
            return false;
        }
        else if (genre.getDescription().isEmpty()){
            return false;
        }
        else return true;
    }


}
