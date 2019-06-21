package ru.otus.library.dao;

import ru.otus.library.model.Author;

import java.util.List;

public interface AuthorDao {

    int count();
    int insert(Author author);
    Author getById(int id);
    List<Author> getAll();
    int delete(Author author);
    List<Author> findByName(String name);
}
