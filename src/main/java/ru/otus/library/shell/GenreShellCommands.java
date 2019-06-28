package ru.otus.library.shell;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.Table;
import org.springframework.shell.table.TableBuilder;
import org.springframework.shell.table.TableModelBuilder;
import ru.otus.library.model.Genre;
import ru.otus.library.services.GenreService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@ShellComponent(value = "Genre shell commands")
@RequiredArgsConstructor
@ShellCommandGroup("Genre commands")
@Slf4j
public class GenreShellCommands {
    //TODO Implements Tests
    private final GenreService genreService;


    @ShellMethod(value = "delete Author", key = {"deleteGenre", "delg"})
    private String deleteGenre(@ShellOption(value = {"-i", "--id"}) int id) {
        Optional<Genre> oGenre = genreService.findById(id);
        if (oGenre.isEmpty()) return "no genre with id " + id;
        int r = genreService.delete(oGenre.get());
        return "Genre deleted rows=" + r;
    }

    @ShellMethod(value = "add Genre", key = {"addGenre", "addg"})
    private String addGenre(@ShellOption(value = {"-n", "--name"}) String name) {
        Genre genre = new Genre(name);
        genreService.saveGenre(genre);
        return "Genre added id=" + genre.getId();
    }

    @ShellMethod(value = "find Genres", key = {"findGenres", "fg"})
    private Table findGenres(@ShellOption(value = {"-i", "--id"}, defaultValue = "-1") int id,
                             @ShellOption(value = {"-n", "--name"}, defaultValue = "") String name) {

        List<Genre> genres = new ArrayList<>();
        if (id > 0) genreService.findById(id).ifPresent(genres::add);
        if (!name.isEmpty()) genres.addAll(genreService.findGenresByName(name));
        return getGenresTable(genres);
    }

    @ShellMethod(value = "find all Genres", key = {"findAllGenres", "fga"})
    private Table findAllGenres() {
        List<Genre> genres = genreService.findAll();
        return getGenresTable(genres);
    }

    private Table getGenresTable(List<Genre> genres) {
        TableModelBuilder<Object> modelBuilder = new TableModelBuilder<>();
        modelBuilder.addRow()
                .addValue("id")
                .addValue("Name");
        genres.forEach(g -> modelBuilder.addRow()
                .addValue(g.getId())
                .addValue(g.getName())
        );

        TableBuilder builder = new TableBuilder(modelBuilder.build());

        return builder.addFullBorder(BorderStyle.fancy_double).build();
    }
}
