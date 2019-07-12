package ru.otus.library.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.otus.library.model.Author;
import ru.otus.library.model.Book;
import ru.otus.library.model.Genre;

import java.util.List;

@Repository
public interface BookRepository extends MongoRepository<Book, String> {

    List<Book> findByTitleContaining(String title);

    List<Book> findByAuthorsContains(Author author);

    List<Book> findByGenresContains(Genre genre);

    List<Book> findByGenresContains(List<Genre> genre);

    List<Book> findByAuthorsContains(List<Author> author);

    @Query("{'authors.name':{ '$regex' : :#{#name}, '$options' : 'i' }}")
    List<Book> findByAuthorsNameContains(@Param("name") String authorName);

    @Query("{'genres.name':{ '$regex' : :#{#name}, '$options' : 'i' }}")
    List<Book> findByGenresNameContains(@Param("name") String genre);

}
