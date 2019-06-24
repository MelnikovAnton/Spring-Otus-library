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
import ru.otus.library.dao.GenreDao;
import ru.otus.library.model.Author;
import ru.otus.library.model.Genre;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doThrow;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false"})
class GenreServiceTest {

    @MockBean
    private GenreDao genreDao;
    @Autowired
    private GenreService genreService;

    @Test
    void saveGenre() {
        genreService.saveGenre(new Genre("test"));
        verify(genreDao, times(1)).insert(any(Genre.class));
    }

    @Test
    void findGenresByName() {
        genreService.findGenresByName("test");
        verify(genreDao, times(1)).findByName(anyString());
    }

    @Test
    void findAll() {
        genreService.findAll();
        verify(genreDao, times(1)).getAll();
    }

    @TestFactory
    @DisplayName("Поиск по ID")
    List<DynamicTest> findById() {
        DynamicTest isPresent = DynamicTest.dynamicTest("Жанр найден", () -> {
            when(genreDao.getById(anyInt())).thenReturn(Optional.of(new Genre("test")));
            Optional<Genre> genre = genreService.findById(1);
            assertTrue(genre.isPresent());
        });
        DynamicTest isNotPresent = DynamicTest.dynamicTest("Жанр не найден", () -> {
            doThrow(new EmptyResultDataAccessException(1)).when(genreDao).getById(anyInt());
            Optional<Genre> genre = assertDoesNotThrow(() -> genreService.findById(1));
            assertTrue(genre.isEmpty());
        });
        return Arrays.asList(isPresent, isNotPresent);
    }

    @Test
    void delete() {
        genreService.delete(new Genre("test"));
        verify(genreDao, times(1)).delete(any(Genre.class));
    }
}