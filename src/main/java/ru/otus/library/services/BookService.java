package ru.otus.library.services;

import ru.otus.library.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Book saveBook(Book book);
    List<Book> findBooksByTitle(String title);
    List<Book> findBooksByAuthor(String author);
    List<Book> findBooksByGenre(String genre);
    Optional<Book> findById(long id);
    long delete(Book book);
    List<Book> findAll();
    void addRelations(Book book);
}
