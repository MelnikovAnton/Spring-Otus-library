package ru.otus.library.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.stereotype.Service;
import ru.otus.library.integration.GenreIntegrationService;
import ru.otus.library.model.Genre;
import ru.otus.library.repository.GenreRepository;
import ru.otus.library.security.util.AclEditService;
import ru.otus.library.services.GenreService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    private final GenreIntegrationService genreIntegrationService;

    @Override
    public Genre saveGenre(Genre genre) {
        log.info("Save Genre {}", genre);
        return genreIntegrationService.createGenre(genre);
    }

    @Override
    public List<Genre> findGenresByName(String name) {
        return genreRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public Optional<Genre> findById(String id) {
        try {
            return genreRepository.findById(id);
        } catch (EmptyResultDataAccessException e) {
            log.warn("Return Empty result.", e);
            return Optional.empty();
        }
    }

    @Override
    public String delete(Genre genre) {
        log.info("Delete Genre {}", genre);
        genreIntegrationService.deleteGenre(genre);
        return genre.getId();
    }

    @Override
    public List<Genre> findAll() {
        return genreRepository.findAll();
    }

    @Override
    public List<Genre> findByBookId(String id) {
        return genreRepository.findByBookId(id);
    }
}
