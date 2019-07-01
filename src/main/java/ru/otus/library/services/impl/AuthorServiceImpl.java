package ru.otus.library.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.otus.library.dao.AuthorDao;
import ru.otus.library.model.Author;
import ru.otus.library.services.AuthorService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorServiceImpl implements AuthorService {

    private final AuthorDao authorDao;

    @Override
    public Author saveAuthor(Author author) {
        authorDao.insert(author);
        return author;
    }

    @Override
    public List<Author> findAuthorsByName(String name) {
        return authorDao.findByName(name);
    }

    @Override
    public Optional<Author> findById(long id) {
        try {
            return authorDao.getById(id);
        } catch (EmptyResultDataAccessException e) {
            log.warn("Return Empty result.", e);
            return Optional.empty();
        }
    }

    @Override
    public long delete(Author author) {
        authorDao.delete(author);
        return author.getId();
    }

    @Override
    public List<Author> findAll() {
        return authorDao.getAll();
    }

    @Override
    public List<Author> findByBookId(long id) {
        return authorDao.findByBookId(id);
    }
}
