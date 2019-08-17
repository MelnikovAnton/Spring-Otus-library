package ru.otus.library.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.library.model.Author;
import ru.otus.library.services.AuthorService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("WebTest")
public class AuthorControllerTest {

    @Autowired
    private RouterFunction authorRestController;
    @Autowired
    private AuthorService authorService;


    @Test
    @DisplayName("Тест получение списка авторов")
    void getAuthorList() {
        when(authorService.findAll()).thenReturn(Flux.fromIterable(getTestAuthors()));

        WebTestClient client = WebTestClient
                .bindToRouterFunction(authorRestController)
                .build();

        client.get()
                .uri("/authors")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Author.class).hasSize(3).contains(getTestAuthors().get(0));
    }

    @Test
    @DisplayName("Тест получение автора по ID")
    void getAuthorById() {
        when(authorService.findById(anyString())).thenReturn(Mono.just(new Author("Test1", "test")));

        WebTestClient client = WebTestClient
                .bindToRouterFunction(authorRestController)
                .build();

        Author author = client.get()
                .uri("/authors/id")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Author.class)
                .returnResult().getResponseBody();
        assertEquals("Test1", author.getId());
    }

    @Test
    @DisplayName("Добавление автора")
    void create() {
        when(authorService.saveAuthor(any(Author.class))).thenReturn(Mono.just(new Author("TestId", "Test")));

        WebTestClient client = WebTestClient
                .bindToRouterFunction(authorRestController)
                .build();

        Author author = client.post()
                .uri("/authors")
                .body(Mono.just(new Author()), Author.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Author.class)
                .returnResult().getResponseBody();

        assertEquals("TestId", author.getId());
    }

    @Test
    @DisplayName("Update автора")
    void update() {
        when(authorService.saveAuthor(any(Author.class))).thenReturn(Mono.just(new Author("Test1", "test")));

        WebTestClient client = WebTestClient
                .bindToRouterFunction(authorRestController)
                .build();

        Author author = client.put()
                .uri("/authors/Test1")
                .body(Mono.just(new Author()), Author.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Author.class)
                .returnResult().getResponseBody();

        assertEquals("Test1", author.getId());
    }

    @Test
    @DisplayName("Удаление автора")
    void delete() {
        when(authorService.delete(anyString())).thenReturn(Mono.empty());

        WebTestClient client = WebTestClient
                .bindToRouterFunction(authorRestController)
                .build();

        client.delete()
                .uri("/authors/Test1")
                .exchange()
                .expectStatus().isOk();
    }

    private List<Author> getTestAuthors() {
        return List.of(new Author("Auth1"),
                new Author("Auth2"),
                new Author("Auth3"));
    }
}
