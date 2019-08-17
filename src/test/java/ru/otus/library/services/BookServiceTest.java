package ru.otus.library.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.otus.library.model.Author;
import ru.otus.library.model.Book;
import ru.otus.library.model.Genre;
import ru.otus.library.repository.BookRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("ServiceTest")
class BookServiceTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;
    @MockBean
    private AuthorService authorService;
    @MockBean
    private GenreService genreService;

    @Test
    @DisplayName("Сохранение книги")
    void saveBook() {
        Book book = new Book("test", "test");

        doAnswer(invocation -> {
            Book b = (Book) invocation.getArgument(0);
            b.setId("1");
            return Mono.just(b);
        }).when(bookRepository).save(any(Book.class));

        Mono<Book> bookMono = bookService.saveBook(book);
        StepVerifier
                .create(bookMono)
                .expectNextMatches(b -> "1".equals(b.getId()))
                .expectComplete()
                .verify();

    }

    @Test
    void findBooksByTitle() {
        when(bookRepository.findByTitleContaining(anyString())).thenReturn(Flux.fromIterable(getTestBooks()));

        Flux<Book> bookFlux = bookService.findBooksByTitle("test");

        StepVerifier
                .create(bookFlux)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void findBooksByAuthor() {
        when(bookRepository.findByAuthorsNameContains(anyString())).thenReturn(Flux.fromIterable(getTestBooks()));

        Flux<Book> bookFlux = bookService.findBooksByAuthor("test");

        StepVerifier
                .create(bookFlux)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void findBooksByGenre() {
        when(bookRepository.findByAuthorsNameContains(anyString())).thenReturn(Flux.fromIterable(getTestBooks()));

        Flux<Book> bookFlux = bookService.findBooksByAuthor("test");

        StepVerifier
                .create(bookFlux)
                .expectNextCount(3)
                .verifyComplete();
    }

    @TestFactory
    @DisplayName("Поиск по ID")
    void findById() {
         when(bookRepository.findById(anyString())).thenReturn(Mono.just(new Book("test", "test")));
        Mono<Book> bookMono = bookService.findById("1");
        StepVerifier
                .create(bookMono)
                .expectNextMatches(b -> "test".equals(b.getTitle()))
                .verifyComplete();

    }

    @Test
    void delete() {
        doAnswer(invocation -> {
            String b = invocation.getArgument(0);
            assertEquals("test", b);
            return Mono.just(Void.TYPE);
        }).when(bookRepository).deleteById(anyString());

        authorService.delete("test");
    }

    @Test
    void findAll() {
        when(bookRepository.findAll()).thenReturn(Flux.fromIterable(getTestBooks()));

        Flux<Book> bookFlux = bookService.findAll();

        StepVerifier
                .create(bookFlux)
                .expectNextCount(3)
                .verifyComplete();
    }

    private List<Book> getTestBooks() {
        return List.of(new Book("Test1", "Test1"),
                new Book("Test2", "Test2"),
                new Book("Test3", "Test3"));
    }

}