package ru.otus.library.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.library.dao.AuthorDao;
import ru.otus.library.model.Author;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false"})
class AuthorServiceTest {

    @MockBean
    private AuthorDao authorDao;
    @Autowired
    private AuthorService authorService;

    @Test
    void saveAuthor() {
        Author author = new Author("test");

        when(authorDao.insert(any(Author.class))).thenAnswer(invocation -> {
            Author a = invocation.getArgument(0);
            a.setId(1);
            return a;
        });

        Author a = assertDoesNotThrow(() -> authorService.saveAuthor(author));
        assertEquals(a, author);
        assertEquals(1, a.getId());
    }

    @Test
    void findAuthorsByName() {
        when(authorDao.findByName(anyString())).thenReturn(getTestAuthors());
        List<Author> authors = authorService.findAuthorsByName("test");
        assertEquals(getTestAuthors(), authors);
    }


    @TestFactory
    @DisplayName("Поиск по ID")
    List<DynamicTest> findById() {
        DynamicTest isPresent = DynamicTest.dynamicTest("автор найден", () -> {
            when(authorDao.getById(anyInt())).thenReturn(Optional.of(new Author("test")));
            Optional<Author> author = authorService.findById(1);
            assertTrue(author.isPresent());
        });
        DynamicTest isNotPresent = DynamicTest.dynamicTest("автор не найден", () -> {
            doThrow(new EmptyResultDataAccessException(1)).when(authorDao).getById(anyInt());
            Optional<Author> author = assertDoesNotThrow(() -> authorService.findById(1));
            assertTrue(author.isEmpty());
        });
        return Arrays.asList(isPresent, isNotPresent);
    }

    @Test
    void delete() {
        when(authorDao.delete(any(Author.class))).thenAnswer(invocation -> {
            Author a = invocation.getArgument(0);
            return a.getId();
        });

        Author author = new Author("Test");
        author.setId(1);

        int id = assertDoesNotThrow(() -> authorService.delete(author));
        assertEquals(1, id);
    }

    @Test
    void findAll() {
        when(authorDao.getAll()).thenReturn(getTestAuthors());

        List<Author> authors = assertDoesNotThrow(() -> authorService.findAll());
        assertEquals(getTestAuthors(), authors);
    }

    private List<Author> getTestAuthors() {
        return List.of(new Author("Auth1"),
                new Author("Auth2"),
                new Author("Auth3"));
    }
}