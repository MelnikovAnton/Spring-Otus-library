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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private TestEntityManager em;

    @Test
    @DisplayName("Получениене количества авторов")
    void count() {
        long count = authorRepository.count();
        assertEquals(count, 3);
    }


    @Test
    @DisplayName("Вставка с получением ID")
    void insert() {
        Author author = new Author("ewq");
        assertDoesNotThrow(() -> authorRepository.save(author));

        em.refresh(author);
        em.detach(author);
        Optional<Author> result = assertDoesNotThrow(() -> authorRepository.findById(author.getId()));
        assertTrue(result.isPresent());
        assertEquals(author, result.get());

    }

    @TestFactory
    @DisplayName("Получение автора по ID")
    List<DynamicTest> getById() {
        DynamicTest author1 = DynamicTest.dynamicTest("ID = 1", () -> {
            Author author = assertDoesNotThrow(() -> authorRepository.findById(1L).orElseThrow());
            assertEquals(author.getId(), 1);
        });
        DynamicTest author2 = DynamicTest.dynamicTest("ID = Integer.MAX_VALUE+1", () -> {
            assertTrue(authorRepository.findById(Integer.MAX_VALUE + 1L).isEmpty());

        });
        return Arrays.asList(author1, author2);
    }

    @Test
    @DisplayName("Получение всех авторов")
    void getAll() {
        List<Author> authors = assertDoesNotThrow(() -> authorRepository.findAll());
        assertEquals(authors.size(), 3);
    }

    @TestFactory
    @DisplayName("Удаление авторов")
    List<DynamicTest> delete() {
        Author author = new Author("Test");

        DynamicTest delExists = DynamicTest.dynamicTest("Удаление существующего автора", () -> {
            author.setId(1);
            assertDoesNotThrow(() -> authorRepository.delete(author));
            Optional<Author> result = assertDoesNotThrow(() -> authorRepository.findById(1L));
            assertTrue(result.isEmpty());
        });

        DynamicTest delDoseNotExists = DynamicTest.dynamicTest("Удаление не существующего автора", () -> {
            author.setId(Integer.MAX_VALUE + 1);
            assertDoesNotThrow(() -> authorRepository.delete(author));
        });

        return Arrays.asList(delExists, delDoseNotExists);
    }

    @TestFactory
    @DisplayName("Поиск по имени автора")
    List<DynamicTest> findAuthorsByName() {
        DynamicTest empty = DynamicTest.dynamicTest("Пустое имя", () -> {
            List<Author> authors = assertDoesNotThrow(() -> authorRepository.findByNameContaining(""));
            assertEquals(authors.size(), 3);
        });
        DynamicTest part = DynamicTest.dynamicTest("Часть имени", () -> {
            List<Author> authors = assertDoesNotThrow(() -> authorRepository.findByNameContaining("uth"));
            assertEquals(authors.size(), 3);
        });
        DynamicTest full = DynamicTest.dynamicTest("Полное имя", () -> {
            List<Author> authors = assertDoesNotThrow(() -> authorRepository.findByNameContaining("Author 3"));
            assertEquals(authors.size(), 1);
            assertEquals(authors.get(0).getId(), 3);
        });
        DynamicTest noMatch = DynamicTest.dynamicTest("Не совпадающее имя", () -> {
            List<Author> authors = assertDoesNotThrow(() -> authorRepository.findByNameContaining("\"!№;%:?*:;%;№\""));
            assertEquals(authors.size(), 0);
        });
        return Arrays.asList(empty, part, full, noMatch);
    }

    @TestFactory
    @DisplayName("Поиск по Id книги")
    List<DynamicTest> findByBookId() {
        DynamicTest auth1 = DynamicTest.dynamicTest("ID = 1", () -> {
            List<Author> authors = assertDoesNotThrow(() -> authorRepository.findByBookId(1L));
            assertEquals(1, authors.size());
            assertEquals(authors.get(0).getId(), 1);
        });
        DynamicTest auth2 = DynamicTest.dynamicTest("ID = Integer.MAX_VALUE+1", () -> {
            List<Author> authors = assertDoesNotThrow(() -> authorRepository.findByBookId(Integer.MAX_VALUE + 1L));
            assertTrue(authors.isEmpty());
        });
        return Arrays.asList(auth1, auth2);
    }
}