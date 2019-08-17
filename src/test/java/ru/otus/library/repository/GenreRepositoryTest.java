package ru.otus.library.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.otus.library.model.Book;
import ru.otus.library.model.Genre;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class GenreRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Вставка с получением ID")
    void insert() {
        Mono<Genre> genreMono = genreRepository.save(new Genre("Genre"));

        StepVerifier
                .create(genreMono)
                .assertNext(a -> assertNotNull(a.getId()))
                .expectComplete()
                .verify();
    }

    @TestFactory
    @DisplayName("Поиск по имени жанра")
    List<DynamicTest> findGenresByName() {
        List<Genre> genres = genreRepository.saveAll(getTestGenres()).collectList().block();
        DynamicTest noGenres = DynamicTest.dynamicTest("Жанра нет", () -> {
            Flux<Genre> genreFlux = genreRepository.findByNameContainingIgnoreCase("a");

            StepVerifier
                    .create(genreFlux)
                    .expectNextCount(0)
                    .verifyComplete();
        });
        DynamicTest existsGenre = DynamicTest.dynamicTest("Жанр есть", () -> {
            Flux<Genre> genreFlux = genreRepository.findByNameContainingIgnoreCase("g");

            StepVerifier
                    .create(genreFlux)
                    .expectNextCount(3)
                    .verifyComplete();
        });

        return Arrays.asList(noGenres, existsGenre);
    }

    @Test
    @DisplayName("Поиск по Id книги")
    void findByBookId() {
        Genre genre = genreRepository.save(new Genre("TestID", "Genre")).block();

        Book testBook = new Book("BookID", "Book1", "content");
        testBook.addGenre(genre);

        Book book = bookRepository.save(testBook).block();

        Flux<Genre> authorFlux = genreRepository.findByBookId(book.getId());

        StepVerifier
                .create(authorFlux)
                .expectNext(genre)
                .verifyComplete();
    }

    @Test
    @DisplayName("Удаление жанра из книг")
    void deleteAuthorFromBook() {
        Genre genre = genreRepository.save(new Genre("TestID", "Genre")).block();

        Book testBook = new Book("BookID", "Book1", "content");
        testBook.addGenre(genre);

        Book book = bookRepository.save(testBook).block();

        genreRepository.deleteById("TestID").block();

        Flux<Book> bookFlux = bookRepository.findByGenresContains(Mono.just(genre));

        StepVerifier
                .create(bookFlux)
                .verifyComplete();
    }

    private List<Genre> getTestGenres() {
        return List.of(new Genre("Genre1"),
                new Genre("Genre3"),
                new Genre("Genre3"));
    }

}