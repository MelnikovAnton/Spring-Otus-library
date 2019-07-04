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
import ru.otus.library.model.Genre;
import ru.otus.library.services.GenreService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false"})
class GenreShellCommandsTest {


    private final List<String> COMMANDS = Arrays.asList("delg", "fga", "fg", "addg");

    @Autowired
    private Shell shell;
    @MockBean
    private GenreService genreService;

    @Test
    @DisplayName("Все команды есть.")
    void testAllCommandsExists() {
        Set<String> keys = shell.listCommands().keySet();
        assertTrue(keys.containsAll(COMMANDS));
    }

    @Test
    @DisplayName("Добавление жанра.")
    void testAddGenre() {

        when(genreService.saveGenre(any(Genre.class))).thenAnswer(invocation -> {
            Genre genre = (Genre) invocation.getArgument(0);
            genre.setId(1);
            return genre;
        });
        String r = (String) shell.evaluate(() -> "addg Name");
        assertEquals("Genre added id=1", r);
    }


    @TestFactory
    @DisplayName("Поиск жанров")
    List<DynamicTest> findGenres() {
        DynamicTest byIdExists = DynamicTest.dynamicTest("Поиск по ID жанр есть", () -> {
            when(genreService.findById(anyLong())).thenReturn(Optional.of(getTestGenre()));
            Table r = (Table) shell.evaluate(() -> "fg -i 1");
            int rowCount = r.getModel().getRowCount();
            assertEquals(2, rowCount);
            long id = (long) r.getModel().getValue(1, 0);
            assertEquals(1, id);
        });

        DynamicTest byIdNotExists = DynamicTest.dynamicTest("Поиск по ID жанра нет", () -> {
            when(genreService.findById(anyLong())).thenReturn(Optional.empty());
            Table r = (Table) shell.evaluate(() -> "fg -i 10");
            int rowCount = r.getModel().getRowCount();
            assertEquals(1, rowCount);
        });

        DynamicTest byName = DynamicTest.dynamicTest("Поиск по имени", () -> {
            when(genreService.findGenresByName(anyString())).thenReturn(List.of(getTestGenre()));
            Table r = (Table) shell.evaluate(() -> "fg -n Test");
            int rowCount = r.getModel().getRowCount();
            assertEquals(2, rowCount);
            long id = (long) r.getModel().getValue(1, 0);
            assertEquals(1, id);
        });

        return Arrays.asList(byIdExists, byIdNotExists, byName);
    }

    @Test
    @DisplayName("Получить все жанры")
    void getAllGenres() {
        when(genreService.findAll()).thenReturn(getTestGenres());

        Table r = (Table) shell.evaluate(() -> "fga");

        int rowCount = r.getModel().getRowCount();
        assertEquals(4, rowCount);
        long id = (long) r.getModel().getValue(1, 0);
        assertEquals(1, id);

    }


    @TestFactory
    @DisplayName("Удаление жанра")
    List<DynamicTest> testDeleteGenre() {
        DynamicTest delBook = DynamicTest.dynamicTest("Удаление жанра", () -> {
            when(genreService.findById(anyLong())).thenReturn(Optional.of(getTestGenre()));

            String r = (String) shell.evaluate(() -> "delg 1");
            assertTrue(r.contains("Genre deleted id="));

        });
        DynamicTest delWrongBook = DynamicTest.dynamicTest("Удаление жанра с неверным ID", () -> {
            when(genreService.findById(anyLong())).thenReturn(Optional.empty());
            String r = (String) shell.evaluate(() -> "delg -i 1");
            assertTrue(r.contains("no genre with id"));
        });

        return Arrays.asList(delWrongBook, delBook);
    }


    private Genre getTestGenre() {
        return new Genre(1, "Test");
    }

    private List<Genre> getTestGenres() {
        return List.of(new Genre(1, "Test"),
                new Genre(2, "Test"),
                new Genre(3, "Test"));
    }
}