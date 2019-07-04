package ru.otus.library.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.library.dao.impl.CommentDaoImpl;
import ru.otus.library.model.Book;
import ru.otus.library.model.Comment;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({CommentDaoImpl.class})
class CommentDaoTest {

    @Autowired
    private CommentDao commentDao;

    @PersistenceContext
    private EntityManager em;

    @Test
    @DisplayName("Получениене количества комментариев")
    void count() {
        long count = commentDao.count();
        assertEquals(count, 4);
    }

    @TestFactory
    @DisplayName("Добавление коментария")
    List<DynamicTest> insert() {
        DynamicTest correct = DynamicTest.dynamicTest("Добавление коментария и чтение к существующей книге", () -> {
            Book book = new Book();
            book.setId(1);
            Comment comment = new Comment();
            comment.setBook(book);
            assertDoesNotThrow(() -> commentDao.insert(comment));

            em.refresh(comment);
            em.detach(comment);

            Optional<Comment> result = commentDao.findById(comment.getId());
            assertTrue(result.isPresent());
            assertEquals(comment, result.get());
        });
        DynamicTest commentWithoutBook = DynamicTest.dynamicTest("Добавление коментария без книги", () -> {
            Comment comment = new Comment();
            assertThrows(RuntimeException.class, () -> commentDao.insert(comment));
            assertFalse(comment.getId() > 0);
        });
        DynamicTest commentWithWrongBook = DynamicTest.dynamicTest("Добавление коментария к несуществующей книге", () -> {
            Book book = new Book();
            book.setId(Integer.MAX_VALUE + 1);
            Comment comment = new Comment();
            comment.setBook(book);
            assertThrows(RuntimeException.class, () -> commentDao.insert(comment));
            assertFalse(comment.getId() > 0);
        });

        return List.of(correct, commentWithoutBook, commentWithWrongBook);
    }

    @TestFactory
    @DisplayName("Получение коментария по книге")
    List<DynamicTest> findByBook() {
        DynamicTest comment1 = DynamicTest.dynamicTest("ID = 1", () -> {
            List<Comment> comments = assertDoesNotThrow(() -> commentDao.findByBookId(1));
            assertEquals(comments.get(0).getId(), 1);
        });
        DynamicTest comment2 = DynamicTest.dynamicTest("ID = Integer.MAX_VALUE+1", () -> {
            List<Comment> comment = assertDoesNotThrow(() -> commentDao.findByBookId(Integer.MAX_VALUE + 1));
            assertTrue(comment.isEmpty());
        });
        return Arrays.asList(comment1, comment2);
    }

    @Test
    void findAll() {
        List<Comment> comments = commentDao.findAll();
        assertTrue(comments.size() == 4);
    }

    @TestFactory
    @DisplayName("Получение коментария по ID")
    List<DynamicTest> getById() {
        DynamicTest comment1 = DynamicTest.dynamicTest("ID = 1", () -> {
            Comment comment = assertDoesNotThrow(() -> commentDao.findById(1).orElseThrow());
            assertEquals(comment.getId(), 1);
        });
        DynamicTest comment2 = DynamicTest.dynamicTest("ID = Integer.MAX_VALUE+1", () -> {
            Optional<Comment> comment = assertDoesNotThrow(() -> commentDao.findById(Integer.MAX_VALUE + 1));
            assertTrue(comment.isEmpty());
        });
        return Arrays.asList(comment1, comment2);
    }

    @TestFactory
    @DisplayName("Удаление коментария")
    List<DynamicTest> delete() {
        Comment comment = new Comment();
        Book book = new Book();
        book.setId(1);
        comment.setBook(book);
        DynamicTest delExists = DynamicTest.dynamicTest("Удаление существующего коментария", () -> {
            comment.setId(1);
            assertDoesNotThrow(() -> commentDao.delete(comment));
            assertTrue(commentDao.findByBookId(1).isEmpty());

        });

        DynamicTest delDoseNotExists = DynamicTest.dynamicTest("Удаление не существующего коментария", () -> {
            comment.setId(Integer.MAX_VALUE + 1);
            assertDoesNotThrow(() -> commentDao.delete(comment));
            assertTrue(commentDao.findById(Integer.MAX_VALUE + 1).isEmpty());
        });

        return Arrays.asList(delExists, delDoseNotExists);
    }
}