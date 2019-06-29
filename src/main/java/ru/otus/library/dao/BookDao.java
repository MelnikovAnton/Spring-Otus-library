package ru.otus.library.dao;

import ru.otus.library.model.Author;
import ru.otus.library.model.Book;
import ru.otus.library.model.Genre;

import java.util.List;
import java.util.Optional;

public interface BookDao {

    int count();
    void insert(Book book);
    Optional<Book> getById(int id);
    List<Book> getAll();
    void delete(Book book);
    List<Book> findByTitle(String title);
    List<Book> getByAuthor(Author author);
    List<Book> getByGenre(Genre genre);

    void addRelations(Book book);
}
