package ru.otus.library.controllers.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.otus.library.model.Book;
import ru.otus.library.model.Comment;
import ru.otus.library.services.BookService;
import ru.otus.library.services.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("commentApi")
public class RestCommentController {

    private final CommentService commentService;
    private final BookService bookService;

    @GetMapping
    public List<Comment> getAllList() {
        return commentService.findAll();
    }

    @GetMapping("{bookId}")
    public List<Comment> getByBookId(@PathVariable("bookId") String id) {
        Book book = bookService.findById(id).orElseThrow(() -> new RuntimeException("no comment with id " + id));
        return commentService.findCommentsByBook(book);
    }

    @PostMapping
    public Comment create(@RequestBody Comment comment) {
        return commentService.saveComment(comment);
    }

    @PutMapping("{id}")
    public Comment update(@RequestBody Comment comment,
                       @PathVariable String id) {
        commentService.findById(id).orElseThrow(() -> new RuntimeException("no comment with id " + id));
        comment.setId(id);
        return commentService.saveComment(comment);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable String id) {
        Comment comment = commentService.findById(id).orElseThrow(() -> new RuntimeException("no comment with id " + id));
        commentService.delete(comment);
    }
}
