package ru.otus.library.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.library.model.Book;

public interface BookService {
    Mono<Book> saveBook(Book book);

    Flux<Book> findBooksByTitle(String title);

    Flux<Book> findBooksByAuthor(String author);

    Flux<Book> findBooksByGenre(String genre);

    Mono<Book> findById(String id);

    Mono<Void> delete(String bookId);

    Flux<Book> findAll();

    void addRelations(Book book);
}
