package ru.otus.library.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.library.dao.CommentDao;
import ru.otus.library.model.Book;
import ru.otus.library.model.Comment;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false"})
class CommentServiceTest {

    @MockBean
    private CommentDao commentDao;
    @Autowired
    private CommentService commentService;

    @Test
    void saveComment() {
        Comment comment = new Comment();

        doAnswer(inv -> {
            Comment c = inv.getArgument(0);
            c.setId(1);
            return null;
        }).when(commentDao).save(any(Comment.class));

        Comment c  = assertDoesNotThrow(() -> commentService.saveComment(comment));
        assertEquals(c, comment);
        assertEquals(1, c.getId());
    }

    @Test
    void findCommentsByBook() {
        Book book = new Book("Test","Test");
        when(commentDao.findByBookId(anyLong())).thenReturn(getTestComments());
        assertEquals(getTestComments(),commentService.findCommentsByBook(book));
    }


    @Test
    void findAll() {
        when(commentDao.findAll()).thenReturn(getTestComments());

        List<Comment> comments = assertDoesNotThrow(() -> commentService.findAll());
        assertEquals(getTestComments(), comments);
    }

    @TestFactory
    @DisplayName("Поиск по ID")
    List<DynamicTest> findById() {
        DynamicTest isPresent = DynamicTest.dynamicTest("Коментарий найден", () -> {
            when(commentDao.findById(anyLong())).thenReturn(Optional.of(new Comment()));
            Optional<Comment> genre = commentService.findById(1);
            assertTrue(genre.isPresent());
        });
        DynamicTest isNotPresent = DynamicTest.dynamicTest("Коментарий не найден", () -> {
            doThrow(new EmptyResultDataAccessException(1)).when(commentDao).findById(anyLong());
            Optional<Comment> genre = assertDoesNotThrow(() -> commentService.findById(1));
            assertTrue(genre.isEmpty());
        });
        return Arrays.asList(isPresent, isNotPresent);
    }

    @Test
    void delete() {
        doAnswer(invocation -> {
            Comment c = invocation.getArgument(0);
            assertEquals(1, c.getId());
            return null;
        }).when(commentDao).delete(any(Comment.class));

        Comment comment = new Comment();
        comment.setId(1);

        assertDoesNotThrow(() -> commentService.delete(comment));
    }

    private List<Comment> getTestComments() {
        return List.of(new Comment(),new Comment(),new Comment());
    }

}