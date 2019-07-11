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
import ru.otus.library.model.Author;
import ru.otus.library.model.Book;
import ru.otus.library.model.Genre;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class BookRepositoryTest {

//    @Autowired
//    private BookRepository bookRepository;
//
//    @Autowired
//    private TestEntityManager em;
//
//    @Test
//    @DisplayName("Вставка с получением ID")
//    void insert() {
//        Book book = new Book("qwe", "ewq");
//        assertDoesNotThrow(() -> bookRepository.save(book));
//
//        em.refresh(book);
//        em.detach(book);
//
//        Optional<Book> result = assertDoesNotThrow(() -> bookRepository.findById(book.getId()));
//        assertTrue(result.isPresent());
//        assertEquals(book, result.get());
//    }
//
//
//    @TestFactory
//    @DisplayName("Поиск по заголовку")
//    List<DynamicTest> findByTitle() {
//        DynamicTest empty = DynamicTest.dynamicTest("Пустой заголовок", () -> {
//            List<Book> books = assertDoesNotThrow(() -> bookRepository.findByTitleContaining(""));
//            assertEquals(books.size(), 3);
//        });
//        DynamicTest part = DynamicTest.dynamicTest("Часть заголовока", () -> {
//            List<Book> books = assertDoesNotThrow(() -> bookRepository.findByTitleContaining("oo"));
//            assertEquals(books.size(), 3);
//        });
//        DynamicTest full = DynamicTest.dynamicTest("Полный заголовок", () -> {
//            List<Book> books = assertDoesNotThrow(() -> bookRepository.findByTitleContaining("Book 2"));
//            assertEquals(books.size(), 1);
//            assertEquals(books.get(0).getId(), 2);
//        });
//        DynamicTest noMatch = DynamicTest.dynamicTest("Не совпадающий заголовок", () -> {
//            List<Book> books = assertDoesNotThrow(() -> bookRepository.findByTitleContaining("!№;%:?*:;%;№"));
//            assertEquals(books.size(), 0);
//        });
//        return Arrays.asList(empty, part, full, noMatch);
//    }
//
//    @TestFactory
//    @DisplayName("Поиск по автору")
//    List<DynamicTest> getByAuthor() {
//        DynamicTest auth1 = DynamicTest.dynamicTest("Автор с ID 1(есть в базе)", () -> {
//            Author author = new Author("Test");
//            author.setId(1);
//            List<Book> books = assertDoesNotThrow(() -> bookRepository.findByAuthorsContains(author));
//            // System.out.println(books);
//            assertEquals(books.size(), 1);
//        });
//
//        DynamicTest auth2 = DynamicTest.dynamicTest("Автор с ID 10(нет есть в базе)", () -> {
//            Author author = new Author("Test");
//            author.setId(10);
//            List<Book> books = assertDoesNotThrow(() -> bookRepository.findByAuthorsContains(author));
//            //    System.out.println(books);
//            assertEquals(books.size(), 0);
//        });
//        return Arrays.asList(auth1, auth2);
//    }
//
//    @TestFactory
//    @DisplayName("Поиск по жанру")
//    List<DynamicTest> getByGenre() {
//        DynamicTest genre1 = DynamicTest.dynamicTest("Жанр с ID 1(есть в базе)", () -> {
//            Genre genre = new Genre("Test");
//            genre.setId(1);
//            List<Book> books = assertDoesNotThrow(() -> bookRepository.findByGenresContains(genre));
//            // System.out.println(books);
//            assertEquals(books.size(), 1);
//        });
//
//        DynamicTest genre2 = DynamicTest.dynamicTest("Жанр с ID 10(нет есть в базе)", () -> {
//            Genre genre = new Genre("Test");
//            genre.setId(10);
//            List<Book> books = assertDoesNotThrow(() -> bookRepository.findByGenresContains(genre));
//            //    System.out.println(books);
//            assertEquals(books.size(), 0);
//        });
//        return Arrays.asList(genre1, genre2);
//    }
}