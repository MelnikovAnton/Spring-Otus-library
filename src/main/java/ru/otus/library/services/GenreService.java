package ru.otus.library.services;

import ru.otus.library.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreService {
    Genre saveGenre(Genre genre);

    List<Genre> findGenresByName(String name);

    List<Genre> findAll();

    Optional<Genre> findById(int id);

    int delete(Genre genre);

}
