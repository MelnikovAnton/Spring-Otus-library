package ru.otus.library.dao;

import ru.otus.library.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreDao {

    int count();

    void insert(Genre genre);

    Optional<Genre> getById(int id);

    List<Genre> getAll();

    void delete(Genre genre);

    List<Genre> findByName(String name);

    List<Genre> findByBookId(int id);
}
