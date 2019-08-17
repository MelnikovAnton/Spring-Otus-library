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

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;


class AuthorRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Вставка с получением ID")
    void insert() {
        Mono<Author> authorMono = authorRepository.save(new Author("Author"));

        StepVerifier
                .create(authorMono)
                .assertNext(a -> assertNotNull(a.getId()))
                .expectComplete()
                .verify();
    }

    @TestFactory
    @DisplayName("Поиск по имени автора")
    List<DynamicTest> findAuthorsByName() {

        List<Author> authors = authorRepository.saveAll(getTestAuthors()).collectList().block();

        DynamicTest noAuthors = DynamicTest.dynamicTest("Авторов нет", () -> {
            Flux<Author> authorFlux = authorRepository.findByNameContainingIgnoreCase("g");

            StepVerifier
                    .create(authorFlux)
                    .expectNextCount(0)
                    .verifyComplete();
        });

        DynamicTest existsAuthors = DynamicTest.dynamicTest("Автор есть", () -> {
            Flux<Author> authorFlux = authorRepository.findByNameContainingIgnoreCase("a");

            StepVerifier
                    .create(authorFlux)
                    .expectNextCount(3)
                    .verifyComplete();
        });

        return Arrays.asList(noAuthors,existsAuthors);
    }

    @Test
    @DisplayName("Поиск по Id книги")
    void findByBookId() {

        Author author = authorRepository.save(new Author("TestID", "Author")).block();

        Book testBook = new Book("BookID", "Book1", "content");
        testBook.addAuthor(author);

        Book book = bookRepository.save(testBook).block();

        Flux<Author> authorFlux = authorRepository.findByBookId(book.getId());

        StepVerifier
                .create(authorFlux)
                .expectNext(author)
                .verifyComplete();
    }

    @Test
    @DisplayName("Удаление автора из книг")
    void deleteAuthorFromBook() {

        Author author = authorRepository.save(new Author("TestID", "Author")).block();

        Book testBook = new Book("BookID", "Book1", "content");
        testBook.addAuthor(author);

        Book book = bookRepository.save(testBook).block();

        authorRepository.deleteById("TestID").block();

        Flux<Book> bookFlux = bookRepository.findByAuthorsContains(Mono.just(author));

        StepVerifier
                .create(bookFlux)
                .verifyComplete();
    }


    private List<Author> getTestAuthors() {
        return List.of(new Author("Auth1"),
                new Author("Auth2"),
                new Author("Auth3"));
    }
}