package ru.otus.library.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.otus.library.model.Author;
import ru.otus.library.services.AuthorService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

//    @GetMapping("/authorList")
//    public String authorList(Model model) {
//        List<Author> authors = authorService.findAll();
//        model.addAttribute("authors", authors);
//        return "authorList";
//    }
//
//    @GetMapping("/editAuthor/{id}")
//    public String editAuthor(@PathVariable("id") String id, Model model) {
//        Author author = authorService.findById(id).orElseThrow(() -> new RuntimeException("no author with id " + id));
//        model.addAttribute("author", author);
//        return "editAuthor";
//    }
//
//    @PostMapping(value = "/editAuthor/{id}")
//    public String postAuthor(@ModelAttribute Author author) {
//        authorService.saveAuthor(author);
//        return "redirect:/editAuthor/" + author.getId();
//    }
//
//    @GetMapping("/deleteAuthor")
//    public String deleteAuthor(@RequestParam("id") String id) {
//        Author author = authorService.findById(id).orElseThrow(() -> new RuntimeException("no author with id " + id));
//        authorService.delete(author);
//        return "redirect:/authorList";
//    }
//
//    @GetMapping("/addAuthor")
//    public String addBook(Model model){
//        Author author = authorService.saveAuthor(new Author());
//        model.addAttribute("author", author);
//        return "editAuthor";
//    }
}
