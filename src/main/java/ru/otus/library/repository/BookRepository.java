package ru.otus.library.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import ru.otus.library.model.Author;
import ru.otus.library.model.Book;
import ru.otus.library.model.Genre;
import ru.otus.library.repository.custom.BookCustomRepository;

import java.util.List;

public interface BookRepository extends MongoRepository<Book, String>, BookCustomRepository {

    List<Book> findByTitleContaining(String title);

    List<Book> findByAuthorsContains(Author author);

    List<Book> findByGenresContains(Genre genre);

    @Query("{'authors.name':{ '$regex' : :#{#name}, '$options' : 'i' }}")
    List<Book> findByAuthorsNameContains(@Param("name") String authorName);

    @Query("{'genres.name':{ '$regex' : :#{#name}, '$options' : 'i' }}")
    List<Book> findByGenresNameContains(@Param("name") String genre);

}
