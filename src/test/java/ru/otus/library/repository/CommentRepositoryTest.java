package ru.otus.library.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.library.model.Book;
import ru.otus.library.model.Comment;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CommentRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private BookRepository bookRepository;

    @TestFactory
    @DisplayName("Добавление коментария")
    List<DynamicTest> insert() {
        DynamicTest correct = DynamicTest.dynamicTest("Добавление коментария и чтение к существующей книге", () -> {
            Book book = bookRepository.findAll().get(0);
            Comment comment = new Comment();
            comment.setBook(book);
            assertDoesNotThrow(() -> commentRepository.save(comment));

            Optional<Comment> result = commentRepository.findById(comment.getId());
            assertTrue(result.isPresent());
            assertEquals(comment, result.get());
        });
        DynamicTest commentWithoutBook = DynamicTest.dynamicTest("Добавление коментария без книги", () -> {
            Comment comment = new Comment();
            assertThrows(RuntimeException.class, () -> commentRepository.save(comment));
            assertNull(comment.getId());
        });
        DynamicTest commentWithWrongBook = DynamicTest.dynamicTest("Добавление коментария к несуществующей книге", () -> {
            Book book = new Book();
            book.setId("WrongId");
            Comment comment = new Comment();
            comment.setBook(book);
            assertThrows(RuntimeException.class, () -> commentRepository.save(comment));
            assertNull(comment.getId());
        });

        return List.of(correct, commentWithoutBook, commentWithWrongBook);
    }

    @TestFactory
    @DisplayName("Получение коментария по книге")
    List<DynamicTest> findByBook() {
        DynamicTest comment1 = DynamicTest.dynamicTest("ID получен из БД", () -> {
            String bookId = bookRepository.findAll().get(0).getId();
            List<Comment> comments = assertDoesNotThrow(() -> commentRepository.findByBookId(bookId));
            assertTrue(comments.get(0).getId().matches("[a-f\\d]{24}"));
        });
        DynamicTest comment2 = DynamicTest.dynamicTest("ID нет в базе", () -> {
            List<Comment> comment = assertDoesNotThrow(() -> commentRepository.findByBookId("WrongId"));
            assertTrue(comment.isEmpty());
        });
        return Arrays.asList(comment1, comment2);
    }

    @Test
    @DisplayName("Комментарий удаляется при удалении книги")
    void deleteBookAndComment(){
        Book book = bookRepository.findAll().get(0);
        List<Comment> comments = commentRepository.findByBookId(book.getId());
        bookRepository.delete(book);
        System.out.println(comments);
        comments.forEach(c->
                assertTrue(commentRepository
                        .findById(c.getId()).isEmpty()));
    }

}