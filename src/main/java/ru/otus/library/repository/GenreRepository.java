package ru.otus.library.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import ru.otus.library.model.Genre;
import ru.otus.library.repository.custom.GenreCustomRepository;

public interface GenreRepository extends ReactiveMongoRepository<Genre, String>, GenreCustomRepository {

    Flux<Genre> findByNameContainingIgnoreCase(String name);

}
