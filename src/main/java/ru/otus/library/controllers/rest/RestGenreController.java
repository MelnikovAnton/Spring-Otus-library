package ru.otus.library.controllers.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.otus.library.model.Genre;
import ru.otus.library.services.GenreService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("genreApi")
public class RestGenreController {

    private final GenreService genreService;

    @GetMapping
    public List<Genre> getBookList() {
        return genreService.findAll();
    }

    @GetMapping("{id}")
    public Genre getBook(@PathVariable("id") String id) {
        return genreService.findById(id).orElseThrow(() -> new RuntimeException("no Genre with id " + id));
    }

    @PostMapping
    public Genre create(@RequestBody Genre genre) {
        return genreService.saveGenre(genre);
    }

    @PutMapping("{id}")
    public Genre update(@RequestBody Genre genre,
                         @PathVariable String id) {
        genreService.findById(id).orElseThrow(() -> new RuntimeException("no Genre with id " + id));
        genre.setId(id);
        return genreService.saveGenre(genre);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable String id) {
        Genre genre = genreService.findById(id).orElseThrow(() -> new RuntimeException("no Genre with id " + id));
        genreService.delete(genre);
    }

}
