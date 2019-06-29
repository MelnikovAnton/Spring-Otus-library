package ru.otus.library.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.otus.library.dao.GenreDao;
import ru.otus.library.model.Genre;
import ru.otus.library.services.GenreService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenreServiceImpl implements GenreService {

    private final GenreDao genreDao;

    @Override
    public Genre saveGenre(Genre genre) {
        return genreDao.insert(genre);
    }

    @Override
    public List<Genre> findGenresByName(String name) {
        return genreDao.findByName(name);
    }

    @Override
    public Optional<Genre> findById(int id) {
        try {
            return genreDao.getById(id);
        } catch (EmptyResultDataAccessException e) {
            log.warn("Return Empty result.", e);
            return Optional.empty();
        }
    }

    @Override
    public int delete(Genre genre) {
        return genreDao.delete(genre);
    }

    @Override
    public List<Genre> findAll() {
        return genreDao.getAll();
    }
}
