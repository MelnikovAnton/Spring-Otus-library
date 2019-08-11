package ru.otus.library.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import ru.otus.library.model.Author;
import ru.otus.library.repository.custom.AuthorCustomRepository;

public interface AuthorRepository extends ReactiveMongoRepository<Author, String>, AuthorCustomRepository {

    Flux<Author> findByNameContainingIgnoreCase(String name);
}
