package ru.otus.library.services;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import ru.otus.library.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreService {
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    Genre saveGenre(Genre genre);
    @PostFilter("hasPermission(filterObject, 'READ')")
    List<Genre> findGenresByName(String name);
    @PostFilter("hasPermission(filterObject, 'READ')")
    List<Genre> findByBookId(String id);
    @PostFilter("hasPermission(filterObject, 'READ')")
    List<Genre> findAll();
    @PostAuthorize("hasPermission(returnObject.get(), 'READ')")
    Optional<Genre> findById(String id);
    @PreAuthorize("hasPermission(#genre,'DELETE')")
    String delete(Genre genre);

}
