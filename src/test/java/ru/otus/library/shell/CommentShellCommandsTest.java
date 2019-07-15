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
import ru.otus.library.model.Book;
import ru.otus.library.model.Comment;
import ru.otus.library.services.BookService;
import ru.otus.library.services.CommentService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false"})
class CommentShellCommandsTest {

    private final List<String> COMMANDS = Arrays.asList("delc", "fca", "fc", "addc");

    @Autowired
    private Shell shell;
    @MockBean
    private CommentService commentService;
    @MockBean
    private BookService bookService;

    @Test
    @DisplayName("Все команды есть.")
    void testAllCommandsExists() {
        Set<String> keys = shell.listCommands().keySet();
        assertTrue(keys.containsAll(COMMANDS));
    }

    @TestFactory
    @DisplayName("Добавление коментария.")
    List<DynamicTest> testAddComment() {
        DynamicTest success = DynamicTest.dynamicTest("Добавление коментария в книгу", ()-> {
            when(commentService.saveComment(any(Comment.class))).thenAnswer(invocation -> {
                Comment comment = (Comment) invocation.getArgument(0);
                comment.setId("1");
                return comment;
            });
            when(bookService.findById(anyString())).thenReturn(Optional.of(new Book("1", "test", "Test")));

            String r = (String) shell.evaluate(() -> "addc -b 1 -c test");
            assertEquals("Comment added id=1", r);
        });

        DynamicTest noBook = DynamicTest.dynamicTest("Добавление коментария в книгу с неверным id", ()-> {
            when(commentService.saveComment(any(Comment.class))).thenAnswer(invocation -> {
                Comment comment = (Comment) invocation.getArgument(0);
                comment.setId("1");
                return comment;
            });
            when(bookService.findById(anyString())).thenReturn(Optional.empty());

            String r = (String) shell.evaluate(() -> "addc -b 1 -c test");
            assertEquals("No book with id 1", r);
        });

        return List.of(success,noBook);

    }


    @TestFactory
    @DisplayName("Поиск коментария")
    List<DynamicTest> findCommentss() {
        DynamicTest byIdExists = DynamicTest.dynamicTest("Поиск по ID,коментарий есть", () -> {
            when(commentService.findById(anyString())).thenReturn(Optional.of(getTestComment()));
            Table r = (Table) shell.evaluate(() -> "fc -i 1");
            int rowCount = r.getModel().getRowCount();
            assertEquals(2, rowCount);
            String id = (String) r.getModel().getValue(1, 0);
            assertEquals("1", id);
        });

        DynamicTest byIdNotExists = DynamicTest.dynamicTest("Поиск по ID, коментария нет", () -> {
            when(commentService.findById(anyString())).thenReturn(Optional.empty());
            Table r = (Table) shell.evaluate(() -> "fc -i 10");
            int rowCount = r.getModel().getRowCount();
            assertEquals(1, rowCount);
        });

        DynamicTest byBook = DynamicTest.dynamicTest("Поиск по книге", () -> {
            when(commentService.findCommentsByBook(any(Book.class))).thenReturn(getTestComments());
            when(bookService.findById(anyString())).thenReturn(Optional.of(new Book("1", "test", "Test")));
            Table r = (Table) shell.evaluate(() -> "fc -b 1");
            int rowCount = r.getModel().getRowCount();
            assertEquals(4, rowCount);
            String id = (String) r.getModel().getValue(1, 0);
            assertEquals("1", id);
        });

        return Arrays.asList(byIdExists, byIdNotExists, byBook);
    }

    @Test
    @DisplayName("Получить все коментарии")
    void getAllGenres() {
        when(commentService.findAll()).thenReturn(getTestComments());

        Table r = (Table) shell.evaluate(() -> "fca");

        int rowCount = r.getModel().getRowCount();
        assertEquals(4, rowCount);
        String id = (String) r.getModel().getValue(1, 0);
        assertEquals("1", id);

    }


    @TestFactory
    @DisplayName("Удаление коментария")
    List<DynamicTest> testDeleteGenre() {
        DynamicTest delBook = DynamicTest.dynamicTest("Удаление коментария", () -> {
            when(commentService.findById(anyString())).thenReturn(Optional.of(getTestComment()));

            String r = (String) shell.evaluate(() -> "delc 1");
            assertTrue(r.contains("Comment deleted id="));

        });
        DynamicTest delWrongBook = DynamicTest.dynamicTest("Удаление коментария с неверным ID", () -> {
            when(commentService.findById(anyString())).thenReturn(Optional.empty());
            String r = (String) shell.evaluate(() -> "delc -i 1");
            assertTrue(r.contains("no comment with id"));
        });

        return Arrays.asList(delWrongBook, delBook);
    }


    private Comment getTestComment() {
        Book book = new Book("1","Test","Test");
        Comment comment=new Comment(book,"Test");
        comment.setId("1");
        return comment;
    }

    private List<Comment> getTestComments() {
        Book book = new Book("1","Test","Test");
        Comment comment = new Comment( book,"Test");
        comment.setId("1");
        return List.of(comment,
                new Comment(book,"Test"),
                new Comment(book,"Test"));
    }

}