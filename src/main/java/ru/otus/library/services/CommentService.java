package ru.otus.library.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.library.model.Book;
import ru.otus.library.model.Comment;

public interface CommentService {
    Mono<Comment> saveComment(Comment comment);

    Flux<Comment> findCommentsByBook(String bookId);

    Flux<Comment> findAll();

    Mono<Comment> findById(String id);

    Mono<Void> delete(String id);
}
