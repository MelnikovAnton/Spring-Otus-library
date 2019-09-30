package ru.otus.library.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.library.integration.AuthorIntegrationService;
import ru.otus.library.model.Author;
import ru.otus.library.repository.AuthorRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("ServiceTest")
class AuthorServiceTest {

    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private AuthorService authorService;
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private AuthorIntegrationService authorIntegrationService;

    @Test
    void saveAuthor() {
        Author author = new Author("test");

        doAnswer(inv -> {
            Author a = inv.getArgument(0);
            a.setId("1");
            return a;
        }).when(authorIntegrationService).createAuthor(any(Author.class));

        Author a = assertDoesNotThrow(() -> authorService.saveAuthor(author));
        assertEquals(a, author);
        assertEquals("1", a.getId());
    }

    @Test
    void findAuthorsByName() {
        when(authorRepository.findByNameContainingIgnoreCase(anyString())).thenReturn(getTestAuthors());
        List<Author> authors = authorService.findAuthorsByName("test");
        assertEquals(getTestAuthors(), authors);
    }


    @TestFactory
    @DisplayName("Поиск по ID")
    List<DynamicTest> findById() {
        DynamicTest isPresent = DynamicTest.dynamicTest("автор найден", () -> {
            when(authorRepository.findById(anyString())).thenReturn(Optional.of(new Author("test")));
            Optional<Author> author = authorService.findById("1");
            assertTrue(author.isPresent());
        });
        DynamicTest isNotPresent = DynamicTest.dynamicTest("автор не найден", () -> {
            doThrow(new EmptyResultDataAccessException(1)).when(authorRepository).findById(anyString());
            Optional<Author> author = assertDoesNotThrow(() -> authorService.findById("1"));
            assertTrue(author.isEmpty());
        });
        return Arrays.asList(isPresent, isNotPresent);
    }

    @Test
    void delete() {
        doAnswer(invocation -> {
            Author a = invocation.getArgument(0);
            assertEquals("1", a.getId());
            return null;
        }).when(authorIntegrationService).deleteAuthor(any(Author.class));

        Author author = new Author("Test");
        author.setId("1");

        assertDoesNotThrow(() -> authorService.delete(author));
    }

    @Test
    void findAll() {
        when(authorRepository.findAll()).thenReturn(getTestAuthors());

        List<Author> authors = assertDoesNotThrow(() -> authorService.findAll());
        assertEquals(getTestAuthors(), authors);
    }


    @Test
    void findByBookId() {
        when(authorRepository.findByBookId(anyString())).thenReturn(getTestAuthors());
        assertEquals(getTestAuthors(), authorService.findByBookId("1"));
    }

    private List<Author> getTestAuthors() {
        return List.of(new Author("Auth1"),
                new Author("Auth2"),
                new Author("Auth3"));
    }
}