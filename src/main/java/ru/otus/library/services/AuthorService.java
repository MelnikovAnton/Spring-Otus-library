package ru.otus.library.services;

import ru.otus.library.model.Author;

import java.util.List;

public interface AuthorService {
    Author saveAuthor(Author author);

    List<Author> findAuthorsByName(String name);

    Author findById(int id);

    int delete(Author author);

    List<Author> findAll();
}
