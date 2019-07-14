package ru.otus.library.repository.custom;

public interface BookCustomRepository {

    void removeAuthorById(String id);
    void removeGenreById(String id);
}
