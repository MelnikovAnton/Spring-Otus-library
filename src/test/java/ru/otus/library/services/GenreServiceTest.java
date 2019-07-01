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
import ru.otus.library.model.Genre;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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
        Genre genre = new Genre("test");

        doAnswer(inv -> {
            Genre g = inv.getArgument(0);
            g.setId(1);
            return null;
        }).when(genreDao).insert(any(Genre.class));

        Genre g  = assertDoesNotThrow(() -> genreService.saveGenre(genre));
        assertEquals(g, genre);
        assertEquals(1, g.getId());
    }

    @Test
    void findGenresByName() {
        when(genreDao.findByName(anyString())).thenReturn(getTestGenres());
        List<Genre> genres = genreService.findGenresByName("test");
        assertEquals(getTestGenres(), genres);
    }

    @Test
    void findAll() {
        when(genreDao.getAll()).thenReturn(getTestGenres());

        List<Genre> genres = assertDoesNotThrow(() -> genreService.findAll());
        assertEquals(getTestGenres(), genres);
    }

    @TestFactory
    @DisplayName("Поиск по ID")
    List<DynamicTest> findById() {
        DynamicTest isPresent = DynamicTest.dynamicTest("Жанр найден", () -> {
            when(genreDao.getById(anyLong())).thenReturn(Optional.of(new Genre("test")));
            Optional<Genre> genre = genreService.findById(1);
            assertTrue(genre.isPresent());
        });
        DynamicTest isNotPresent = DynamicTest.dynamicTest("Жанр не найден", () -> {
            doThrow(new EmptyResultDataAccessException(1)).when(genreDao).getById(anyLong());
            Optional<Genre> genre = assertDoesNotThrow(() -> genreService.findById(1));
            assertTrue(genre.isEmpty());
        });
        return Arrays.asList(isPresent, isNotPresent);
    }

    @Test
    void delete() {
        doAnswer(invocation -> {
            Genre g = invocation.getArgument(0);
            assertEquals(1, g.getId());
            return null;
        }).when(genreDao).delete(any(Genre.class));

        Genre genre = new Genre("Test");
        genre.setId(1);

        assertDoesNotThrow(() -> genreService.delete(genre));
    }

    @Test
    void findByBookId() {
        when(genreDao.findByBookId(anyLong())).thenReturn(getTestGenres());
        assertEquals(getTestGenres(),genreService.findByBookId(1));
    }

    private List<Genre> getTestGenres() {
        return List.of(new Genre("Genre1"),
                new Genre("Genre3"),
                new Genre("Genre3"));
    }
}