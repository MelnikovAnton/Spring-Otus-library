package ru.otus.library.controllers.rest;


import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.otus.library.model.Book;
import ru.otus.library.services.BookService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class RestBookController {
    private final BookService bookService;

    @GetMapping("/books")
    public List<Book> getBookList() {
      return bookService.findAll();
    }

    @GetMapping("books/{id}")
    public Book getBook(@PathVariable("id") String id) {
        return bookService.findById(id).orElseThrow(() -> new RuntimeException("no book with id " + id));
    }

    @PostMapping("books")
    public Book create(@RequestBody Book book) {
        return bookService.saveBook(book);
    }

    @PutMapping("books/{id}")
    public Book update(@RequestBody Book book,
                       @PathVariable String id) {
        bookService.findById(id).orElseThrow(() -> new RuntimeException("no book with id " + id));
        book.setId(id);
        return bookService.saveBook(book);
    }

    @DeleteMapping("books/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable String id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(auth.getPrincipal());
        Book book = bookService.findById(id).orElseThrow(() -> new RuntimeException("no book with id " + id));
        bookService.delete(book);
    }

}
