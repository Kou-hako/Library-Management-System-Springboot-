package com.lms.Controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.lms.Entity.Genre;
import com.lms.Repository.BookRepository;
import com.lms.Repository.GenreRepository;

import org.apache.catalina.connector.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.lms.Entity.Address;
import com.lms.Entity.Author;
import com.lms.Entity.Book;
import com.lms.Repository.AddressRepository;
import com.lms.Repository.AuthorRepository;

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

    @GetMapping("/getBooks")
    public String getAllBooks(){
        String result = "";
        for (Book b : bookRepository.findAll()){
            String a = b.getAuthor().name;
            result += b.getId() + ": " + b.getTitle() + " by " + a + ".\nGenres : \n"; 
            int count = 1;
            for(Genre g : b.getGenres()){
                    result += count + ". " + g.getName() + "\n";
                    count++;
                }
            result += "----------------\n";
        }
        return result;
        }

     @GetMapping("/getBooks/{id}")
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

    @GetMapping("/getGenres")
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

    @GetMapping("/getGenres/{id}")
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

    @GetMapping("/getBooksByGenre/{id}")
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
           
    @PostMapping("/saveBook")
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

     @PostMapping("/saveAuthor")
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

     @PostMapping("/saveGenre")
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

       //UPDATE ADDRESS By AuthorID
    @PutMapping("/updateAddress/{id}")
    public ResponseEntity<String> updateAddress(@PathVariable Long id, @RequestBody Address address){
        if (addressRepository.existsById(id)){
        if (isValidAddress(address)){
            address.setId(id);
            Author author = authorRepository.findByAddressId(id).get();
            address.setAuthor(author);
            addressRepository.save(address);
            return ResponseEntity.badRequest().body("SUCCESSFUL");
        }
        else return ResponseEntity.badRequest().body("The address object definition is incorrect");
        }
        else return ResponseEntity.badRequest().body("Address ID does not exist");
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
            return ResponseEntity.badRequest().body("This genre is already added to this book");
        }
        }   
       b.addGenre(g);
       bookRepository.save(b);
       return ResponseEntity.ok("SUCCESSFUL");
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
       //THINK OF MORE FUNCTIONS LATER
    

    @DeleteMapping("/deleteAuthor/{id}")
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

    @DeleteMapping("/deleteGenre/{id}")
    public ResponseEntity<String> deleteGenre(@PathVariable Long id) {
        if(genreRepository.existsById(id)){
            List<Book> books = bookRepository.findByGenresId(id);
            for(Book b : books){
                Book book = bookRepository.findByGenresId(id).get(0);
                Genre genre = genreRepository.findById(id).get();
                book.removeGenre(genre);
                bookRepository.save(book);
            }
            genreRepository.deleteById(id);

            return ResponseEntity.ok("SUCCESSFUL");
        } else {
             return ResponseEntity.badRequest().body("Genre ID is incorrect");
        }
    }

      @DeleteMapping("/deleteBook/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        if(bookRepository.existsById(id)){
            Author author = authorRepository.findByBooksId(id).get();
            author.removeBook(bookRepository.findById(id).get());
            authorRepository.save(author);
            bookRepository.deleteById(id);
            return ResponseEntity.ok("SUCCESSFUL");
        } else {
             return ResponseEntity.badRequest().body("ID is incorrect");
        }
    }

    @PutMapping("/updateAuthor/{id}")
    public ResponseEntity<String> updateAuthor(@PathVariable Long id, @RequestBody Author author) {
        if(authorRepository.existsById(id)) {
            if (isValidAuthor(author)) {
                author.setId(id);
                author.getAddress().setId(id);
                Address a = addressRepository.findByAuthorId(id).get();
                addressRepository.delete(a);
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

       @PutMapping("/updateGenre/{id}")
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

    // @PutMapping("/updateBook/{id}")
    // public ResponseEntity<String> updateBook(@PathVariable Long id, @RequestBody Book book) {
    //     if(bookRepository.existsById(id)) {    
    //         if (isValidAuthor(book.getAuthor())){
    //         Author author = book.getAuthor();
            
    //         ArrayList<Genre> genres = new ArrayList<>();
    //         for(Genre g : book.getGenres()){
    //             Long genreId = g.getId();
    //             if(genreRepository.existsById(genreId)){
    //                 Genre genre = genreRepository.findById(genreId).get();
    //                 genres.add(genre);
    //             }else{
    //                 return ResponseEntity.badRequest().body("Genre ID does not exist");
    //             }
    //         }
    //        Book newBook = bookRepository.findById(id).get();
    //        newBook.setAuthor(author);
    //        newBook.setGenres(genres);
    //        bookRepository.save(newBook);
    //         return ResponseEntity.ok("SUCCESSFUL");
    //     }   else {
    //         return ResponseEntity.badRequest().body("The author object definition is incorrect");
    //     }
    //         } else {
    //             return ResponseEntity.badRequest().body("Book ID does not exist");
    //         }
    //     }
    
 



    private boolean isValidAuthor(Author author){
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


