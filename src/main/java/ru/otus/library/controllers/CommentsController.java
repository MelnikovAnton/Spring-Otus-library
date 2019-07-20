package ru.otus.library.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.library.model.Book;
import ru.otus.library.model.Comment;
import ru.otus.library.services.BookService;
import ru.otus.library.services.CommentService;

import javax.websocket.server.PathParam;

@Controller
@RequiredArgsConstructor
public class CommentsController {

    private final CommentService commentService;
    private final BookService bookService;

    @GetMapping("/deleteComment")
    public String deleteComment(@RequestParam String id){
        Comment comment = commentService.findById(id).orElseThrow(() -> new RuntimeException("no comment with id " + id));
        commentService.delete(comment);
        return "redirect:/edit/" + comment.getBook().getId();
    }

    @PostMapping("/addComment/{bookId}")
    public String addComment(@PathVariable("bookId") String bookId,
                             @RequestParam String comment){
        Book book = bookService.findById(bookId).orElseThrow(()->new RuntimeException("no book with id " + bookId));
        commentService.saveComment(new Comment(book,comment));
        return "redirect:/edit/" + bookId;
    }
}
