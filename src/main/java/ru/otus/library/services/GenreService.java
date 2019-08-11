package ru.otus.library.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.library.model.Genre;

import java.util.List;

public interface GenreService {
    Mono<Genre> saveGenre(Genre genre);

    Flux<Genre> findGenresByName(String name);

    Flux<Genre> findByBookId(String id);

    Flux<Genre> findAll();

    Mono<Genre> findById(String id);

    Mono<Void> delete(String id);

}
