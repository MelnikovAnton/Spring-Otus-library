package ru.otus.library.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.otus.library.dao.CommentDao;
import ru.otus.library.model.Book;
import ru.otus.library.model.Comment;
import ru.otus.library.services.CommentService;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentDao commentDao;

    @Override
    public Comment saveComment(Comment comment) {
        commentDao.insert(comment);
        return comment;
    }

    @Override
    public List<Comment> findCommentsByBook(Book book) {
        return commentDao.findByBookId(book.getId());
    }

    @Override
    public List<Comment> findAll() {
        return commentDao.findAll();
    }

    @Override
    public Optional<Comment> findById(long id) {
        try {
            return commentDao.findById(id);
        } catch (EmptyResultDataAccessException e) {
            log.warn("Return Empty result.", e);
            return Optional.empty();
        }
    }

    @Override
    public long delete(Comment comment) {
        commentDao.delete(comment);
        return comment.getId();
    }
}
