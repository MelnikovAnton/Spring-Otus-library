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
import ru.otus.library.services.AuthorService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ShellComponent(value = "Author shell commands")
@RequiredArgsConstructor
@ShellCommandGroup("Author commands")
@Slf4j
public class AuthorShellCommands {

    private final AuthorService authorService;

    @ShellMethod(value = "delete Author", key = {"deleteAuthor", "dela"})
    private String deleteAuthor(@ShellOption(value = {"-i", "--id"}) long id) {
        Optional<Author> oAuthor = authorService.findById(id);
        if (oAuthor.isEmpty()) return "no author with id " + id;
        long r = authorService.delete(oAuthor.get());
        return "Author deleted rows=" + r;
    }

    @ShellMethod(value = "add Author", key = {"addAuthor", "adda"})
    private String addAuthor(@ShellOption(value = {"-n", "--name"}) String name) {
        Author author = new Author(name);
        authorService.saveAuthor(author);
        return "Author added id=" + author.getId();
    }

    @ShellMethod(value = "find Authors", key = {"findAuthors", "fa"})
    private Table findAuthors(@ShellOption(value = {"-i", "--id"}, defaultValue = "-1") long id,
                              @ShellOption(value = {"-n", "--name"}, defaultValue = "") String name) {

        List<Author> authors = new ArrayList<>();
        if (id > 0) authorService.findById(id).ifPresent(authors::add);
        if (!name.isEmpty()) authors.addAll(authorService.findAuthorsByName(name));
        return getAuthorsTable(authors);
    }


    @ShellMethod(value = "find all Authors", key = {"findAllAuthors", "faa"})
    private Table findAllAuthors() {
        List<Author> authors = authorService.findAll();
        return getAuthorsTable(authors);
    }

    private Table getAuthorsTable(List<Author> authors) {
        TableModelBuilder<Object> modelBuilder = new TableModelBuilder<>();
        modelBuilder.addRow()
                .addValue("id")
                .addValue("Name");
        authors.forEach(a -> modelBuilder.addRow()
                .addValue(a.getId())
                .addValue(a.getName())
        );

        TableBuilder builder = new TableBuilder(modelBuilder.build());

        return builder.addFullBorder(BorderStyle.fancy_double).build();
    }
}
