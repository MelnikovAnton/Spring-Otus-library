package ru.otus.library.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.otus.library.model.Book;
import ru.otus.library.model.Genre;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class GenreRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Вставка с получением ID")
    void insert() {
        Genre genre = new Genre("ewq");
        assertDoesNotThrow(() -> genreRepository.save(genre));

        Optional<Genre> result = assertDoesNotThrow(() -> genreRepository.findById(genre.getId()));
        assertTrue(result.isPresent());
        assertEquals(genre, result.get());
    }

    @Test
    @DisplayName("Поиск по имени жанра")
    void findGenresByName() {
        List<Genre> genres = assertDoesNotThrow(() -> genreRepository.findByNameContainingIgnoreCase("enre"));
        assertEquals(genres.size(), 3);
    }

    @TestFactory
    @DisplayName("Поиск по Id книги")
    List<DynamicTest> findByBookId() {
        DynamicTest auth1 = DynamicTest.dynamicTest("ID получен из БД", () -> {
            String bookId = bookRepository.findAll().get(0).getId();
            List<Genre> authors = assertDoesNotThrow(() -> genreRepository.findByBookId(bookId));
            assertEquals(1, authors.size());
            assertTrue(authors.get(0).getId().matches("[a-f\\d]{24}"));
        });
        DynamicTest auth2 = DynamicTest.dynamicTest("ID нет в БД", () -> {
            List<Genre> authors = assertDoesNotThrow(() -> genreRepository.findByBookId("WrongId"));
            assertTrue(authors.isEmpty());
        });
        return Arrays.asList(auth1, auth2);
    }

    @Test
    @DisplayName("Удаление жанра из книг")
    void deleteAuthorFromBook() {
        Genre genre = assertDoesNotThrow(() -> genreRepository.findAll().get(0));
        assertDoesNotThrow(() -> genreRepository.delete(genre));
        List<Book> books = bookRepository.findByGenresContains(genre);
        assertTrue(books.isEmpty());
    }
}