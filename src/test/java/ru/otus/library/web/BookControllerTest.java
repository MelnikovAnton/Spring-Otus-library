package ru.otus.library.web;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.reactive.function.server.RouterFunction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.library.model.Author;
import ru.otus.library.model.Book;
import ru.otus.library.services.BookService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("WebTest")
public class BookControllerTest {

    @Autowired
    private RouterFunction bookRestController;
    @Autowired
    private BookService bookService;

    @Test
    @DisplayName("Тест получение списка книг")
    void getBookList() {
        when(bookService.findAll()).thenReturn(Flux.fromIterable(getTestBooks()));

        WebTestClient client = WebTestClient
                .bindToRouterFunction(bookRestController)
                .build();

        client.get()
                .uri("/books")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Book.class).hasSize(3).contains(getTestBooks().get(0));
    }

    @Test
    @DisplayName("Тест получение книги по ID")
    void getBookById() {
        when(bookService.findById(anyString())).thenReturn(Mono.just(getTestBooks().get(0)));

        WebTestClient client = WebTestClient
                .bindToRouterFunction(bookRestController)
                .build();

        Book book = client.get()
                .uri("/books/id")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Book.class)
                .returnResult().getResponseBody();
        assertEquals("id1", book.getId());
    }

    @Test
    @DisplayName("Добавление книги")
    void create() {
        when(bookService.saveBook(any(Book.class))).thenReturn(Mono.just(getTestBooks().get(0)));

        WebTestClient client = WebTestClient
                .bindToRouterFunction(bookRestController)
                .build();

        Book book = client.post()
                .uri("/books")
                .body(Mono.just(new Book()), Book.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Book.class)
                .returnResult().getResponseBody();

        assertEquals("id1", book.getId());
    }

    @Test
    @DisplayName("Update книги")
    void update() {
        when(bookService.saveBook(any(Book.class))).thenReturn(Mono.just(getTestBooks().get(0)));

        WebTestClient client = WebTestClient
                .bindToRouterFunction(bookRestController)
                .build();

        Book book = client.put()
                .uri("/books/Test1")
                .body(Mono.just(new Book()), Book.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Book.class)
                .returnResult().getResponseBody();

        assertEquals("id1", book.getId());
    }

    @Test
    @DisplayName("Удаление книги")
    void deleteBookTest() {
        when(bookService.delete(anyString())).thenReturn(Mono.empty());

        WebTestClient client = WebTestClient
                .bindToRouterFunction(bookRestController)
                .build();

        client.delete()
                .uri("/books/Test1")
                .exchange()
                .expectStatus().isOk();
    }

    private List<Book> getTestBooks() {
        return List.of(new Book("id1", "Test1", "Test1"),
                new Book("id1", "Test2", "Test2"),
                new Book("id1", "Test3", "Test3"));
    }
}
