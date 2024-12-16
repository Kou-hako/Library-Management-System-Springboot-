package com.lms.Controller;

import java.util.ArrayList;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.lms.Entity.Author;
import com.lms.Entity.Book;
import com.lms.Entity.Genre;
import com.lms.Repository.AddressRepository;
import com.lms.Repository.AuthorRepository;
import com.lms.Repository.BookRepository;
import com.lms.Repository.GenreRepository;
//Imports
//-------------------------------------------------------------
@RestController
@RequestMapping("/books")
public class BookController {
 
   public AuthorRepository authorRepository;
    public GenreRepository genreRepository;
    public BookRepository bookRepository;
    public AddressRepository addressRepository;

    public BookController(AuthorRepository authorRepository, GenreRepository genreRepository, BookRepository bookRepository, AddressRepository addressRepository) {
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.bookRepository = bookRepository;
        this.addressRepository = addressRepository;
    }

    //GET METHODS
    //------------------------------------------------------------------------------------------
    @GetMapping
    public String getAllBooks(){
        String result = "";
        for (Book b : bookRepository.findAll()){
            result += b.getId() + ": " + b.getTitle() + " by " + b.getAuthor().getName() + ".\nGenres : \n"; 
            int count = 1;
            for(Genre g : b.getGenres()){
                    result += count + ". " + g.getName() + "\n";
                    count++;
                }
            result += "----------------\n";
        }
        return result;
        }

        @GetMapping("/{id}")
    public String getBooksById(@PathVariable Long id){
        String result = "";
        if(bookRepository.existsById(id)){
        Book b = bookRepository.findById(id).get();
        result += "Book name : " + b.getTitle() + " by " + b.getAuthor().getName() + ".\nGenres : \n";
        int count = 1;
            for(Genre g : b.getGenres()){
                    result += count + ". " + g.getName() + "\n";
                    count++;
                }
        result += "\n-----------------------------------------------";
        return result;
        }
        else return ("Book does not exist");
    }

    //--------------------------------------------------------------------------------------------------------

    //POST METHODS
    //--------------------------------------------------------------------------------------------------------
    @PostMapping
    public ResponseEntity<String> saveBook(@RequestBody Book book) {
        Long authorId = book.getAuthor().getId();
        if(authorRepository.existsById(authorId)){
            Author author = authorRepository.findById(authorId).get();

            ArrayList<Genre> genres = new ArrayList<>();
            for(Genre g : book.getGenres()){
                Long genreId = g.getId();
                if(genreRepository.existsById(genreId)){
                    Genre genre = genreRepository.findById(genreId).get();
                    genres.add(genre);
                }else{
                    return ResponseEntity.badRequest().body("Genre ID does not exist");
                }
            }

            Book newBook = new Book(book.getTitle(), author, genres);
            bookRepository.save(newBook);
            return ResponseEntity.ok("SUCCESSFUL");

        } else {
            return ResponseEntity.badRequest().body("Author ID does not exist");
        }
    }

     //addGenre to Book Function
     @PostMapping("/addGenre/{genreId}/toBook/{bookId}")
     public ResponseEntity<String> addGenre(@PathVariable Long genreId, @PathVariable Long bookId){
      if(!genreRepository.existsById(genreId)){
      return ResponseEntity.badRequest().body("Genre ID does not exist");
      }
      else if(!bookRepository.existsById(bookId)){
          return ResponseEntity.badRequest().body("Book ID does not exist");
      }
      Genre g = genreRepository.findById(genreId).get();
      Book b = bookRepository.findById(bookId).get();
      for (Genre genre : bookRepository.findById(bookId).get().getGenres()){
          if (genre.getName().equals(g.getName())){
             return ResponseEntity.badRequest().body("This genre is already included in book");
          }
              b.addGenre(g);
              bookRepository.save(b);
              return ResponseEntity.ok("SUCCESSFUL");
      }
      return ResponseEntity.badRequest().body("Genre is not included in the book");
  }

       //removeGenre from Book Function
       @PostMapping("/removeGenre/{genreId}/fromBook/{bookId}")
       public ResponseEntity<String> removeGenre(@PathVariable Long genreId, @PathVariable Long bookId){
        if(!genreRepository.existsById(genreId)){
        return ResponseEntity.badRequest().body("Genre ID does not exist");
        }
        else if(!bookRepository.existsById(bookId)){
            return ResponseEntity.badRequest().body("Book ID does not exist");
        }
        Genre g = genreRepository.findById(genreId).get();
        Book b = bookRepository.findById(bookId).get();
        for (Genre genre : bookRepository.findById(bookId).get().getGenres()){
            if (genre.getName().equals(g.getName())){
                b.removeGenre(g);
                bookRepository.save(b);
                return ResponseEntity.ok("SUCCESSFUL");
            }
        }
        return ResponseEntity.badRequest().body("Genre is not included in the book");
    }

     //addBook to Author Function
     @PostMapping("/addBook/{bookId}/toAuthor/{authorId}")
     public ResponseEntity<String> addBook(@PathVariable Long bookId, @PathVariable Long authorId){
         if(!bookRepository.existsById(bookId)){
            return ResponseEntity.badRequest().body("Book ID does not exist");
         }
         else if (!authorRepository.existsById(authorId)){
             return ResponseEntity.badRequest().body("Author ID does not exist");
         }
         else {
             Author a = authorRepository.findById(authorId).get();
             Author tempAuthor = authorRepository.findByBooksId(bookId).get();
             Book b = bookRepository.findById(bookId).get();
             a.addBook(b);
             tempAuthor.removeBook(b);
             authorRepository.save(tempAuthor);
             b.setAuthor(a);
             bookRepository.save(b);
             authorRepository.save(a);
             return ResponseEntity.ok("SUCCESSFUL");
         }
         }

    //------------------------------------------------------------------------------------------------------

    //DELETE METHODS
    //------------------------------------------------------------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        if(bookRepository.existsById(id)){
            Author author = authorRepository.findByBooksId(id).get();
            Book b = bookRepository.findById(id).get();
            for(Genre g : b.getGenres()){
                b.removeGenre(g);
            }
            author.removeBook(bookRepository.findById(id).get());
            authorRepository.save(author);
            bookRepository.deleteById(id);
            return ResponseEntity.ok("SUCCESSFUL");
        } else {
             return ResponseEntity.badRequest().body("ID is incorrect");
        }
    }

    //------------------------------------------------------------------------------------------------------
    

    //PUT METHODS
    //------------------------------------------------------------------------------------------------------
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateBook(@PathVariable Long id, @RequestBody Book book) {
        if(bookRepository.existsById(id)){
            Book b = bookRepository.findById(id).get();
            b.setTitle(book.getTitle());
            Author newAuthor = book.getAuthor();
            try{
                if (newAuthor != null && authorRepository.existsById(newAuthor.getId())) {
                    newAuthor = authorRepository.findById(newAuthor.getId()).get();
                    newAuthor.addBook(b);
                    authorRepository.save(newAuthor);
                }
                if (book.getGenres() != null) {
                    b.setGenres(new ArrayList<>());
                    for (Genre g : book.getGenres()) {
                        if (genreRepository.existsById(g.getId())) {
                            Genre genre = genreRepository.findById(g.getId()).get();
                            b.addGenre(genre);
                        }
                    }
                }
            }catch (Exception e){
                return ResponseEntity.badRequest().body("Bad Author or Genre Objects");
            }

            bookRepository.save(b);
            return ResponseEntity.ok("SUCCESSFUL");
        } else {
            return ResponseEntity.badRequest().body("Book ID is incorrect");
        }
    }
}
