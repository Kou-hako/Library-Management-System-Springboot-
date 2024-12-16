package com.lms.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lms.Entity.Address;
import com.lms.Entity.Author;
import com.lms.Repository.AddressRepository;
import com.lms.Repository.AuthorRepository;
import com.lms.Repository.BookRepository;
import com.lms.Repository.GenreRepository;

//Imports
//--------------------------------------------------------
@RestController
@RequestMapping("/address")
public class AddressController {
    public AuthorRepository authorRepository;
    public GenreRepository genreRepository;
    public BookRepository bookRepository;
    public AddressRepository addressRepository;

    public AddressController(AuthorRepository authorRepository, GenreRepository genreRepository, BookRepository bookRepository, AddressRepository addressRepository) {
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
        this.bookRepository = bookRepository;
        this.addressRepository = addressRepository;
    }

    // @Autowired
    // AuthorRepository authorRepository;
    // @Autowired
    // BookRepository bookRepository;
    // @Autowired
    // AddressRepository addressRepository;
    // @Autowired
    // GenreRepository genreRepository;

    //GET METHODS
    //--------------------------------------------------------------------------------------------------------
    @GetMapping
    public String getAllAddress(){
        String result = "";
        for (Address a : addressRepository.findAll()){
            result += a.getId() + ": " + a.getAdress()  + ", Current Residence Of : " +a.getAuthor().getName() + "\n-----------------------------------------------------------------------------\n"; 
        }
        return result;
        }

    //GET ADDRESS BY AUTHOR_ID METHOD
    @GetMapping("/{id}")
    public String getAddressByAuthorId(@PathVariable Long id){
      if(authorRepository.existsById(id)){
      Author a = authorRepository.findById(id).get();
      String result = "";
      String address = a.getAddress().getAdress();
      result += ("Author ID : " + a.getId() + "\n" + "Address : " + address + "\n" + "Name : " + a.getName());
      return result; 
      }
      else {
        return ("Author ID is incorrect");
      }
    }



    //PUT METHODS
    //--------------------------------------------------------------------------------------------------------
    //UPDATE ADDRESS By AuthorID
    @PutMapping("/updateAddress/{id}")
    public ResponseEntity<String> updateAddress(@PathVariable Long id, @RequestBody Address address){
        if (authorRepository.existsById(id)){
        if (isValidAddress(address)){
            address.setId(id);
            Author author = authorRepository.findByAddressId(id).get();
            address.setAuthor(author);
            addressRepository.save(address);
            return ResponseEntity.ok("SUCCESSFUL");
        }
        else return ResponseEntity.badRequest().body("The address object definition is incorrect");
        }
        else return ResponseEntity.badRequest().body("Address ID does not exist");
    }
    //--------------------------------------------------------------------------------------------------------

    //--------------------------------------------------------------------------------------------------------
    //DATA AUTHENTICATION FUNCTIONS
    private boolean isValidAddress(Address address){
      if (address.getAdress().isEmpty()){
          return false;
      }
      else return true;
  }
   
}
 