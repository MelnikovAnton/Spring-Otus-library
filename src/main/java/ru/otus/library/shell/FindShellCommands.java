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
import ru.otus.library.model.Author;
import ru.otus.library.model.Book;
import ru.otus.library.model.Genre;
import ru.otus.library.services.AuthorService;
import ru.otus.library.services.BookService;
import ru.otus.library.services.GenreService;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@ShellComponent(value = "Find shell commands")
@RequiredArgsConstructor
@ShellCommandGroup("Find commands")
@Slf4j
public class FindShellCommands {

    private final BookService bookService;
    private final GenreService genreService;
    private final AuthorService authorService;

    @ShellMethod(value = "find Book", key = {"findBook", "fb"})
    private Table findBooks(@ShellOption(value = {"-i", "--id"}, defaultValue = "-1") int id,
                            @ShellOption(value = {"-t", "--title"}, defaultValue = "") String title,
                            @ShellOption(value = {"-gn", "--genre-name"}, defaultValue = "") String genre,
                            @ShellOption(value = {"-an", "--author-name"}, defaultValue = "") String author) {

        List<Book> books = new ArrayList<>();

        if (id > 0) bookService.findById(id).ifPresent(books::add);
        if (!title.isEmpty()) books.addAll(bookService.findBooksByTitle(title));
        if (!genre.isEmpty()) books.addAll(bookService.findBooksByGenre(genre));
        if (!author.isEmpty()) books.addAll(bookService.findBooksByAuthor(author));

        return getBooksTable(books);
    }

    @ShellMethod(value = "find Authors", key = {"findAuthors", "fa"})
    private Table findAuthors(@ShellOption(value = {"-i", "--id"}, defaultValue = "-1") int id,
                              @ShellOption(value = {"-n", "--name"}, defaultValue = "") String name) {

        List<Author> authors = new ArrayList<>();
        if (id > 0) authorService.findById(id).ifPresent(authors::add);
        if (!name.isEmpty()) authors.addAll(authorService.findAuthorsByName(name));
        return getAuthorsTable(authors);
    }


    @ShellMethod(value = "find Genres", key = {"findGenres", "fg"})
    private Table findGenres(@ShellOption(value = {"-i", "--id"}, defaultValue = "-1") int id,
                             @ShellOption(value = {"-n", "--name"}, defaultValue = "") String name) {

        List<Genre> genres = new ArrayList<>();
        if (id > 0)genreService.findById(id).ifPresent(genres::add);
        if (!name.isEmpty()) genres.addAll(genreService.findGenresByName(name));
        return getGenresTable(genres);
    }

    @ShellMethod(value = "find all Book", key = {"findAllBook", "fba"})
    private Table findAllBooks() {
        List<Book> books = bookService.findAll();
        return getBooksTable(books);
    }

    @ShellMethod(value = "find all Genres", key = {"findAllGenres", "fga"})
    private Table findAllGenres() {
        List<Genre> genres = genreService.findAll();
        return getGenresTable(genres);
    }

    @ShellMethod(value = "find all Authors", key = {"findAllAuthors", "faa"})
    private Table findAllAuthors() {
        List<Author> authors = authorService.findAll();
        return getAuthorsTable(authors);
    }

    private Table getBooksTable(List<Book> books) {
        TableModelBuilder<Object> modelBuilder = new TableModelBuilder<>();
        modelBuilder.addRow()
                .addValue("id")
                .addValue("Title")
                .addValue("Content Path")
                .addValue("Authors")
                .addValue("Genres");
        books.forEach(b -> {
                    modelBuilder.addRow()
                            .addValue(b.getId())
                            .addValue(b.getTitle())
                            .addValue(b.getContentPath())
                            .addValue(b.getAuthors().stream()
                                    .map(Author::getName)
                                    .collect(Collectors.joining(",")))
                            .addValue(b.getGenres().stream()
                                    .map(Genre::getName)
                                    .collect(Collectors.joining(",")));
                    ;
                }
        );

        TableBuilder builder = new TableBuilder(modelBuilder.build());

        return builder.addFullBorder(BorderStyle.fancy_double).build();
    }

    private Table getGenresTable(List<Genre> genres) {
        TableModelBuilder<Object> modelBuilder = new TableModelBuilder<>();
        modelBuilder.addRow()
                .addValue("id")
                .addValue("Name");
        genres.forEach(g -> {
                    modelBuilder.addRow()
                            .addValue(g.getId())
                            .addValue(g.getName());
                }
        );

        TableBuilder builder = new TableBuilder(modelBuilder.build());

        return builder.addFullBorder(BorderStyle.fancy_double).build();
    }

    private Table getAuthorsTable(List<Author> authors) {
        TableModelBuilder<Object> modelBuilder = new TableModelBuilder<>();
        modelBuilder.addRow()
                .addValue("id")
                .addValue("Name");
        authors.forEach(a -> {
                    modelBuilder.addRow()
                            .addValue(a.getId())
                            .addValue(a.getName());
                }
        );

        TableBuilder builder = new TableBuilder(modelBuilder.build());

        return builder.addFullBorder(BorderStyle.fancy_double).build();
    }
}
