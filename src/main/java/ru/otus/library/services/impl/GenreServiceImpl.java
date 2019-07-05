package ru.otus.library.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.otus.library.model.Genre;
import ru.otus.library.repository.GenreRepository;
import ru.otus.library.services.GenreService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    @Override
    public Genre saveGenre(Genre genre) {
        return genreRepository.save(genre);
    }

    @Override
    public List<Genre> findGenresByName(String name) {
        return genreRepository.findByNameContaining(name);
    }

    @Override
    public Optional<Genre> findById(long id) {
        try {
            return genreRepository.findById(id);
        } catch (EmptyResultDataAccessException e) {
            log.warn("Return Empty result.", e);
            return Optional.empty();
        }
    }

    @Override
    public long delete(Genre genre) {
        genreRepository.delete(genre);
        return genre.getId();
    }

    @Override
    public List<Genre> findAll() {
        return genreRepository.findAll();
    }

    @Override
    public List<Genre> findByBookId(long id) {
        return genreRepository.findByBookId(id);
    }
}
