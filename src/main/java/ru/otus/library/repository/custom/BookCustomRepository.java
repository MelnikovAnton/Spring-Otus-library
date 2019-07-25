package ru.otus.library.repository.custom;

import ru.otus.library.model.Author;
import ru.otus.library.model.Genre;

public interface BookCustomRepository {

    void removeAuthorById(String id);

    void removeGenreById(String id);

    void updateAuthorName(Author author);

    void updateGenreName(Genre genre);
}
