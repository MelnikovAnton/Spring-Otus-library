package ru.otus.library.web;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.library.model.Genre;
import ru.otus.library.services.GenreService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("WebTest")
public class GenreControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private GenreService genreService;


    @Test
    @DisplayName("Тест получение списка жанров")
    void getBookList() throws Exception {

        when(genreService.findAll()).thenReturn(getTestGenres());
        assertTrue(this.mvc.perform(get("/genreApi/"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString()
                .contains("Genre1"));
    }

    @Test
    @DisplayName("Тест получение жанра по ID")
    void getBookById() throws Exception {
        when(genreService.findById(anyString())).thenReturn(Optional.of(new Genre("Test1", "test")));

        assertTrue(this.mvc.perform(get("/genreApi/id"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString()
                .contains("Test1"));
    }

    @Test
    @DisplayName("Добавление жанра")
    void create() throws Exception {
        when(genreService.saveGenre(any(Genre.class))).thenReturn(new Genre("TestId", "Test"));

        assertTrue(this.mvc.perform(post("/genreApi/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":\"Id\",\"name\":\"xxx\"}"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString()
                .contains("Test"));
        verify(genreService, times(1)).saveGenre(any(Genre.class));
    }


    @Test
    @DisplayName("Update жанра")
    void update() throws Exception {
        when(genreService.findById(anyString())).thenReturn(Optional.of(new Genre("Test1", "test")));

        when(genreService.saveGenre(any(Genre.class))).thenReturn(new Genre("Test1", "test"));

        assertTrue(this.mvc.perform(put("/genreApi/TestID")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":\"Id\",\"name\":\"xxx\"}"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString()
                .contains("Test1"));

        verify(genreService, times(1)).saveGenre(any(Genre.class));
    }

    @Test
    @DisplayName("Удаление жанра")
    void deleteBookTest() throws Exception {
        when(genreService.findById(anyString())).thenReturn(Optional.of(new Genre("Test1", "test")));

        this.mvc.perform(delete("/genreApi/TestId"))
                .andExpect(status().isOk());

        verify(genreService, times(1)).findById("TestId");
        verify(genreService, times(1)).delete(any(Genre.class));
    }


    private List<Genre> getTestGenres() {
        return List.of(new Genre("Genre1"),
                new Genre("Genre2"),
                new Genre("Genre3"));
    }
}
