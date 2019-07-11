package ru.otus.library.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
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

}
