package com.lms.Entity;

import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
     
    @Column(name = "title")
    public String title;

    @ManyToOne
    @JoinColumn(name = "author_id")
    public Author author;

    @ManyToMany
    @JoinTable(
      name = "book_genre",
      joinColumns = @JoinColumn(name = "book_id"),
      inverseJoinColumns = @JoinColumn(name = "genre_id"))
    public List<Genre> genres;

    public Book(String title, Author author, List<Genre> genres){
        this.title = title;
        this.author = author;
        author.addBook(this);
        this.genres = genres;
    }

    // Additional feature: Method to add a genre to the book
    public void addGenre(Genre genre) {
        this.genres.add(genre);
        genre.getBooks().add(this);
    }

    // Additional feature: Method to remove a genre from the book
    public void removeGenre(Genre genre) {
        this.genres.remove(genre);
        genre.getBooks().remove(this);
    }

    public String toString(){
        return "Book: " + this.title + " by " + (this.author==null?"Unknown":this.author.name);
    }

}
