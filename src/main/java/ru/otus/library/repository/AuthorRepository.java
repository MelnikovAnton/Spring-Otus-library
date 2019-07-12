package ru.otus.library.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.library.model.Author;
import ru.otus.library.repository.custom.AuthorCustomRepository;

import java.util.List;

public interface AuthorRepository extends MongoRepository<Author, String>, AuthorCustomRepository {

    List<Author> findByNameContainingIgnoreCase(String name);
}
