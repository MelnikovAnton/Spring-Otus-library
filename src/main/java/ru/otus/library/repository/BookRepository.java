package ru.otus.library.repository;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.library.model.Author;
import ru.otus.library.model.Book;
import ru.otus.library.model.Genre;
import ru.otus.library.repository.custom.BookCustomRepository;

public interface BookRepository extends ReactiveMongoRepository<Book, String>, BookCustomRepository {

    Flux<Book> findByTitleContaining(String title);

    Flux<Book> findByAuthorsContains(Mono<Author> author);

    Flux<Book> findByGenresContains(Mono<Genre> genre);

    @Query("{'authors.name':{ '$regex' : :#{#name}, '$options' : 'i' }}")
    Flux<Book> findByAuthorsNameContains(@Param("name") String authorName);

    @Query("{'genres.name':{ '$regex' : :#{#name}, '$options' : 'i' }}")
    Flux<Book> findByGenresNameContains(@Param("name") String genre);

}
