package ru.otus.library.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.library.model.Book;
import ru.otus.library.services.BookService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService service;

    @GetMapping("/")
    public String getBookList(Model model) {
        List<Book> books = service.findAll();
        model.addAttribute("books", books);
        return "bookList";
    }

    @GetMapping("edit")
    public String editBook(@RequestParam String id, Model model) {
        System.out.println(id);
        return "editBook";
    }

}
