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
import ru.otus.library.model.Genre;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class GenreRepositoryTest {

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private TestEntityManager em;

    @Test
    @DisplayName("Получениене количества жанров")
    void count() {
        long count = genreRepository.count();
        assertEquals(count, 4);
    }


    @Test
    @DisplayName("Вставка с получением ID")
    void insert() {
        Genre genre = new Genre("ewq");
        assertDoesNotThrow(() -> genreRepository.save(genre));

        em.refresh(genre);
        em.detach(genre);
        Optional<Genre> result = assertDoesNotThrow(() -> genreRepository.findById(genre.getId()));
        assertTrue(result.isPresent());
        assertEquals(genre, result.get());
    }

    @TestFactory
    @DisplayName("Получение жанра по ID")
    List<DynamicTest> getById() {
        DynamicTest genre1 = DynamicTest.dynamicTest("ID = 1", () -> {
            Genre genre = assertDoesNotThrow(() -> genreRepository.findById(1L).orElseThrow());
            assertEquals(genre.getId(), 1);
        });
        DynamicTest genre2 = DynamicTest.dynamicTest("ID = 10", () -> {
            Optional<Genre> genre = assertDoesNotThrow(() -> genreRepository.findById(Integer.MAX_VALUE + 1L));
            assertTrue(genre.isEmpty());
        });
        return Arrays.asList(genre1, genre2);
    }

    @Test
    @DisplayName("Получение всех жанров")
    void getAll() {
        List<Genre> genres = assertDoesNotThrow(() -> genreRepository.findAll());
        assertEquals(genres.size(), 4);
    }

    @TestFactory
    @DisplayName("Удаление жанров")
    List<DynamicTest> delete() {
        Genre genre = new Genre("Test");

        DynamicTest delExists = DynamicTest.dynamicTest("Удаление существующего жанра", () -> {
            genre.setId(1);
            assertDoesNotThrow(() -> genreRepository.delete(genre));
            Optional<Genre> result = assertDoesNotThrow(() -> genreRepository.findById(1L));
            assertTrue(result.isEmpty());
        });

        DynamicTest delDoseNotExists = DynamicTest.dynamicTest("Удаление не существующего жанра", () -> {
            genre.setId(Integer.MAX_VALUE + 1L);
            assertDoesNotThrow(() -> genreRepository.delete(genre));
        });

        return Arrays.asList(delExists, delDoseNotExists);
    }

    @TestFactory
    @DisplayName("Поиск по имени жанра")
    List<DynamicTest> findGenresByName() {
        DynamicTest empty = DynamicTest.dynamicTest("Пустое имя", () -> {
            List<Genre> genres = assertDoesNotThrow(() -> genreRepository.findByNameContaining(""));
            assertEquals(genres.size(), 4);
        });
        DynamicTest part = DynamicTest.dynamicTest("Часть имени", () -> {
            List<Genre> genres = assertDoesNotThrow(() -> genreRepository.findByNameContaining("enre"));
            assertEquals(genres.size(), 4);
        });
        DynamicTest full = DynamicTest.dynamicTest("Полное имя", () -> {
            List<Genre> genres = assertDoesNotThrow(() -> genreRepository.findByNameContaining("Genre 3"));
            assertEquals(genres.size(), 1);
            assertEquals(genres.get(0).getId(), 3);
        });
        DynamicTest noMatch = DynamicTest.dynamicTest("Не совпадающее имя", () -> {
            List<Genre> genres = assertDoesNotThrow(() -> genreRepository.findByNameContaining("\"!№;%:?*:;%;№\""));
            assertEquals(genres.size(), 0);
        });
        return Arrays.asList(empty, part, full, noMatch);
    }

    @TestFactory
    @DisplayName("Поиск по Id книги")
    List<DynamicTest> findByBookId() {
        DynamicTest auth1 = DynamicTest.dynamicTest("ID = 1", () -> {
            List<Genre> authors = assertDoesNotThrow(() -> genreRepository.findByBookId(1));
            assertEquals(1, authors.size());
            assertEquals(authors.get(0).getId(), 1);
        });
        DynamicTest auth2 = DynamicTest.dynamicTest("ID = Integer.MAX_VALUE+1", () -> {
            List<Genre> authors = assertDoesNotThrow(() -> genreRepository.findByBookId(Integer.MAX_VALUE + 1));
            assertTrue(authors.isEmpty());
        });
        return Arrays.asList(auth1, auth2);
    }
}