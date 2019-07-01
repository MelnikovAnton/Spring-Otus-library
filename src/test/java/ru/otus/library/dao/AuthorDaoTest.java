package ru.otus.library.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.otus.library.dao.impl.AuthorDaoImpl;
import ru.otus.library.model.Author;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Import({AuthorDaoImpl.class})
class AuthorDaoTest {

    @Autowired
    private AuthorDao authorDao;

    @Test
    @DisplayName("Получениене количества авторов")
    void count() {
        long count = authorDao.count();
        assertEquals(count, 3);
    }


//    @Test
//    @DisplayName("Вставка с получением ID")
//    void insert() {
//        Author author = assertDoesNotThrow(() -> authorDao.insert(new Author("ewq")));
//        assertTrue(author.getId() > 0);
//    }

    @TestFactory
    @DisplayName("Получение автора по ID")
    List<DynamicTest> getById() {
        DynamicTest author1 = DynamicTest.dynamicTest("ID = 1", () -> {
            Author author = assertDoesNotThrow(() -> authorDao.getById(1).orElseThrow());
            assertEquals(author.getId(), 1);
        });
        DynamicTest author2 = DynamicTest.dynamicTest("ID = 10", () -> assertThrows(EmptyResultDataAccessException.class, () -> authorDao.getById(10)));
        return Arrays.asList(author1, author2);
    }

    @Test
    @DisplayName("Получение всех авторов")
    void getAll() {
        List<Author> authors = assertDoesNotThrow(() -> authorDao.getAll());
        assertEquals(authors.size(), 3);
    }

//    @TestFactory
//    @DisplayName("Удаление авторов")
//    List<DynamicTest> delete() {
//        Author author = new Author("Test");
//
//        DynamicTest delExists = DynamicTest.dynamicTest("Удаление существующего автора", () -> {
//            author.setId(1);
//            int c = assertDoesNotThrow(() -> authorDao.delete(author));
//            assertEquals(c, 1);
//        });
//
//        DynamicTest delDoseNotExists = DynamicTest.dynamicTest("Удаление не существующего автора", () -> {
//            author.setId(10);
//            int c1 = assertDoesNotThrow(() -> authorDao.delete(author));
//            assertEquals(c1, 0);
//        });
//
//        return Arrays.asList(delExists, delDoseNotExists);
//    }

    @TestFactory
    @DisplayName("Поиск по имени автора")
    List<DynamicTest> findAuthorsByName() {
        DynamicTest empty = DynamicTest.dynamicTest("Пустое имя", () -> {
            List<Author> authors = assertDoesNotThrow(() -> authorDao.findByName(""));
            assertEquals(authors.size(), 3);
        });
        DynamicTest part = DynamicTest.dynamicTest("Часть имени", () -> {
            List<Author> authors = assertDoesNotThrow(() -> authorDao.findByName("uth"));
            assertEquals(authors.size(), 3);
        });
        DynamicTest full = DynamicTest.dynamicTest("Полное имя", () -> {
            List<Author> authors = assertDoesNotThrow(() -> authorDao.findByName("Author3"));
            assertEquals(authors.size(), 1);
            assertEquals(authors.get(0).getId(), 3);
        });
        DynamicTest noMatch = DynamicTest.dynamicTest("Не совпадающее имя", () -> {
            List<Author> authors = assertDoesNotThrow(() -> authorDao.findByName("\"!№;%:?*:;%;№\""));
            assertEquals(authors.size(), 0);
        });
        return Arrays.asList(empty, part, full, noMatch);
    }
}