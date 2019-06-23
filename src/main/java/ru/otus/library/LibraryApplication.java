package ru.otus.library;

import org.h2.tools.Console;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import ru.otus.library.dao.BookDao;
import ru.otus.library.model.Author;
import ru.otus.library.model.Book;
import ru.otus.library.model.Genre;
import ru.otus.library.services.BookService;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class LibraryApplication {

    public static void main(String[] args) throws SQLException {
        Console.main(args);

        ApplicationContext ctx = SpringApplication.run(LibraryApplication.class, args);


        BookService service = ctx.getBean(BookService.class);


        Author author1 = new Author("TestAuthor1");
        Author author2 = new Author("TestAuthor2");

        Genre genre1 = new Genre("TestGenre1");
        Genre genre2 = new Genre("TestGenre2");


        Book testBook = new Book("Title", "Path");
        testBook.setAuthors(Arrays.asList(author1, author2));
        testBook.setGenres(Arrays.asList(genre1,genre2));
        System.out.println(testBook);
        service.saveBook(testBook);
        System.out.println(testBook);


    }

}
