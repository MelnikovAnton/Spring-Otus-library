package ru.otus.library.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import ru.otus.library.model.Author;

import java.util.List;

@Repository
public interface AuthorRepository extends MongoRepository<Author,String> {

    List<Author> findByNameContaining(String name);

   // List<Author> findByBookId(String id);
}
