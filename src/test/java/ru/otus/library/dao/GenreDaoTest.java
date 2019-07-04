package ru.otus.library.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.otus.library.dao.impl.GenreDaoImpl;
import ru.otus.library.model.Genre;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({GenreDaoImpl.class})
class GenreDaoTest {

    @Autowired
    private GenreDao genreDao;

    @PersistenceContext
    private EntityManager em;

    @Test
    @DisplayName("Получениене количества жанров")
    void count() {
        long count = genreDao.count();
        assertEquals(count, 4);
    }


    @Test
    @DisplayName("Вставка с получением ID")
    void insert() {
        Genre genre = new Genre("ewq");
        assertDoesNotThrow(() -> genreDao.insert(genre));

        em.refresh(genre);
        em.detach(genre);
        Optional<Genre> result = assertDoesNotThrow(() -> genreDao.getById(genre.getId()));
        assertTrue(result.isPresent());
        assertEquals(genre,result.get());
    }

    @TestFactory
    @DisplayName("Получение жанра по ID")
    List<DynamicTest> getById() {
        DynamicTest genre1 = DynamicTest.dynamicTest("ID = 1", () -> {
            Genre genre = assertDoesNotThrow(() -> genreDao.getById(1).orElseThrow());
            assertEquals(genre.getId(), 1);
        });
        DynamicTest genre2 = DynamicTest.dynamicTest("ID = 10", () -> {
            Optional<Genre> genre = assertDoesNotThrow(() -> genreDao.getById(Integer.MAX_VALUE + 1));
            assertTrue(genre.isEmpty());
        });
        return Arrays.asList(genre1, genre2);
    }

    @Test
    @DisplayName("Получение всех жанров")
    void getAll() {
        List<Genre> genres = assertDoesNotThrow(() -> genreDao.getAll());
        assertEquals(genres.size(), 4);
    }

    @TestFactory
    @DisplayName("Удаление жанров")
    List<DynamicTest> delete() {
        Genre genre = new Genre("Test");

        DynamicTest delExists = DynamicTest.dynamicTest("Удаление существующего жанра", () -> {
            genre.setId(1);
            assertDoesNotThrow(() -> genreDao.delete(genre));
            Optional<Genre> result = assertDoesNotThrow(() -> genreDao.getById(1));
            assertTrue(result.isEmpty());
        });

        DynamicTest delDoseNotExists = DynamicTest.dynamicTest("Удаление не существующего жанра", () -> {
            genre.setId(Integer.MAX_VALUE + 1);
            assertDoesNotThrow(() -> genreDao.delete(genre));
        });

        return Arrays.asList(delExists, delDoseNotExists);
    }

    @TestFactory
    @DisplayName("Поиск по имени жанра")
    List<DynamicTest> findGenresByName() {
        DynamicTest empty = DynamicTest.dynamicTest("Пустое имя", () -> {
            List<Genre> genres = assertDoesNotThrow(() -> genreDao.findByName(""));
            assertEquals(genres.size(), 4);
        });
        DynamicTest part = DynamicTest.dynamicTest("Часть имени", () -> {
            List<Genre> genres = assertDoesNotThrow(() -> genreDao.findByName("enre"));
            assertEquals(genres.size(), 4);
        });
        DynamicTest full = DynamicTest.dynamicTest("Полное имя", () -> {
            List<Genre> genres = assertDoesNotThrow(() -> genreDao.findByName("Genre 3"));
            assertEquals(genres.size(), 1);
            assertEquals(genres.get(0).getId(), 3);
        });
        DynamicTest noMatch = DynamicTest.dynamicTest("Не совпадающее имя", () -> {
            List<Genre> genres = assertDoesNotThrow(() -> genreDao.findByName("\"!№;%:?*:;%;№\""));
            assertEquals(genres.size(), 0);
        });
        return Arrays.asList(empty, part, full, noMatch);
    }

    @TestFactory
    @DisplayName("Поиск по Id книги")
    List<DynamicTest> findByBookId() {
        DynamicTest auth1 = DynamicTest.dynamicTest("ID = 1", () -> {
            List<Genre> authors = assertDoesNotThrow(() -> genreDao.findByBookId(1));
            assertEquals(1, authors.size());
            assertEquals(authors.get(0).getId(), 1);
        });
        DynamicTest auth2 = DynamicTest.dynamicTest("ID = Integer.MAX_VALUE+1", () -> {
            List<Genre> authors = assertDoesNotThrow(() -> genreDao.findByBookId(Integer.MAX_VALUE + 1));
            assertTrue(authors.isEmpty());
        });
        return Arrays.asList(auth1, auth2);
    }
}