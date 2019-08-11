package ru.otus.library.repository;

import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.library.model.Comment;

public interface CommentRepository extends ReactiveMongoRepository<Comment, String> {

    Flux<Comment> findByBookId(String id);

    @DeleteQuery("{'book.id': :#{#bookId} }")
    Mono<Long> deleteCommentsByBookId(@Param("bookId") String bookId);
}
