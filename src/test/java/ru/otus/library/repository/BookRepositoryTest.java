package ru.otus.library.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.otus.library.model.Author;
import ru.otus.library.model.Book;
import ru.otus.library.model.Genre;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private GenreRepository genreRepository;

    @Test
    @DisplayName("Вставка с получением ID")
    void insert() {
        Book book = new Book("qwe", "ewq");

        Mono<Book> bookMono = bookRepository.save(book);

        StepVerifier
                .create(bookMono)
                .assertNext(a -> assertNotNull(a.getId()))
                .expectComplete()
                .verify();
    }


    @TestFactory
    @DisplayName("Поиск по заголовку")
    List<DynamicTest> findByTitle() {

        List<Book> books = bookRepository.saveAll(getTestBooks()).collectList().block();

        DynamicTest empty = DynamicTest.dynamicTest("Пустой заголовок", () -> {
            Flux<Book> bookFlux = bookRepository.findByTitleContaining("");
            StepVerifier
                    .create(bookFlux)
                    .expectNextCount(3)
                    .verifyComplete();
        });
        DynamicTest part = DynamicTest.dynamicTest("Часть заголовока", () -> {
            Flux<Book> bookFlux = bookRepository.findByTitleContaining("est");
            StepVerifier
                    .create(bookFlux)
                    .expectNextCount(3)
                    .verifyComplete();
        });
        DynamicTest full = DynamicTest.dynamicTest("Полный заголовок", () -> {
            Flux<Book> bookFlux = bookRepository.findByTitleContaining("Test2");
            StepVerifier
                    .create(bookFlux)
                    .expectNextMatches(b -> "Test2".equals(b.getTitle()))
                    .verifyComplete();
        });
        DynamicTest noMatch = DynamicTest.dynamicTest("Не совпадающий заголовок", () -> {
            Flux<Book> bookFlux = bookRepository.findByTitleContaining("\"!№;%:?*:;%;№\"");
            StepVerifier
                    .create(bookFlux)
                    .expectNextCount(0)
                    .verifyComplete();
        });
        return Arrays.asList(empty, part, full, noMatch);
    }

    @TestFactory
    @DisplayName("Поиск по автору")
    List<DynamicTest> getByAuthor() {
        Author author = authorRepository.save(new Author("TestID", "Author")).block();

        Book testBook = new Book("BookID", "Book1", "content");
        testBook.addAuthor(author);

        Book book = bookRepository.save(testBook).block();

        DynamicTest auth1 = DynamicTest
                .dynamicTest("Получение книги по автору", () -> {
                    Flux<Book> bookFlux = bookRepository.findByAuthorsNameContains("Author");
                    StepVerifier
                            .create(bookFlux)
                            .expectNextMatches(b -> "BookID".equals(b.getId()))
                            .verifyComplete();
                });

        DynamicTest auth2 = DynamicTest.dynamicTest("Автор с неправельным ID", () -> {
            Flux<Book> bookFlux = bookRepository.findByAuthorsNameContains("WrongName");
            StepVerifier
                    .create(bookFlux)
                    .expectNextCount(0)
                    .verifyComplete();
        });
        return Arrays.asList(auth1, auth2);
    }

    @TestFactory
    @DisplayName("Поиск по жанру")
    List<DynamicTest> getByGenre() {
        Genre genre = genreRepository.save(new Genre("TestID", "Genre")).block();

        Book testBook = new Book("BookID", "Book1", "content");
        testBook.addGenre(genre);

        Book book = bookRepository.save(testBook).block();

        DynamicTest genre1 = DynamicTest.dynamicTest("Поиск книги по жанру", () -> {
            Flux<Book> bookFlux = bookRepository.findByGenresNameContains("Genre");
            StepVerifier
                    .create(bookFlux)
                    .expectNextMatches(b -> "BookID".equals(b.getId()))
                    .verifyComplete();
        });

        DynamicTest genre2 = DynamicTest.dynamicTest("Жанр с несуществующим ID", () -> {
            Flux<Book> bookFlux = bookRepository.findByGenresNameContains("WrongName");
            StepVerifier
                    .create(bookFlux)
                    .expectNextCount(0)
                    .verifyComplete();
        });
        return Arrays.asList(genre1, genre2);
    }

    private List<Book> getTestBooks() {
        return List.of(new Book("Test1", "Test1"),
                new Book("Test2", "Test2"),
                new Book("Test3", "Test3"));
    }
}