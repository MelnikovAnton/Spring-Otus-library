package ru.otus.library.dao;

import ru.otus.library.model.Genre;

import java.util.List;

public interface GenreDao {

    int count();

    Genre insert(Genre genre);

    Genre getById(int id);

    List<Genre> getAll();

    int delete(Genre genre);

    List<Genre> findByName(String name);
}
