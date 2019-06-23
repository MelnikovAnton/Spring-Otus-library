package ru.otus.library.dao;

import ru.otus.library.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreDao {

    int count();

    Genre insert(Genre genre);

    Optional<Genre> getById(int id);

    List<Genre> getAll();

    int delete(Genre genre);

    List<Genre> findByName(String name);
}
