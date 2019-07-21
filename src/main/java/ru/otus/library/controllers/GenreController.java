package ru.otus.library.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.otus.library.model.Genre;
import ru.otus.library.services.GenreService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class GenreController {

    private final GenreService genreService;


    @GetMapping("/genreList")
    public String genreList(Model model) {
        List<Genre> genres = genreService.findAll();
        model.addAttribute("genres", genres);
        return "genreList";
    }

    @GetMapping("editGenre/{id}")
    public String editAuthor(@PathVariable("id") String id, Model model) {
        Genre genre = genreService.findById(id).orElseThrow(() -> new RuntimeException("no genre with id " + id));
        model.addAttribute("genre", genre);
        return "editGenre";
    }

    @PostMapping(value = "editGenre/{id}")
    public String postBook(@ModelAttribute Genre genre) {
       genreService.saveGenre(genre);
        return "redirect:/editGenre/" + genre.getId();
    }

    @GetMapping("/deleteGenre")
    public String deleteAuthor(@RequestParam("id") String id) {
        Genre genre = genreService.findById(id).orElseThrow(() -> new RuntimeException("no genre with id " + id));
        genreService.delete(genre);
        return "redirect:/genreList";
    }

    @GetMapping("/addGenre")
    public String addBook(Model model){
        Genre genre = genreService.saveGenre(new Genre());
        model.addAttribute("genre", genre);
        return "editGenre";
    }
}
