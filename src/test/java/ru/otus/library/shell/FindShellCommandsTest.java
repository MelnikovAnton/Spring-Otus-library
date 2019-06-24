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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false"})
class FindShellCommandsTest {

    private final List<String> COMMANDS = Arrays.asList("fa", "faa", "fba", "fga", "fb", "fg");

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

    @TestFactory
    @DisplayName("Поиск авторов")
    List<DynamicTest> findAuthors(){
        DynamicTest byIdExists = DynamicTest.dynamicTest("Поиск по ID автор есть", () -> {
            when(authorService.findById(anyInt())).thenReturn(Optional.of(getTestAuthor()));
            Table r = (Table) shell.evaluate(() -> "fa -i 1");
            int rowCount = r.getModel().getRowCount();
            assertEquals(2, rowCount);
            int id = (int) r.getModel().getValue(1, 0);
            assertEquals(1, id);
        });

        DynamicTest byIdNotExists = DynamicTest.dynamicTest("Поиск по ID автора нет", () -> {
            when(authorService.findById(anyInt())).thenReturn(Optional.empty());
            Table r = (Table) shell.evaluate(() -> "fa -i 10");
            int rowCount = r.getModel().getRowCount();
            assertEquals(1, rowCount);
        });

        DynamicTest byName = DynamicTest.dynamicTest("Поиск по имени", () -> {
            when(authorService.findAuthorsByName(anyString())).thenReturn(List.of(getTestAuthor()));
            Table r = (Table) shell.evaluate(() -> "fa -n 1");
            int rowCount = r.getModel().getRowCount();
            assertEquals(2, rowCount);
            int id = (int) r.getModel().getValue(1, 0);
            assertEquals(1, id);
        });

        return Arrays.asList(byIdExists, byIdNotExists,byName);
    }

    @TestFactory
    @DisplayName("Поиск жанров")
    List<DynamicTest> findGenres(){
        DynamicTest byIdExists = DynamicTest.dynamicTest("Поиск по ID жанр есть", () -> {
            when(genreService.findById(anyInt())).thenReturn(Optional.of(getTestGenre()));
            Table r = (Table) shell.evaluate(() -> "fg -i 1");
            int rowCount = r.getModel().getRowCount();
            assertEquals(2, rowCount);
            int id = (int) r.getModel().getValue(1, 0);
            assertEquals(1, id);
        });

        DynamicTest byIdNotExists = DynamicTest.dynamicTest("Поиск по ID жанра нет", () -> {
            when(genreService.findById(anyInt())).thenReturn(Optional.empty());
            Table r = (Table) shell.evaluate(() -> "fg -i 10");
            int rowCount = r.getModel().getRowCount();
            assertEquals(1, rowCount);
        });

        DynamicTest byName = DynamicTest.dynamicTest("Поиск по имени", () -> {
            when(genreService.findGenresByName(anyString())).thenReturn(List.of(getTestGenre()));
            Table r = (Table) shell.evaluate(() -> "fg -n Test");
            int rowCount = r.getModel().getRowCount();
            assertEquals(2, rowCount);
            int id = (int) r.getModel().getValue(1, 0);
            assertEquals(1, id);
        });

        return Arrays.asList(byIdExists, byIdNotExists,byName);
    }

    @Test
    @DisplayName("Получить все книги")
    void getAllBooks(){
        shell.evaluate(()->"fba");
        verify(bookService,times(1)).findAll();
    }

    @Test
    @DisplayName("Получить всех авторов")
    void getAllAuthors(){
        shell.evaluate(()->"faa");
        verify(authorService,times(1)).findAll();
    }

    @Test
    @DisplayName("Получить все жанры")
    void getAllGenres(){
        shell.evaluate(()->"fga");
        verify(genreService,times(1)).findAll();
    }

    private Book getTestBook() {
        Book testBook = new Book("Test", "Test");
        testBook.setId(1);
        testBook.setAuthors(List.of(new Author(1, "Auth1"),
                new Author(2, "Auth2"),
                new Author(3, "Auth3")));
        testBook.setGenres(List.of(new Genre(1, "Genre1"),
                new Genre(2, "Genre2"),
                new Genre(3, "Genre3")));
        return testBook;
    }

    private Author getTestAuthor(){
        return new Author(1,"Test");
    }

    private Genre getTestGenre(){
        return new Genre(1,"Test");
    }
}