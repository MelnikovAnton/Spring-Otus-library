package ru.otus.library.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.otus.library.model.Genre;

import java.util.List;


public interface GenreRepository extends MongoRepository<Genre, String> {

    List<Genre> findByNameContaining(String name);

  //  List<Genre> findByBookId(String id);
}
