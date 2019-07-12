package ru.otus.library.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.otus.library.model.Genre;
import ru.otus.library.repository.custom.GenreCustomRepository;

import java.util.List;


public interface GenreRepository extends MongoRepository<Genre, String>, GenreCustomRepository {

    List<Genre> findByNameContainingIgnoreCase(String name);

}
