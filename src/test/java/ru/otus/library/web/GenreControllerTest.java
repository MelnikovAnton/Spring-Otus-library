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
import ru.otus.library.model.Genre;
import ru.otus.library.services.GenreService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("WebTest")
public class GenreControllerTest {

    @Autowired
    private RouterFunction genreRestController;
    @Autowired
    private GenreService genreService;

    @Test
    @DisplayName("Тест получение списка жанров")
    void getGenreList() {
        when(genreService.findAll()).thenReturn(Flux.fromIterable(getTestGenres()));

        WebTestClient client = WebTestClient
                .bindToRouterFunction(genreRestController)
                .build();

        client.get()
                .uri("/genres")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Genre.class).hasSize(3).contains(getTestGenres().get(0));
    }

    @Test
    @DisplayName("Тест получение жанра по ID")
    void getGenreById() {
        when(genreService.findById(anyString())).thenReturn(Mono.just(new Genre("Test1", "test")));

        WebTestClient client = WebTestClient
                .bindToRouterFunction(genreRestController)
                .build();

        Genre genre = client.get()
                .uri("/genres/id")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Genre.class)
                .returnResult().getResponseBody();
        assertEquals("Test1", genre.getId());
    }

    @Test
    @DisplayName("Добавление жанра")
    void create() {
        when(genreService.saveGenre(any(Genre.class))).thenReturn(Mono.just(new Genre("TestId", "Test")));

        WebTestClient client = WebTestClient
                .bindToRouterFunction(genreRestController)
                .build();

        Genre genre = client.post()
                .uri("/genres")
                .body(Mono.just(new Genre()), Genre.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Genre.class)
                .returnResult().getResponseBody();

        assertEquals("TestId", genre.getId());
    }

    @Test
    @DisplayName("Update жанра")
    void update() {
        when(genreService.saveGenre(any(Genre.class))).thenReturn(Mono.just(new Genre("Test1", "test")));

        WebTestClient client = WebTestClient
                .bindToRouterFunction(genreRestController)
                .build();

        Genre genre = client.put()
                .uri("/genres/Test1")
                .body(Mono.just(new Genre()), Genre.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Genre.class)
                .returnResult().getResponseBody();

        assertEquals("Test1", genre.getId());
    }

    @Test
    @DisplayName("Удаление жанра")
    void delete()  {
        when(genreService.delete(anyString())).thenReturn(Mono.empty());

        WebTestClient client = WebTestClient
                .bindToRouterFunction(genreRestController)
                .build();

        client.delete()
                .uri("/genres/Test1")
                .exchange()
                .expectStatus().isOk();
    }

    private List<Genre> getTestGenres() {
        return List.of(new Genre("Genre1"),
                new Genre("Genre2"),
                new Genre("Genre3"));
    }
}
