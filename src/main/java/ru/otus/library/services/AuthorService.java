package ru.otus.library.services;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import ru.otus.library.model.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorService {
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    Author saveAuthor(Author author);
    @PostFilter("hasPermission(filterObject, 'READ')")
    List<Author> findAuthorsByName(String name);
    @PostFilter("hasPermission(filterObject, 'READ')")
    List<Author> findByBookId(String id);
    @PostAuthorize("hasPermission(returnObject.get(), 'READ')")
    Optional<Author> findById(String id);
    @PreAuthorize("hasPermission(#author,'DELETE')")
    String delete(Author author);
    @PostFilter("hasPermission(filterObject, 'READ')")
    List<Author> findAll();
}
