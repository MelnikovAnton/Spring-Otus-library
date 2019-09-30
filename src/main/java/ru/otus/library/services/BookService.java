package ru.otus.library.services;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import ru.otus.library.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    Book saveBook(Book book);
    @PostFilter("hasPermission(filterObject, 'READ')")
    List<Book> findBooksByTitle(String title);
    @PostFilter("hasPermission(filterObject, 'READ')")
    List<Book> findBooksByAuthor(String author);
    @PostFilter("hasPermission(filterObject, 'READ')")
    List<Book> findBooksByGenre(String genre);
    @PostAuthorize("hasPermission(returnObject.get(), 'READ')")
    Optional<Book> findById(String id);
    @PreAuthorize("hasPermission(#book,'DELETE')")
    String delete(Book book);
    @PostFilter("hasPermission(filterObject, 'READ')")
    List<Book> findAll();

    void addRelations(Book book);
}
