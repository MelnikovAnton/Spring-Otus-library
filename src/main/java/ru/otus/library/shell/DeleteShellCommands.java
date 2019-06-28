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

import java.util.Optional;

//@ShellComponent(value = "Delete shell commands")
//@RequiredArgsConstructor
//@ShellCommandGroup("Delete commands")
//@Slf4j
public class DeleteShellCommands {

//
//    private final BookService bookService;
//    private final GenreService genreService;
//    private final AuthorService authorService;
//
//    @ShellMethod(value = "delete Author", key = {"deleteAuthor", "dela"})
//    private String deleteAuthor(@ShellOption(value = {"-i", "--id"}) int id) {
//        Optional<Author> oAuthor = authorService.findById(id);
//        if (oAuthor.isEmpty()) return "no author with id " + id;
//        int r = authorService.delete(oAuthor.get());
//        return "Author deleted rows=" + r;
//    }
//
//    @ShellMethod(value = "delete Author", key = {"deleteGenre", "delg"})
//    private String deleteGenre(@ShellOption(value = {"-i", "--id"}) int id) {
//        Optional<Genre> oGenre = genreService.findById(id);
//        if (oGenre.isEmpty()) return "no genre with id " + id;
//        int r = genreService.delete(oGenre.get());
//        return "Genre deleted rows=" + r;
//    }
//
//    @ShellMethod(value = "delete Book", key = {"deleteBook", "delb"})
//    private String deleteBook(@ShellOption(value = {"-i", "--id"}) int id) {
//
//        Optional<Book> oBook = bookService.findById(id);
//        if (oBook.isEmpty()) return "no book with id " + id;
//        int r = bookService.delete(oBook.get());
//        return "Book deleted rows=" + r;
//    }
}
