package ru.otus.library.services;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import ru.otus.library.model.Book;
import ru.otus.library.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    Comment saveComment(Comment comment);
    @PostFilter("hasPermission(filterObject, 'READ')")
    List<Comment> findCommentsByBook(Book book);
    @PostFilter("hasPermission(filterObject, 'READ')")
    List<Comment> findAll();
    @PostAuthorize("hasPermission(returnObject.get(), 'READ')")
    Optional<Comment> findById(String id);
    @PreAuthorize("hasPermission(#comment,'DELETE')")
    String delete(Comment comment);
}
