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

//@ShellComponent(value = "Insert shell commands")
//@RequiredArgsConstructor
//@ShellCommandGroup("Insert commands")
//@Slf4j
public class InsertShellCommands {

//    private final BookService bookService;
//    private final GenreService genreService;
//    private final AuthorService authorService;
//
//    @ShellMethod(value = "add Book", key = {"addBook", "addb"})
//    private String addBook(@ShellOption(value = {"-t", "--title"}) String title,
//                           @ShellOption(value = {"-c", "--content"}) String contentPath) {
//        Book book = new Book(title, contentPath);
//        bookService.saveBook(book);
//        return "Book added id=" + book.getId();
//    }
//
//    @ShellMethod(value = "add Author", key = {"addAuthor", "adda"})
//    private String addAuthor(@ShellOption(value = {"-n", "--name"}) String name) {
//        Author author = new Author(name);
//        authorService.saveAuthor(author);
//        return "Author added id=" + author.getId();
//    }
//
//    @ShellMethod(value = "add Genre", key = {"addGenre", "addg"})
//    private String addGenre(@ShellOption(value = {"-n", "--name"}) String name) {
//        Genre genre = new Genre(name);
//        genreService.saveGenre(genre);
//        return "Genre added id=" + genre.getId();
//    }
//
//    @ShellMethod(value = "set Genre to book", key = {"setGenre", "setg"})
//    private String setGenre(@ShellOption(value = {"-b", "--book"}) int bookId,
//                            @ShellOption(value = {"-g", "--genre"}) int genreId) {
//
//        Optional<Book> oBook = bookService.findById(bookId);
//        if (oBook.isEmpty()) return "no book with id " + bookId;
//        Book book = oBook.get();
//
//        Optional<Genre> oGenre = genreService.findById(genreId);
//        if (oGenre.isEmpty()) return "no Genre with id " + genreId;
//        Genre genre = oGenre.get();
//
//        book.addGenre(genre);
//        bookService.addRelations(book);
//        return "Genre added to book ";
//    }
//
//
//    @ShellMethod(value = "set Author to book", key = {"setAuthor", "seta"})
//    private String setAuthor(@ShellOption(value = {"-b", "--book"}) int bookId,
//                             @ShellOption(value = {"-a", "--author"}) int authorId) {
//
//        Optional<Book> oBook = bookService.findById(bookId);
//        if (oBook.isEmpty()) return "no book with id " + bookId;
//        Book book = oBook.get();
//
//        Optional<Author> oAuthor = authorService.findById(authorId);
//        if (oAuthor.isEmpty()) return "no Author with id " + authorId;
//        Author author = oAuthor.get();
//
//        book.addAuthor(author);
//        bookService.addRelations(book);
//        return "Author added to book ";
//    }

}
