package ru.otus.library.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.otus.library.model.Author;
import ru.otus.library.repository.AuthorRepository;
import ru.otus.library.services.AuthorService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @Override
    public Author saveAuthor(Author author) {
        return authorRepository.save(author);
    }

    @Override
    public List<Author> findAuthorsByName(String name) {
        return authorRepository.findByNameContaining(name);
    }

    @Override
    public Optional<Author> findById(String id) {
        try {
            return authorRepository.findById(id);
        } catch (EmptyResultDataAccessException e) {
            log.warn("Return Empty result.", e);
            return Optional.empty();
        }
    }

    @Override
    public String delete(Author author) {
        authorRepository.delete(author);
        return author.getId();
    }

    @Override
    public List<Author> findAll() {
        return authorRepository.findAll();
    }

    @Override
    public List<Author> findByBookId(String id) {
        return null;//authorRepository.findByBookId(id);
    }
}
