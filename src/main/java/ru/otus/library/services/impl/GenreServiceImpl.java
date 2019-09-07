package ru.otus.library.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.stereotype.Service;
import ru.otus.library.model.Genre;
import ru.otus.library.repository.GenreRepository;
import ru.otus.library.secutity.AclCreationUtil;
import ru.otus.library.services.GenreService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    private final AclCreationUtil aclCreationUtil;

    @Override
    public Genre saveGenre(Genre genre) {
        Genre savedGenre = genreRepository.save(genre);
        aclCreationUtil.createDefaultAcl(new ObjectIdentityImpl(savedGenre));
        return savedGenre;
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
        genreRepository.delete(genre);
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
