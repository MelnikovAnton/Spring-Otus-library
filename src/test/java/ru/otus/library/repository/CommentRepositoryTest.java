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
import ru.otus.library.model.Comment;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class CommentRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Добавление коментария")
    void insert() {

        Book testBook = new Book("BookID", "Book1", "content");

        Book book = bookRepository.save(testBook).block();

        Comment comment = new Comment();
        comment.setBook(book);
        Mono<Comment> commentMono = commentRepository.save(comment);

        StepVerifier
                .create(commentMono)
                .assertNext(c -> assertNotNull(c.getId()))
                .expectComplete()
                .verify();
    }

    @TestFactory
    @DisplayName("Получение коментария по книге")
    List<DynamicTest> findByBook() {
        Book testBook = new Book("BookID", "Book1", "content");

        Book book = bookRepository.save(testBook).block();

        Comment testComment = new Comment();
        testComment.setBook(book);

        Comment comment = commentRepository.save(testComment).block();

        DynamicTest comment1 = DynamicTest.dynamicTest("ID есть в БД", () -> {
            Flux<Comment> commentFlux = commentRepository.findByBookId("BookID");
            StepVerifier
                    .create(commentFlux)
                    .expectNext(comment)
                    .verifyComplete();
        });
        DynamicTest comment2 = DynamicTest.dynamicTest("ID нет в базе", () -> {
            Flux<Comment> commentFlux = commentRepository.findByBookId("WrongId");
            StepVerifier
                    .create(commentFlux)
                    .verifyComplete();
        });
        return Arrays.asList(comment1, comment2);
    }

    @Test
    @DisplayName("Комментарий удаляется при удалении книги")
    void deleteBookAndComment() {
        Book testBook = new Book("BookID", "Book1", "content");

        Book book = bookRepository.save(testBook).block();

        Comment testComment = new Comment();
        testComment.setBook(book);

        Comment comment = commentRepository.save(testComment).block();

        bookRepository.deleteById("BookID").block();

        Flux<Comment> commentFlux = commentRepository.findByBookId("BookID");
        StepVerifier
                .create(commentFlux)
                .verifyComplete();

    }

}