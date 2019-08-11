package ru.otus.library.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.library.model.Book;
import ru.otus.library.model.Comment;
import ru.otus.library.repository.CommentRepository;
import ru.otus.library.services.CommentService;


@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Override
    public Mono<Comment> saveComment(Comment comment) {
        return commentRepository.save(comment);

    }

    @Override
    public Flux<Comment> findCommentsByBook(String bookId) {
        return commentRepository.findByBookId(bookId);
    }

    @Override
    public Flux<Comment> findAll() {
        return commentRepository.findAll();
    }

    @Override
    public Mono<Comment> findById(String id) {
        return commentRepository.findById(id);
    }

    @Override
    public Mono<Void> delete(String id) {
      return commentRepository.deleteById(id);
    }
}
