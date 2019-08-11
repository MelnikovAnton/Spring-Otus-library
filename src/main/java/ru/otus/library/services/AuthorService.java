package ru.otus.library.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.library.model.Author;

public interface AuthorService {
    Mono<Author> saveAuthor(Author author);

    Flux<Author> findAuthorsByName(String name);

    Flux<Author> findByBookId(String id);

    Mono<Author> findById(String id);

    Mono<Void> delete(String id);

    Flux<Author> findAll();
}
