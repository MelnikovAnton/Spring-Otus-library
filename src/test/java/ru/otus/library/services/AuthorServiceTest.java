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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
        authorService.saveAuthor(new Author("test"));
        verify(authorDao, times(1)).insert(any(Author.class));
    }

    @Test
    void findAuthorsByName() {
        authorService.findAuthorsByName("test");
        verify(authorDao, times(1)).findByName(anyString());
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
        authorService.delete(new Author("test"));
        verify(authorDao, times(1)).delete(any(Author.class));
    }

    @Test
    void findAll() {
        authorService.findAll();
        verify(authorDao, times(1)).getAll();
    }
}