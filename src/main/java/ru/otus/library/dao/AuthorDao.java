package ru.otus.library.dao;

import ru.otus.library.model.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorDao {

    int count();
    void insert(Author author);
    Optional<Author> getById(int id);
    List<Author> getAll();
    void delete(Author author);
    List<Author> findByName(String name);
    List<Author> findByBookId(int id);
}
