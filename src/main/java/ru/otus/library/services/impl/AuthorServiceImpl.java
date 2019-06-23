package ru.otus.library.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.library.dao.AuthorDao;
import ru.otus.library.model.Author;
import ru.otus.library.services.AuthorService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorDao authorDao;

    @Override
    public Author saveAuthor(Author author) {
        return authorDao.insert(author);
    }

    @Override
    public List<Author> findAuthorsByName(String name) {
        return authorDao.findByName(name);
    }

    @Override
    public Optional<Author> findById(int id) {
        return authorDao.getById(id);
    }

    @Override
    public int delete(Author author) {
        return authorDao.delete(author);
    }

    @Override
    public List<Author> findAll() {
        return authorDao.getAll();
    }
}
