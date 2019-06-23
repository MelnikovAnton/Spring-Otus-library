package ru.otus.library.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.library.dao.GenreDao;
import ru.otus.library.model.Genre;
import ru.otus.library.services.GenreService;

import java.util.List;

@Service
@RequiredArgsConstructor
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
    public Genre findById(int id) {
        return genreDao.getById(id);
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
