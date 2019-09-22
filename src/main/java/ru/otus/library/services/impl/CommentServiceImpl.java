package ru.otus.library.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.stereotype.Service;
import ru.otus.library.integration.CommentIntegrationService;
import ru.otus.library.model.Book;
import ru.otus.library.model.Comment;
import ru.otus.library.repository.CommentRepository;
import ru.otus.library.security.util.AclEditService;
import ru.otus.library.services.CommentService;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentIntegrationService commentIntegrationService;

    @Override
    public Comment saveComment(Comment comment) {
        log.info("Save Genre {}", comment);
        return commentIntegrationService.createComment(comment);

    }

    @Override
    public List<Comment> findCommentsByBook(Book book) {
        return commentRepository.findByBookId(book.getId());
    }

    @Override
    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    @Override
    public Optional<Comment> findById(String id) {
        try {
            return commentRepository.findById(id);
        } catch (EmptyResultDataAccessException e) {
            log.warn("Return Empty result.", e);
            return Optional.empty();
        }
    }

    @Override
    public String delete(Comment comment) {
        log.info("Delete comment {}", comment);
        commentIntegrationService.deleteComment(comment);
        return comment.getId();
    }
}
