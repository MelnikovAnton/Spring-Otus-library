package ru.otus.library.services;

import ru.otus.library.model.Book;

import java.util.List;

public interface BookService {
    int saveBook(Book book);
    List<Book> findBooksByTitle(String title);
    List<Book> findBooksByTitleAuthor(String author);
    List<Book> findBooksByTitleGenre(String genre);
}
