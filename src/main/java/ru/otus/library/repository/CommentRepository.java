package ru.otus.library.repository;

import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import ru.otus.library.model.Comment;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String> {

    List<Comment> findByBookId(String id);
    @DeleteQuery("{'book.id': :#{#bookId} }")
    Long deleteCommentsByBookId(@Param("bookId") String bookId);
}
