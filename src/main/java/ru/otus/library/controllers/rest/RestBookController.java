package ru.otus.library.controllers.rest;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.otus.library.model.Book;
import ru.otus.library.services.BookService;

import java.util.List;

@RestController
@RequestMapping("bookApi")
@RequiredArgsConstructor
public class RestBookController {
    private final BookService bookService;

    @GetMapping
    public List<Book> getBookList() {
        return bookService.findAll();
    }

    @GetMapping("{id}")
    public Book getBook(@PathVariable("id") String id) {
        return bookService.findById(id).orElseThrow(() -> new RuntimeException("no book with id " + id));
    }

    @PostMapping
    public Book create(@RequestBody Book book) {
        return bookService.saveBook(book);
    }

    @PutMapping("{id}")
    public Book update(@RequestBody Book book,
                       @PathVariable String id) {
        bookService.findById(id).orElseThrow(() -> new RuntimeException("no book with id " + id));
        book.setId(id);
        return bookService.saveBook(book);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable String id) {
        Book book = bookService.findById(id).orElseThrow(() -> new RuntimeException("no book with id " + id));
        bookService.delete(book);
    }

}
