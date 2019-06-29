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
import ru.otus.library.services.AuthorService;

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
class AuthorShellCommandsTest {

//    private final List<String> COMMANDS = Arrays.asList("dela", "faa", "fa", "adda");
//
//    @Autowired
//    private Shell shell;
//    @MockBean
//    private AuthorService authorService;
//
//    @Test
//    @DisplayName("Все команды есть.")
//    void testAllCommandsExists() {
//        Set<String> keys = shell.listCommands().keySet();
//        assertTrue(keys.containsAll(COMMANDS));
//    }
//
//    @TestFactory
//    @DisplayName("Поиск авторов")
//    List<DynamicTest> findAuthors() {
//        DynamicTest byIdExists = DynamicTest.dynamicTest("Поиск по ID автор есть", () -> {
//            when(authorService.findById(anyInt())).thenReturn(Optional.of(getTestAuthor()));
//            Table r = (Table) shell.evaluate(() -> "fa -i 1");
//            int rowCount = r.getModel().getRowCount();
//            assertEquals(2, rowCount);
//            int id = (int) r.getModel().getValue(1, 0);
//            assertEquals(1, id);
//        });
//
//        DynamicTest byIdNotExists = DynamicTest.dynamicTest("Поиск по ID автора нет", () -> {
//            when(authorService.findById(anyInt())).thenReturn(Optional.empty());
//            Table r = (Table) shell.evaluate(() -> "fa -i 10");
//            int rowCount = r.getModel().getRowCount();
//            assertEquals(1, rowCount);
//        });
//
//        DynamicTest byName = DynamicTest.dynamicTest("Поиск по имени", () -> {
//            when(authorService.findAuthorsByName(anyString())).thenReturn(List.of(getTestAuthor()));
//            Table r = (Table) shell.evaluate(() -> "fa -n 1");
//            int rowCount = r.getModel().getRowCount();
//            assertEquals(2, rowCount);
//            int id = (int) r.getModel().getValue(1, 0);
//            assertEquals(1, id);
//        });
//
//        return Arrays.asList(byIdExists, byIdNotExists, byName);
//    }
//
//    @Test
//    @DisplayName("Получить всех авторов")
//    void getAllAuthors() {
//
//        when(authorService.findAll()).thenReturn(getTestAuthors());
//
//        Table r = (Table) shell.evaluate(() -> "faa");
//
//        int rowCount = r.getModel().getRowCount();
//        assertEquals(4, rowCount);
//        int id = (int) r.getModel().getValue(1, 0);
//        assertEquals(1, id);
//    }
//
//
//    @TestFactory
//    @DisplayName("Удаление автора")
//    List<DynamicTest> testDeleteAuthor() {
//        DynamicTest delBook = DynamicTest.dynamicTest("Удаление автора", () -> {
//            when(authorService.findById(anyInt())).thenReturn(Optional.of(getTestAuthor()));
//
//            String r = (String) shell.evaluate(() -> "dela 1");
//            assertTrue(r.contains("Author deleted rows="));
//        });
//        DynamicTest delWrongBook = DynamicTest.dynamicTest("Удаление автора с неверным ID", () -> {
//            when(authorService.findById(anyInt())).thenReturn(Optional.empty());
//            String r = (String) shell.evaluate(() -> "dela -i 1");
//            assertTrue(r.contains("no author with id"));
//        });
//
//        return Arrays.asList(delWrongBook, delBook);
//    }
//
//    @Test
//    @DisplayName("Добавление автора.")
//    void testAddAuthor() {
//
//        when(authorService.saveAuthor(any(Author.class))).thenAnswer(invocation -> {
//            Author genre = invocation.getArgument(0);
//            genre.setId(1);
//            return genre;
//        });
//        String r = (String) shell.evaluate(() -> "adda Name");
//        assertEquals("Author added id=1", r);
//
//        verify(authorService, times(1)).saveAuthor(any(Author.class));
//    }
//
//    private Author getTestAuthor() {
//        return new Author(1, "Test");
//    }
//
//    private List<Author> getTestAuthors() {
//        return List.of(new Author(1, "Test"),
//                new Author(2, "Test"),
//                new Author(3, "Test"));
//    }

}