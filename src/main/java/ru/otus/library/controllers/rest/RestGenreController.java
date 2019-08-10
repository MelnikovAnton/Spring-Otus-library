package ru.otus.library.controllers.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.otus.library.model.Genre;
import ru.otus.library.services.GenreService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RestGenreController {

    private final GenreService genreService;

    @GetMapping("genres")
    public List<Genre> getAllGenres() {
        return genreService.findAll();
    }

    @GetMapping("genres/{id}")
    public Genre getGenreById(@PathVariable("id") String id) {
        return genreService.findById(id).orElseThrow(() -> new RuntimeException("no Genre with id " + id));
    }

    @PostMapping("genres")
    public Genre create(@RequestBody Genre genre) {
        return genreService.saveGenre(genre);
    }

    @PutMapping("genres/{id}")
    public Genre update(@RequestBody Genre genre,
                         @PathVariable String id) {
        genreService.findById(id).orElseThrow(() -> new RuntimeException("no Genre with id " + id));
        genre.setId(id);
        return genreService.saveGenre(genre);
    }

    @DeleteMapping("genres/{id}")
    public void delete(@PathVariable String id) {
        Genre genre = genreService.findById(id).orElseThrow(() -> new RuntimeException("no Genre with id " + id));
        genreService.delete(genre);
    }

}
