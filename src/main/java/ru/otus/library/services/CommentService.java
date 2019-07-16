package ru.otus.library.services;

import ru.otus.library.model.Book;
import ru.otus.library.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    Comment saveComment(Comment comment);

    List<Comment> findCommentsByBook(Book book);

    List<Comment> findAll();

    Optional<Comment> findById(String id);

    String delete(Comment comment);
}
