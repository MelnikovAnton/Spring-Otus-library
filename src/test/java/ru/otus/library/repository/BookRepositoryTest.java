package ru.otus.library.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.library.model.Author;
import ru.otus.library.model.Book;
import ru.otus.library.model.Genre;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class BookRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private BookRepository bookRepository;


    @Test
    @DisplayName("Вставка с получением ID")
    void insert() {
        Book book = new Book("qwe", "ewq");
        assertDoesNotThrow(() -> bookRepository.save(book));

        Optional<Book> result = assertDoesNotThrow(() -> bookRepository.findById(book.getId()));
        assertTrue(result.isPresent());
        assertEquals(book, result.get());
    }


    @TestFactory
    @DisplayName("Поиск по заголовку")
    List<DynamicTest> findByTitle() {
        DynamicTest empty = DynamicTest.dynamicTest("Пустой заголовок", () -> {
            List<Book> books = assertDoesNotThrow(() -> bookRepository.findByTitleContaining(""));
            assertEquals(books.size(), 3);
        });
        DynamicTest part = DynamicTest.dynamicTest("Часть заголовока", () -> {
            List<Book> books = assertDoesNotThrow(() -> bookRepository.findByTitleContaining("oo"));
            assertEquals(books.size(), 3);
        });
        DynamicTest full = DynamicTest.dynamicTest("Полный заголовок", () -> {
            List<Book> books = assertDoesNotThrow(() -> bookRepository.findByTitleContaining("Book2"));
            assertEquals(books.size(), 1);
            assertTrue(books.get(0).getId().matches("[a-f\\d]{24}"));
        });
        DynamicTest noMatch = DynamicTest.dynamicTest("Не совпадающий заголовок", () -> {
            List<Book> books = assertDoesNotThrow(() -> bookRepository.findByTitleContaining("!№;%:?*:;%;№"));
            assertEquals(books.size(), 0);
        });
        return Arrays.asList(empty, part, full, noMatch);
    }

    @TestFactory
    @DisplayName("Поиск по автору")
    List<DynamicTest> getByAuthor() {
        DynamicTest auth1 = DynamicTest
                .dynamicTest("Поиск всех книг получение автора и поиско по этому автору", () -> {
                    Author author = assertDoesNotThrow(() ->
                            bookRepository.findAll()
                                    .get(0)
                                    .getAuthors()
                                    .iterator().next());
                    List<Book> books = assertDoesNotThrow(() -> bookRepository.findByAuthorsContains(author));
                    assertEquals(3, books.size());
                });

        DynamicTest auth2 = DynamicTest.dynamicTest("Автор с неправельным ID", () -> {
            Author author = new Author("Test");
            author.setId("WrongId");
            List<Book> books = assertDoesNotThrow(() -> bookRepository.findByAuthorsContains(author));
            //    System.out.println(books);
            assertEquals(books.size(), 0);
        });
        return Arrays.asList(auth1, auth2);
    }

    @TestFactory
    @DisplayName("Поиск по жанру")
    List<DynamicTest> getByGenre() {
        DynamicTest genre1 = DynamicTest.dynamicTest("Поиск всех книг получение жанров и поиско по этому жанру", () -> {
            Genre genre = assertDoesNotThrow(() ->
                    bookRepository.findAll()
                            .get(0)
                            .getGenres()
                            .iterator().next());
            List<Book> books = assertDoesNotThrow(() -> bookRepository.findByGenresContains(genre));
            assertEquals(3, books.size());
        });

        DynamicTest genre2 = DynamicTest.dynamicTest("Жанр с несуществующим ID", () -> {
            Genre genre = new Genre("Test");
            genre.setId("WrongId");
            List<Book> books = assertDoesNotThrow(() -> bookRepository.findByGenresContains(genre));
            assertEquals(books.size(), 0);
        });
        return Arrays.asList(genre1, genre2);
    }
}