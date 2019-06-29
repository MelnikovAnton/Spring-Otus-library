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
import java.util.Optional;
import java.util.stream.Collectors;

@ShellComponent(value = "Book shell commands")
@RequiredArgsConstructor
@ShellCommandGroup("Book commands")
@Slf4j
public class BookShellCommands {

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

    @ShellMethod(value = "find all Book", key = {"findAllBook", "fba"})
    private Table findAllBooks() {
        List<Book> books = bookService.findAll();
        return getBooksTable(books);
    }

    @ShellMethod(value = "delete Book", key = {"deleteBook", "delb"})
    private String deleteBook(@ShellOption(value = {"-i", "--id"}) int id) {

        Optional<Book> oBook = bookService.findById(id);
        if (oBook.isEmpty()) return "no book with id " + id;
        int r = bookService.delete(oBook.get());
        return "Book deleted rows=" + r;
    }

    @ShellMethod(value = "add Book", key = {"addBook", "addb"})
    private String addBook(@ShellOption(value = {"-t", "--title"}) String title,
                           @ShellOption(value = {"-c", "--content"}) String contentPath) {
        Book book = new Book(title, contentPath);
        bookService.saveBook(book);
        return "Book added id=" + book.getId();
    }

    @ShellMethod(value = "set Genre to book", key = {"setGenre", "setg"})
    private String setGenre(@ShellOption(value = {"-b", "--book"}) int bookId,
                            @ShellOption(value = {"-g", "--genre"}) int genreId) {

        Optional<Book> oBook = bookService.findById(bookId);
        if (oBook.isEmpty()) return "no book with id " + bookId;
        Book book = oBook.get();

        Optional<Genre> oGenre = genreService.findById(genreId);
        if (oGenre.isEmpty()) return "no Genre with id " + genreId;
        Genre genre = oGenre.get();

        book.addGenre(genre);
        bookService.addRelations(book);
        return "Genre added to book ";
    }


    @ShellMethod(value = "set Author to book", key = {"setAuthor", "seta"})
    private String setAuthor(@ShellOption(value = {"-b", "--book"}) int bookId,
                             @ShellOption(value = {"-a", "--author"}) int authorId) {

        Optional<Book> oBook = bookService.findById(bookId);
        if (oBook.isEmpty()) return "no book with id " + bookId;
        Book book = oBook.get();

        Optional<Author> oAuthor = authorService.findById(authorId);
        if (oAuthor.isEmpty()) return "no Author with id " + authorId;
        Author author = oAuthor.get();

        book.addAuthor(author);
        bookService.addRelations(book);
        return "Author added to book ";
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
                }
        );

        TableBuilder builder = new TableBuilder(modelBuilder.build());

        return builder.addFullBorder(BorderStyle.fancy_double).build();
    }
}
