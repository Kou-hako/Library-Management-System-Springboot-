package com.lms.Entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
//Imports
//-------------------------------------------------------------

@AllArgsConstructor
@NoArgsConstructor

@Entity
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    
    @Column(name = "name")
    public String name;
    @Column(name = "birthDate")
    public String birthDate;

    @OneToOne(mappedBy = "author", cascade = CascadeType.ALL)
    public Address address;
    
   
    @OneToMany(mappedBy = "author", fetch = FetchType.EAGER)
    public List<Book> books;

    public Author(String name , String birthDate, Address address) {
        this.name = name;
        this.birthDate = birthDate;
        this.address = address;
        this.address.setAuthor(this);
        books = new ArrayList<>();
    }

    // Additional feature: Method to add a book to the author
    public void addBook(Book book) {
        this.books.add(book);
        book.setAuthor(this);
    }

    // Additional feature: Method to remove a book from the author
    public void removeBook(Book book) {
        this.books.remove(book);
        book.setAuthor(null);
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public Address getAddress() {
        return address;
    }
    
    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
