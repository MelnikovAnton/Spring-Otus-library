package ru.otus.library.shell;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.shell.Shell;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.library.model.Author;
import ru.otus.library.model.Book;
import ru.otus.library.model.Genre;
import ru.otus.library.services.AuthorService;
import ru.otus.library.services.BookService;
import ru.otus.library.services.GenreService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false"})
class DeleteShellCommandsTest {

    private final List<String> COMMANDS = Arrays.asList("dela", "delb", "delg");

    @Autowired
    private Shell shell;
    @MockBean
    private BookService bookService;
    @MockBean
    private GenreService genreService;
    @MockBean
    private AuthorService authorService;

    @Test
    @DisplayName("Все команды есть.")
    void testAllCommandsExists() {
        Set<String> keys = shell.listCommands().keySet();
        assertTrue(keys.containsAll(COMMANDS));
    }

    @TestFactory
    @DisplayName("Удаление книги")
    List<DynamicTest> testDeleteBook() {
        DynamicTest delBook = DynamicTest.dynamicTest("Удаление книги", () -> {
            when(bookService.findById(anyInt())).thenReturn(Optional.of(getTestBook()));

            String r = (String) shell.evaluate(() -> "delb 1");
            assertTrue(r.contains("Book deleted rows="));

            verify(bookService, times(1)).delete(any(Book.class));
        });
        DynamicTest delWrongBook = DynamicTest.dynamicTest("Удаление книги с неверным ID", () -> {
            when(bookService.findById(anyInt())).thenReturn(Optional.empty());
            String r = (String) shell.evaluate(() -> "delb -i 1");
            assertTrue(r.contains("no book with id"));

            verify(bookService, times(0)).delete(any(Book.class));
        });

        return Arrays.asList(delWrongBook, delBook);
    }

    @TestFactory
    @DisplayName("Удаление автора")
    List<DynamicTest> testDeleteAuthor() {
        DynamicTest delBook = DynamicTest.dynamicTest("Удаление автора", () -> {
            when(authorService.findById(anyInt())).thenReturn(Optional.of(getTestAuthor()));

            String r = (String) shell.evaluate(() -> "dela 1");
            assertTrue(r.contains("Author deleted rows="));

            verify(authorService, times(1)).delete(any(Author.class));
        });
        DynamicTest delWrongBook = DynamicTest.dynamicTest("Удаление автора с неверным ID", () -> {
            when(authorService.findById(anyInt())).thenReturn(Optional.empty());
            String r = (String) shell.evaluate(() -> "dela -i 1");
            assertTrue(r.contains("no author with id"));

            verify(authorService, times(0)).delete(any(Author.class));
        });

        return Arrays.asList(delWrongBook, delBook);
    }

    @TestFactory
    @DisplayName("Удаление жанра")
    List<DynamicTest> testDeleteGenre() {
        DynamicTest delBook = DynamicTest.dynamicTest("Удаление жанра", () -> {
            when(genreService.findById(anyInt())).thenReturn(Optional.of(getTestGenre()));

            String r = (String) shell.evaluate(() -> "delg 1");
            assertTrue(r.contains("Genre deleted rows="));

            verify(genreService, times(1)).delete(any(Genre.class));
        });
        DynamicTest delWrongBook = DynamicTest.dynamicTest("Удаление жанра с неверным ID", () -> {
            when(genreService.findById(anyInt())).thenReturn(Optional.empty());
            String r = (String) shell.evaluate(() -> "delg -i 1");
            assertTrue(r.contains("no genre with id"));

            verify(genreService, times(0)).delete(any(Genre.class));
        });

        return Arrays.asList(delWrongBook, delBook);
    }


    private Book getTestBook() {
        Book testBook = new Book("Test", "Test");
        testBook.setId(1);
        testBook.addAuthor(new Author(1, "Auth1"));
        testBook.addAuthor(new Author(2, "Auth2"));
        testBook.addAuthor(new Author(3, "Auth3"));

        testBook.addGenre(new Genre(1, "Genre1"));
        testBook.addGenre(new Genre(2, "Genre2"));
        testBook.addGenre(new Genre(3, "Genre3"));

        return testBook;
    }

    private Author getTestAuthor() {
        return new Author(5, "Test");
    }

    private Genre getTestGenre() {
        return new Genre(5, "Test");
    }
}