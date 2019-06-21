package ru.otus.library.dao;

import ru.otus.library.model.Author;
import ru.otus.library.model.Book;
import ru.otus.library.model.Genre;

import java.util.List;

public interface BookDao {

    int count();
    int insert(Book book);
    Book getById(int id);
    List<Book> getAll();
    int delete(Book book);
    List<Book> findByTitle(String title);
    List<Book> getByAuthor(Author author);
    List<Book> getByGenre(Genre genre);
}
