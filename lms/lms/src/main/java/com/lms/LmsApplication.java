package com.lms;

import com.lms.Entity.Address;
import com.lms.Entity.Author;
import com.lms.Entity.Book;
import com.lms.Entity.Genre;
import com.lms.Repository.AddressRepository;
import com.lms.Repository.AuthorRepository;
import com.lms.Repository.BookRepository;
import com.lms.Repository.GenreRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class LmsApplication {

	public static void main(String[] args) {
		SpringApplication.run(LmsApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(AuthorRepository ar, BookRepository br, GenreRepository gr, AddressRepository addRep) {
		return (runner) -> {
			Genre g1 = new Genre("Fantasy", "Fantasy is a genre of speculative fiction set in a fictional universe, often inspired by real world myth and folklore.");
			gr.save(g1);
			Genre g2 = new Genre("Science Fiction", "Science fiction is a genre of speculative fiction that typically deals with imaginative and futuristic concepts such as advanced science and technology, space exploration, time travel, parallel universes, and extraterrestrial life.");
			gr.save(g2);
			Genre g3 = new Genre("Horror", "Horror is a genre of speculative fiction which is intended to frighten, scare, or disgust.");
			gr.save(g3);
			Genre g4 = new Genre("Thriller", "Thriller is a genre of fiction, having numerous, often overlapping subgenres. Thrillers are characterized and defined by the moods they elicit, giving viewers heightened feelings of suspense, excitement, surprise, anticipation and anxiety.");
			gr.save(g4);
			Genre g5 = new Genre("Mystery", "Mystery fiction is a genre of fiction usually involving a mysterious death or a crime to be solved. Often with a closed circle of suspects, each suspect is usually provided with a credible motive and a reasonable opportunity for committing the crime.");
			gr.save(g5);
      
			Address a1Address = new Address("Cennet Mahallesi");
			Address a2Address = new Address ("Besiktas");
			Address a3Address = new Address ("Basaksehir");
			Author a1 = new Author("Bilal", "1990-08-01", a1Address);
			Author a2 = new Author("Ulvi", "1995-01-01", a2Address);
			Author a3 = new Author("Abdullah","1980-01-01", a3Address);
			ar.save(a1);
			ar.save(a2);
			ar.save(a3);

			Book b1 = new Book("Book1", a1, List.of(g1, g2));
			Book b2 = new Book("Book2", a1, List.of(g3, g4));
			Book b3 = new Book("Book3", a2, List.of(g5, g1));
			Book b4 = new Book("Book4", a3, List.of(g2, g3));
			Book b5 = new Book("Book5", a3, List.of(g4, g5));

			br.save(b1);
			br.save(b2);
			br.save(b3);
			br.save(b4);
			br.save(b5);
		};
	}

}
