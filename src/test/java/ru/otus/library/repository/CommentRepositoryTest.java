package ru.otus.library.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.library.model.Book;
import ru.otus.library.model.Comment;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TestEntityManager em;

    @Test
    @DisplayName("Получениене количества комментариев")
    void count() {
        long count = commentRepository.count();
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
            assertDoesNotThrow(() -> commentRepository.save(comment));

            em.refresh(comment);
            em.detach(comment);

            Optional<Comment> result = commentRepository.findById(comment.getId());
            assertTrue(result.isPresent());
            assertEquals(comment, result.get());
        });
        DynamicTest commentWithoutBook = DynamicTest.dynamicTest("Добавление коментария без книги", () -> {
            Comment comment = new Comment();
            assertThrows(RuntimeException.class, () -> commentRepository.save(comment));
            assertFalse(comment.getId() > 0);
        });
        DynamicTest commentWithWrongBook = DynamicTest.dynamicTest("Добавление коментария к несуществующей книге", () -> {
            Book book = new Book();
            book.setId(Integer.MAX_VALUE + 1);
            Comment comment = new Comment();
            comment.setBook(book);
            assertThrows(RuntimeException.class, () -> commentRepository.save(comment));
            assertFalse(comment.getId() > 0);
        });

        return List.of(correct, commentWithoutBook, commentWithWrongBook);
    }

    @TestFactory
    @DisplayName("Получение коментария по книге")
    List<DynamicTest> findByBook() {
        DynamicTest comment1 = DynamicTest.dynamicTest("ID = 1", () -> {
            List<Comment> comments = assertDoesNotThrow(() -> commentRepository.findByBookId(1L));
            assertEquals(comments.get(0).getId(), 1);
        });
        DynamicTest comment2 = DynamicTest.dynamicTest("ID = Integer.MAX_VALUE+1", () -> {
            List<Comment> comment = assertDoesNotThrow(() -> commentRepository.findByBookId(Integer.MAX_VALUE + 1L));
            assertTrue(comment.isEmpty());
        });
        return Arrays.asList(comment1, comment2);
    }

    @Test
    void findAll() {
        List<Comment> comments = commentRepository.findAll();
        assertTrue(comments.size() == 4);
    }

    @TestFactory
    @DisplayName("Получение коментария по ID")
    List<DynamicTest> getById() {
        DynamicTest comment1 = DynamicTest.dynamicTest("ID = 1", () -> {
            Comment comment = assertDoesNotThrow(() -> commentRepository.findById(1L).orElseThrow());
            assertEquals(comment.getId(), 1);
        });
        DynamicTest comment2 = DynamicTest.dynamicTest("ID = Integer.MAX_VALUE+1", () -> {
            Optional<Comment> comment = assertDoesNotThrow(() -> commentRepository.findById(Integer.MAX_VALUE + 1L));
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
            assertDoesNotThrow(() -> commentRepository.delete(comment));
            assertTrue(commentRepository.findByBookId(1L).isEmpty());

        });

        DynamicTest delDoseNotExists = DynamicTest.dynamicTest("Удаление не существующего коментария", () -> {
            comment.setId(Integer.MAX_VALUE + 1L);
            assertDoesNotThrow(() -> commentRepository.delete(comment));
            assertTrue(commentRepository.findById(Integer.MAX_VALUE + 1L).isEmpty());
        });

        return Arrays.asList(delExists, delDoseNotExists);
    }
}