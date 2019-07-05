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
import ru.otus.library.model.Genre;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class GenreRepositoryTest {

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private TestEntityManager em;


    @Test
    @DisplayName("Вставка с получением ID")
    void insert() {
        Genre genre = new Genre("ewq");
        assertDoesNotThrow(() -> genreRepository.save(genre));

        em.refresh(genre);
        em.detach(genre);
        Optional<Genre> result = assertDoesNotThrow(() -> genreRepository.findById(genre.getId()));
        assertTrue(result.isPresent());
        assertEquals(genre, result.get());
    }

    @Test
    @DisplayName("Поиск по имени жанра")
    void findGenresByName() {
        List<Genre> genres = assertDoesNotThrow(() -> genreRepository.findByNameContaining("enre"));
        assertEquals(genres.size(), 4);
    }

    @TestFactory
    @DisplayName("Поиск по Id книги")
    List<DynamicTest> findByBookId() {
        DynamicTest auth1 = DynamicTest.dynamicTest("ID = 1", () -> {
            List<Genre> authors = assertDoesNotThrow(() -> genreRepository.findByBookId(1));
            assertEquals(1, authors.size());
            assertEquals(authors.get(0).getId(), 1);
        });
        DynamicTest auth2 = DynamicTest.dynamicTest("ID = Integer.MAX_VALUE+1", () -> {
            List<Genre> authors = assertDoesNotThrow(() -> genreRepository.findByBookId(Integer.MAX_VALUE + 1));
            assertTrue(authors.isEmpty());
        });
        return Arrays.asList(auth1, auth2);
    }
}