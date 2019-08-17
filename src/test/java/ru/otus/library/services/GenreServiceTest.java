package ru.otus.library.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.otus.library.model.Genre;
import ru.otus.library.repository.GenreRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("ServiceTest")
class GenreServiceTest {

    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private GenreService genreService;

    @Test
    void saveGenre() {
        Genre genre = new Genre("test");

        doAnswer(inv -> {
            Genre g = inv.getArgument(0);
            g.setId("1");
            return Mono.just(g);
        }).when(genreRepository).save(any(Genre.class));

        Mono<Genre> genreMono = genreService.saveGenre(genre);

        StepVerifier
                .create(genreMono)
                .expectNextMatches(a -> "1".equals(a.getId()))
                .expectComplete()
                .verify();
    }

    @Test
    void findGenresByName() {
        when(genreRepository.findByNameContainingIgnoreCase(anyString()))
                .thenReturn(Flux.fromIterable(getTestGenres()));
        Flux<Genre> genreFlux = genreService.findGenresByName("test");

        StepVerifier
                .create(genreFlux)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void findAll() {
        when(genreRepository.findAll()).thenReturn(Flux.fromIterable(getTestGenres()));

        Flux<Genre> genreFlux = genreService.findAll();

        StepVerifier
                .create(genreFlux)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    @DisplayName("Поиск по ID")
    void findById() {
        when(genreRepository.findById(anyString())).thenReturn(Mono.just(new Genre("test")));
        Mono<Genre> genreMono = genreService.findById("1");
        StepVerifier
                .create(genreMono)
                .expectNextMatches(a -> "test".equals(a.getName()))
                .verifyComplete();
    }

    @Test
    void delete() {
        doAnswer(invocation -> {
            String g = invocation.getArgument(0);
            assertEquals("test", g);
            return Mono.just(Void.TYPE);
        }).when(genreRepository).deleteById(anyString());

        genreService.delete("test");
    }

    @Test
    void findByBookId() {
        when(genreRepository.findByBookId(anyString())).thenReturn(Flux.fromIterable(getTestGenres()));

        Flux<Genre> genreFlux = genreService.findByBookId("1");

        StepVerifier
                .create(genreFlux)
                .expectNextCount(3)
                .verifyComplete();
    }

    private List<Genre> getTestGenres() {
        return List.of(new Genre("Genre1"),
                new Genre("Genre3"),
                new Genre("Genre3"));
    }
}