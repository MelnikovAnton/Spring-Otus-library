package ru.otus.library.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.library.model.Book;
import ru.otus.library.model.Comment;
import ru.otus.library.services.BookService;
import ru.otus.library.services.CommentService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final CommentService commentService;

    @GetMapping("/")
    public String getBookList(Model model) {
        List<Book> books = bookService.findAll();
        model.addAttribute("books", books);
        return "bookList";
    }

    @GetMapping("edit/{id}")
    public String editBook(@PathVariable("id") String id, Model model) {
        Book book = bookService.findById(id).orElseThrow(() -> new RuntimeException("no book with id " + id));
        List<Comment> comments = commentService.findCommentsByBook(book);
        model.addAttribute("book", book);
        model.addAttribute("comments", comments);
        return "editBook";
    }


    @PostMapping(value = "edit/{id}")
    public String postBook(@ModelAttribute Book book) {
        bookService.saveBook(book);
        return "redirect:/edit/" + book.getId();
    }

}
