package ru.otus.library.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.otus.library.model.Author;
import ru.otus.library.model.Book;
import ru.otus.library.model.Comment;
import ru.otus.library.model.Genre;
import ru.otus.library.services.AuthorService;
import ru.otus.library.services.BookService;
import ru.otus.library.services.CommentService;
import ru.otus.library.services.GenreService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final CommentService commentService;
    private final AuthorService authorService;
    private final GenreService genreService;

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
        List<Author> authors=authorService.findAll();
        List<Genre> genres=genreService.findAll();
        model.addAttribute("book", book);
        model.addAttribute("comments", comments);
        model.addAttribute("authors",authors);
        model.addAttribute("genres",genres);
        return "editBook";
    }


    @PostMapping(value = "edit/{id}")
    public String postBook(@ModelAttribute Book book) {
        bookService.saveBook(book);
        return "redirect:/edit/" + book.getId();
    }

    @GetMapping("/deleteBook")
    public String deleteBook(@RequestParam("id") String bookId){
        Book book = bookService.findById(bookId).orElseThrow(() -> new RuntimeException("no book with id " + bookId));
        bookService.delete(book);
        return "redirect:/";
    }

    @GetMapping("/addBook")
    public String addBook(Model model){
        Book book = bookService.saveBook(new Book());
        List<Author> authors=authorService.findAll();
        List<Genre> genres=genreService.findAll();
        model.addAttribute("book", book);
        model.addAttribute("comments", new ArrayList<Comment>());
        model.addAttribute("authors",authors);
        model.addAttribute("genres",genres);
        return "editBook";
    }

}