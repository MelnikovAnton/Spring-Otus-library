package ru.otus.library.services;

import ru.otus.library.model.Book;

import java.util.List;

public interface BookService {
    Book saveBook(Book book);
    List<Book> findBooksByTitle(String title);
    List<Book> findBooksByAuthor(String author);
    List<Book> findBooksByGenre(String genre);
    Book findById(int id);
    int delete(Book book);
    List<Book> findAll();
}
