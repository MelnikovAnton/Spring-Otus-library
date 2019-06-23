package ru.otus.library.shell;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.library.model.Author;
import ru.otus.library.model.Book;
import ru.otus.library.model.Genre;
import ru.otus.library.services.AuthorService;
import ru.otus.library.services.BookService;
import ru.otus.library.services.GenreService;

@ShellComponent(value = "Delete shell commands")
@RequiredArgsConstructor
@ShellCommandGroup("Delete commands")
@Slf4j
//TODO Logs...
public class DeleteShellCommands {


    private final BookService bookService;
    private final GenreService genreService;
    private final AuthorService authorService;

    @ShellMethod(value = "delete Author", key = {"deleteAuthor", "dela"})
    private String deleteAuthor(@ShellOption(value = {"-i", "--id"}) int id) {
        Author author = authorService.findById(id).orElseThrow();
        authorService.delete(author);
        return "Author deleted" + author;
    }

    @ShellMethod(value = "delete Author", key = {"deleteGenre", "delg"})
    private String deleteGenre(@ShellOption(value = {"-i", "--id"}) int id) {
        Genre genre = genreService.findById(id).orElseThrow();
        genreService.delete(genre);
        return "Genre deleted " + genre;
    }

    @ShellMethod(value = "delete Book", key = {"deleteBook", "delb"})
    private String deleteBook(@ShellOption(value = {"-i", "--id"}) int id) {
        Book book = bookService.findById(id).orElseThrow();
        bookService.delete(book);
        return "Book deleted " + book;
    }
}
