package ru.otus.library.repository.custom;

import ru.otus.library.model.Author;

import java.util.List;

public interface AuthorCustomRepository {

    List<Author> findByBookId(String BookId);
}
