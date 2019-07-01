package ru.otus.library.dao;

import ru.otus.library.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentDao {
    long count();

    void insert(Comment comment);

    List<Comment> findByBookId(long id);

    List<Comment> findAll();

    Optional<Comment> findById(long id);

    void delete(Comment comment);
}
