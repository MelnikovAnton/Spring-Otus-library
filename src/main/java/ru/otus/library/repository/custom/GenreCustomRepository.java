package ru.otus.library.repository.custom;

import ru.otus.library.model.Genre;

import java.util.List;

public interface GenreCustomRepository {

    List<Genre> findByBookId(String book_id);
}
