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
import ru.otus.library.model.Book;
import ru.otus.library.model.Comment;
import ru.otus.library.repository.CommentRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("ServiceTest")
class  CommentServiceTest {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private CommentService commentService;

    @Test
    void saveComment() {
        Comment comment = new Comment();

        doAnswer(inv -> {
            Comment c = inv.getArgument(0);
            c.setId("1");
            return Mono.just(c);
        }).when(commentRepository).save(any(Comment.class));

        Mono<Comment> commentMono = commentService.saveComment(comment);

        StepVerifier
                .create(commentMono)
                .expectNextMatches(a -> "1".equals(a.getId()))
                .expectComplete()
                .verify();
    }

    @Test
    void findCommentsByBook() {
        when(commentRepository.findByBookId(anyString())).thenReturn(Flux.fromIterable(getTestComments()));

        Flux<Comment> commentFlux = commentService.findCommentsByBook("bookId");

        StepVerifier
                .create(commentFlux)
                .expectNextCount(3)
                .verifyComplete();
    }


    @Test
    void findAll() {
        when(commentRepository.findAll()).thenReturn(Flux.fromIterable(getTestComments()));

        Flux<Comment> commentFlux = commentService.findAll();

        StepVerifier
                .create(commentFlux)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    @DisplayName("Поиск по ID")
    void findById() {
      Comment comment= new Comment();
      comment.setComment("TestComment");
      when(commentRepository.findById(anyString())).thenReturn(Mono.just(comment));

        Mono<Comment> commentMono = commentService.findById("id");

        StepVerifier
                .create(commentMono)
                .expectNextMatches(c->"TestComment".equals(c.getComment()))
                .verifyComplete();
    }

    @Test
    void delete() {
        doAnswer(invocation -> {
            String c = invocation.getArgument(0);
            assertEquals("test", c);
            return Mono.just(Void.TYPE);
        }).when(commentRepository).deleteById(anyString());

        commentService.delete("test");
    }

    private List<Comment> getTestComments() {
        return List.of(new Comment(), new Comment(), new Comment());
    }

}