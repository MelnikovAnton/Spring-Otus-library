package ru.otus.library.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.otus.library.dao.impl.GenreDaoImpl;
import ru.otus.library.model.Genre;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Import({GenreDaoImpl.class})
class GenreDaoTest {

    @Autowired
    private GenreDao genreDao;

    @Test
    @DisplayName("Получениене количества жанров")
    void count() {
        long count = genreDao.count();
        assertEquals(count, 3);
    }


//    @Test
//    @DisplayName("Вставка с получением ID")
//    void insert() {
//        Genre genre = assertDoesNotThrow(() -> genreDao.insert(new Genre("ewq")));
//        assertTrue(genre.getId() > 0);
//    }

    @TestFactory
    @DisplayName("Получение жанра по ID")
    List<DynamicTest> getById() {
        DynamicTest genre1 = DynamicTest.dynamicTest("ID = 1", () -> {
            Genre genre = assertDoesNotThrow(() -> genreDao.getById(1).orElseThrow());
            assertEquals(genre.getId(), 1);
        });
        DynamicTest genre2 = DynamicTest.dynamicTest("ID = 10", () -> assertThrows(EmptyResultDataAccessException.class, () -> genreDao.getById(10)));
        return Arrays.asList(genre1, genre2);
    }

    @Test
    @DisplayName("Получение всех жанров")
    void getAll() {
        List<Genre> genres = assertDoesNotThrow(() -> genreDao.getAll());
        assertEquals(genres.size(), 3);
    }

//    @TestFactory
//    @DisplayName("Удаление жанров")
//    List<DynamicTest> delete() {
//        Genre genre = new Genre("Test");
//
//        DynamicTest delExists = DynamicTest.dynamicTest("Удаление существующего жанра", () -> {
//            genre.setId(1);
//            int c = assertDoesNotThrow(() -> genreDao.delete(genre));
//            assertEquals(c, 1);
//        });
//
//        DynamicTest delDoseNotExists = DynamicTest.dynamicTest("Удаление не существующего жанра", () -> {
//            genre.setId(10);
//            int c1 = assertDoesNotThrow(() -> genreDao.delete(genre));
//            assertEquals(c1, 0);
//        });
//
//        return Arrays.asList(delExists, delDoseNotExists);
//    }

    @TestFactory
    @DisplayName("Поиск по имени жанра")
    List<DynamicTest> findGenresByName() {
        DynamicTest empty = DynamicTest.dynamicTest("Пустое имя", () -> {
            List<Genre> genres = assertDoesNotThrow(() -> genreDao.findByName(""));
            assertEquals(genres.size(), 3);
        });
        DynamicTest part = DynamicTest.dynamicTest("Часть имени", () -> {
            List<Genre> genres = assertDoesNotThrow(() -> genreDao.findByName("enre"));
            assertEquals(genres.size(), 3);
        });
        DynamicTest full = DynamicTest.dynamicTest("Полное имя", () -> {
            List<Genre> genres = assertDoesNotThrow(() -> genreDao.findByName("Genre3"));
            assertEquals(genres.size(), 1);
            assertEquals(genres.get(0).getId(), 3);
        });
        DynamicTest noMatch = DynamicTest.dynamicTest("Не совпадающее имя", () -> {
            List<Genre> genres = assertDoesNotThrow(() -> genreDao.findByName("\"!№;%:?*:;%;№\""));
            assertEquals(genres.size(), 0);
        });
        return Arrays.asList(empty, part, full, noMatch);
    }
}