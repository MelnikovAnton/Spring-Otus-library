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
import ru.otus.library.model.Book;
import ru.otus.library.model.Comment;
import ru.otus.library.services.CommentService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("WebTest")
public class CommentControllerTest {

    @Autowired
    private RouterFunction commentsRestController;
    @Autowired
    private CommentService commentService;


    @Test
    @DisplayName("Тест получение списка комментариев")
    void getCommentList() {
        when(commentService.findAll()).thenReturn(Flux.fromIterable(getTestComments()));

        WebTestClient client = WebTestClient
                .bindToRouterFunction(commentsRestController)
                .build();

        client.get()
                .uri("/comments")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Comment.class).hasSize(3).contains(getTestComments().get(0));
    }

    @Test
    @DisplayName("Тест получение комментария по ID книги")
    void getCommentByBookId() {
        when(commentService.findCommentsByBook(anyString())).thenReturn(Flux.fromIterable(getTestComments()));

        WebTestClient client = WebTestClient
                .bindToRouterFunction(commentsRestController)
                .build();

        client.get()
                .uri("/comments/id")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Comment.class).hasSize(3).contains(getTestComments().get(1));
    }

    @Test
    @DisplayName("Добавление комментария")
    void create() {
        when(commentService.saveComment(any(Comment.class))).thenReturn(Mono.just(getTestComments().get(0)));

        WebTestClient client = WebTestClient
                .bindToRouterFunction(commentsRestController)
                .build();

        Comment comment = client.post()
                .uri("/comments")
                .body(Mono.just(new Comment()), Comment.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Comment.class)
                .returnResult().getResponseBody();

        assertEquals("id1", comment.getId());
    }

    @Test
    @DisplayName("Update комментария")
    void update() {
        when(commentService.saveComment(any(Comment.class))).thenReturn(Mono.just(getTestComments().get(0)));

        WebTestClient client = WebTestClient
                .bindToRouterFunction(commentsRestController)
                .build();

        Comment comment = client.put()
                .uri("/comments/Test1")
                .body(Mono.just(new Comment()), Comment.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Comment.class)
                .returnResult().getResponseBody();

        assertEquals("id1", comment.getId());
    }

    @Test
    @DisplayName("Удаление комментария")
    void deleteBookTest() throws Exception {
        when(commentService.delete(anyString())).thenReturn(Mono.empty());

        WebTestClient client = WebTestClient
                .bindToRouterFunction(commentsRestController)
                .build();

        client.delete()
                .uri("/comments/Test1")
                .exchange()
                .expectStatus().isOk();
    }

    private List<Comment> getTestComments() {
        Book book = new Book("id1", "Test1", "Test1");
        return List.of(new Comment("id1", book, "comment1"),
                new Comment("id", book, "comment2"),
                new Comment("id2", book, "comment3"));
    }
}
