package ru.otus.library.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.library.model.Author;
import ru.otus.library.repository.AuthorRepository;
import ru.otus.library.services.AuthorService;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @Override
    public Mono<Author> saveAuthor(Author author) {
        return authorRepository.save(author);
    }

    @Override
    public Flux<Author> findAuthorsByName(String name) {
        return authorRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public Mono<Author> findById(String id) {
        return authorRepository.findById(id);
    }

    @Override
    public Mono<Void> delete(String authorId) {
        return authorRepository.deleteById(authorId);
    }

    @Override
    public Flux<Author> findAll() {
        return authorRepository.findAll();
    }

    @Override
    public Flux<Author> findByBookId(String id) {
        return authorRepository.findByBookId(id);
    }
}
