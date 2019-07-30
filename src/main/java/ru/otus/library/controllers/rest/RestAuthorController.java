package ru.otus.library.controllers.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.otus.library.model.Author;
import ru.otus.library.services.AuthorService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("authorApi")
public class RestAuthorController {

    private final AuthorService authorService;

    @GetMapping
    public List<Author> getBookList() {
        return authorService.findAll();
    }

    @GetMapping("{id}")
    public Author getBook(@PathVariable("id") String id) {
        return authorService.findById(id).orElseThrow(() -> new RuntimeException("no Author with id " + id));
    }

    @PostMapping
    public Author create(@RequestBody Author author) {
        return authorService.saveAuthor(author);
    }

    @PutMapping("{id}")
    public Author update(@RequestBody Author author,
                       @PathVariable String id) {
        authorService.findById(id).orElseThrow(() -> new RuntimeException("no author with id " + id));
        author.setId(id);
        return authorService.saveAuthor(author);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable String id) {
        Author author = authorService.findById(id).orElseThrow(() -> new RuntimeException("no Author with id " + id));
        authorService.delete(author);
    }

}
