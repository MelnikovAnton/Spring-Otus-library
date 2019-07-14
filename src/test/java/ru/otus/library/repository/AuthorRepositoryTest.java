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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AuthorRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;


    @Test
    @DisplayName("Вставка с получением ID")
    void insert() {
        Author author = new Author("ewq");
        assertDoesNotThrow(() -> authorRepository.save(author));
        Optional<Author> result = assertDoesNotThrow(() -> authorRepository.findById(author.getId()));
        assertTrue(result.isPresent());
        assertEquals(author, result.get());
    }

    @Test
    @DisplayName("Поиск по имени автора")
    void findAuthorsByName() {
        List<Author> authors = assertDoesNotThrow(() -> authorRepository.findByNameContainingIgnoreCase(""));
        assertEquals(authors.size(), 3);
    }

    @TestFactory
    @DisplayName("Поиск по Id книги")
    List<DynamicTest> findByBookId() {
        DynamicTest auth1 = DynamicTest.dynamicTest("поиск по ID", () -> {
            Book book = bookRepository.findAll().get(0);
            List<Author> authors = assertDoesNotThrow(() -> authorRepository.findByBookId(book.getId()));
            assertEquals(1, authors.size());
            assertEquals(authors.get(0).getId(), book.getAuthors().iterator().next().getId());
        });
        DynamicTest auth2 = DynamicTest.dynamicTest("ID = Integer.MAX_VALUE+1", () -> {
            List<Author> authors = assertDoesNotThrow(() -> authorRepository.findByBookId("123"));
            assertTrue(authors.isEmpty());
        });
        return Arrays.asList(auth1, auth2);
    }
}