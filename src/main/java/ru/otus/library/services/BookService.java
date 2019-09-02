package ru.otus.library.services;

import org.springframework.security.access.prepost.PostFilter;
import ru.otus.library.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Book saveBook(Book book);

    List<Book> findBooksByTitle(String title);

    List<Book> findBooksByAuthor(String author);

    List<Book> findBooksByGenre(String genre);

    Optional<Book> findById(String id);

    String delete(Book book);
    @PostFilter("hasPermission(filterObject, 'READ')")
    List<Book> findAll();

    void addRelations(Book book);
}
