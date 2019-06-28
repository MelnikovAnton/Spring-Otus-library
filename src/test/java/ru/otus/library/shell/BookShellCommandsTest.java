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
import org.springframework.shell.table.Table;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false"})
class BookShellCommandsTest {

    private final List<String> COMMANDS = Arrays.asList("delb", "fba","fb", "seta", "setg","addb");

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
    @DisplayName("Поиск книг")
    List<DynamicTest> findBooks() {
        DynamicTest byIdExists = DynamicTest.dynamicTest("Поиск по ID книга есть", () -> {
            when(bookService.findById(anyInt())).thenReturn(Optional.of(getTestBook()));
            Table r = (Table) shell.evaluate(() -> "fb -i 1");
            int rowCount = r.getModel().getRowCount();
            assertEquals(2, rowCount);
            int id = (int) r.getModel().getValue(1, 0);
            assertEquals(1, id);
        });

        DynamicTest byIdNotExists = DynamicTest.dynamicTest("Поиск по ID книги нет", () -> {
            when(bookService.findById(anyInt())).thenReturn(Optional.empty());
            Table r = (Table) shell.evaluate(() -> "fb -i 1");
            int rowCount = r.getModel().getRowCount();
            assertEquals(1, rowCount);
        });
        DynamicTest byTitle = DynamicTest.dynamicTest("Поиск по заголовку", () -> {
            when(bookService.findBooksByTitle(anyString())).thenReturn(List.of(getTestBook()));
            Table r = (Table) shell.evaluate(() -> "fb -t test");
            int rowCount = r.getModel().getRowCount();
            assertEquals(2, rowCount);
            int id = (int) r.getModel().getValue(1, 0);
            assertEquals(1, id);
        });
        DynamicTest byAuthor = DynamicTest.dynamicTest("Поиск по автору", () -> {
            when(bookService.findBooksByAuthor(anyString())).thenReturn(List.of(getTestBook()));
            Table r = (Table) shell.evaluate(() -> "fb -an test");
            int rowCount = r.getModel().getRowCount();
            assertEquals(2, rowCount);
            int id = (int) r.getModel().getValue(1, 0);
            assertEquals(1, id);
        });

        DynamicTest byGenre = DynamicTest.dynamicTest("Поиск по жанру", () -> {
            when(bookService.findBooksByGenre(anyString())).thenReturn(List.of(getTestBook()));
            Table r = (Table) shell.evaluate(() -> "fb -an test");
            int rowCount = r.getModel().getRowCount();
            assertEquals(2, rowCount);
            int id = (int) r.getModel().getValue(1, 0);
            assertEquals(1, id);
        });

        return Arrays.asList(byIdExists, byIdNotExists, byTitle, byAuthor, byGenre);
    }

    @Test
    @DisplayName("Получить все книги")
    void getAllBooks() {
        shell.evaluate(() -> "fba");
        verify(bookService, times(1)).findAll();
    }

    @Test
    @DisplayName("Добавление книги.")
    void testAddBook() {
        assertDoesNotThrow(() -> shell.evaluate(() -> "addb -t Title -c Content"));
        verify(bookService, times(1)).saveBook(any(Book.class));
    }


    @TestFactory
    @DisplayName("Добавление автора в книгу")
    List<DynamicTest> setAuthor() {
        DynamicTest addAuthor = DynamicTest.dynamicTest("Добавление автора", () -> {
            when(authorService.findById(anyInt())).thenReturn(Optional.of(getTestAuthor()));
            when(bookService.findById(anyInt())).thenReturn(Optional.of(getTestBook()));

            String r = (String) shell.evaluate(() -> "seta -b 1 -a 1");
            assertTrue(r.contains("Author added to book"));
        });
        DynamicTest addWrongAuthor = DynamicTest.dynamicTest("Добавление автора с неверным ID", () -> {
            when(authorService.findById(anyInt())).thenReturn(Optional.empty());
            when(bookService.findById(anyInt())).thenReturn(Optional.of(getTestBook()));
            String r = (String) shell.evaluate(() -> "seta -b 1 -a 1");
            assertTrue(r.contains("no Author with id"));
        });
        DynamicTest addWrongBook = DynamicTest.dynamicTest("Добавление книги с неверным ID", () -> {
            when(authorService.findById(anyInt())).thenReturn(Optional.of(getTestAuthor()));
            when(bookService.findById(anyInt())).thenReturn(Optional.empty());
            String r = (String) shell.evaluate(() -> "seta -b 1 -a 1");
            assertTrue(r.contains("no book with id"));
        });

        return Arrays.asList(addAuthor, addWrongAuthor, addWrongBook);
    }

    @TestFactory
    @DisplayName("Добавление жанра в книгу")
    List<DynamicTest> setGenre() {
        DynamicTest addGenre = DynamicTest.dynamicTest("Добавление жанра", () -> {
            when(genreService.findById(anyInt())).thenReturn(Optional.of(getTestGenre()));
            when(bookService.findById(anyInt())).thenReturn(Optional.of(getTestBook()));

            String r = (String) shell.evaluate(() -> "setg -b 1 -g 1");
            assertTrue(r.contains("Genre added to book"));
        });
        DynamicTest addWrongGenre = DynamicTest.dynamicTest("Добавление жанра с неверным ID", () -> {
            when(genreService.findById(anyInt())).thenReturn(Optional.empty());
            when(bookService.findById(anyInt())).thenReturn(Optional.of(getTestBook()));
            String r = (String) shell.evaluate(() -> "setg -b 1 -g 1");
            assertTrue(r.contains("no Genre with id"));
        });
        DynamicTest addWrongBook = DynamicTest.dynamicTest("Добавление книги с неверным ID", () -> {
            when(genreService.findById(anyInt())).thenReturn(Optional.of(getTestGenre()));
            when(bookService.findById(anyInt())).thenReturn(Optional.empty());
            String r = (String) shell.evaluate(() -> "setg -b 1 -g 1");
            assertTrue(r.contains("no book with id"));
        });

        return Arrays.asList(addGenre, addWrongGenre, addWrongBook);
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