package ru.otus.library.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.library.model.Genre;
import ru.otus.library.repository.GenreRepository;
import ru.otus.library.services.GenreService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    @Override
    public Mono<Genre> saveGenre(Genre genre) {
        return genreRepository.save(genre);
    }

    @Override
    public Flux<Genre> findGenresByName(String name) {
        return genreRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public Mono<Genre> findById(String id) {
            return genreRepository.findById(id);
    }

    @Override
    public Mono<Void> delete(String genreId) {
       return genreRepository.deleteById(genreId);
    }

    @Override
    public Flux<Genre> findAll() {
        return genreRepository.findAll();
    }

    @Override
    public Flux<Genre> findByBookId(String id) {
        return genreRepository.findByBookId(id);
    }
}
