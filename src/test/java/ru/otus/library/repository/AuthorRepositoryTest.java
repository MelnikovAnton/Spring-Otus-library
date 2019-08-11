package ru.otus.library.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.otus.library.model.Author;
import ru.otus.library.model.Book;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AuthorRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;
    
    @Test
    @DisplayName("Вставка с получением ID")
    void insert() {
        Mono<Author> mono = assertDoesNotThrow(() -> authorRepository.save(new Author("ewq")));
        StepVerifier.create(mono)
                .assertNext(author -> assertNotNull(author.getId()))
                .expectComplete()
                .verify();

    }
//
//    @Test
//    @DisplayName("Поиск по имени автора")
//    void findAuthorsByName() {
//        List<Author> authors = assertDoesNotThrow(() -> authorRepository.findByNameContainingIgnoreCase(""));
//        assertEquals(authors.size(), 3);
//    }
//
//    @TestFactory
//    @DisplayName("Поиск по Id книги")
//    List<DynamicTest> findByBookId() {
//        DynamicTest auth1 = DynamicTest.dynamicTest("получение ID из БД и поиск по нему", () -> {
//            Book book = bookRepository.findAll().get(0);
//            List<Author> authors = assertDoesNotThrow(() -> authorRepository.findByBookId(book.getId()));
//            assertEquals(1, authors.size());
//            assertEquals(authors.get(0).getId(), book.getAuthors().iterator().next().getId());
//        });
//        DynamicTest auth2 = DynamicTest.dynamicTest("поиск по неправельному ID", () -> {
//            List<Author> authors = assertDoesNotThrow(() -> authorRepository.findByBookId("WrongId"));
//            assertTrue(authors.isEmpty());
//        });
//        return Arrays.asList(auth1, auth2);
//    }
//
//    @Test
//    @DisplayName("Удаление автора из книг")
//    void deleteAuthorFromBook() {
//        Author author = assertDoesNotThrow(() -> authorRepository.findAll().get(0));
//        assertDoesNotThrow(() -> authorRepository.delete(author));
//        List<Book> books = bookRepository.findByAuthorsContains(author);
//        assertTrue(books.isEmpty());
//    }
}