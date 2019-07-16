package ru.otus.library.services;

import ru.otus.library.model.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorService {
    Author saveAuthor(Author author);

    List<Author> findAuthorsByName(String name);

    List<Author> findByBookId(String id);

    Optional<Author> findById(String id);

    String delete(Author author);

    List<Author> findAll();
}
