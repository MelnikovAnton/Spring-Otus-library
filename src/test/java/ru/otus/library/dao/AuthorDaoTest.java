package ru.otus.library.dao;

import org.h2.tools.Console;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.otus.library.dao.impl.AuthorDaoImpl;
import ru.otus.library.model.Author;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
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


    @Test
    @DisplayName("Вставка с получением ID")
    void insert() {
        Author author = new Author("ewq");
        assertDoesNotThrow(() -> authorDao.insert(author));
        assertTrue(author.getId() > 0);
    }

    @TestFactory
    @DisplayName("Получение автора по ID")
    List<DynamicTest> getById() {
        DynamicTest author1 = DynamicTest.dynamicTest("ID = 1", () -> {
            Author author = assertDoesNotThrow(() -> authorDao.getById(1).orElseThrow());
            assertEquals(author.getId(), 1);
        });
        DynamicTest author2 = DynamicTest.dynamicTest("ID = Integer.MAX_VALUE+1", () -> {
           assertTrue(authorDao.getById(Integer.MAX_VALUE+1).isEmpty());

        });
        return Arrays.asList(author1, author2);
    }

    @Test
    @DisplayName("Получение всех авторов")
    void getAll() {
        List<Author> authors = assertDoesNotThrow(() -> authorDao.getAll());
        assertEquals(authors.size(), 3);
    }

    @TestFactory
    @DisplayName("Удаление авторов")
    List<DynamicTest> delete() {
        Author author = new Author("Test");

        DynamicTest delExists = DynamicTest.dynamicTest("Удаление существующего автора", () -> {
            author.setId(1);
            assertDoesNotThrow(() -> authorDao.delete(author));
        });

        DynamicTest delDoseNotExists = DynamicTest.dynamicTest("Удаление не существующего автора", () -> {
            author.setId(10);
            assertDoesNotThrow(() -> authorDao.delete(author));
        });

        return Arrays.asList(delExists, delDoseNotExists);
    }

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
            List<Author> authors = assertDoesNotThrow(() -> authorDao.findByName("Author 3"));
            assertEquals(authors.size(), 1);
            assertEquals(authors.get(0).getId(), 3);
        });
        DynamicTest noMatch = DynamicTest.dynamicTest("Не совпадающее имя", () -> {
            List<Author> authors = assertDoesNotThrow(() -> authorDao.findByName("\"!№;%:?*:;%;№\""));
            assertEquals(authors.size(), 0);
        });
        return Arrays.asList(empty, part, full, noMatch);
    }

    @TestFactory
    @DisplayName("Поиск по Id книги")
    List<DynamicTest> findByBookId() {
        DynamicTest auth1 = DynamicTest.dynamicTest("ID = 1", () -> {
            List<Author> authors = assertDoesNotThrow(() -> authorDao.findByBookId(1));
            assertEquals(1, authors.size());
            assertEquals(authors.get(0).getId(), 1);
        });
        DynamicTest auth2 = DynamicTest.dynamicTest("ID = Integer.MAX_VALUE+1", () -> {
            List<Author> authors = assertDoesNotThrow(() -> authorDao.findByBookId(Integer.MAX_VALUE + 1));
            assertTrue(authors.isEmpty());
        });
        return Arrays.asList(auth1, auth2);
    }
}