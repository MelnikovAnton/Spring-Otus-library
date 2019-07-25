package ru.otus.library.web;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    @DisplayName("View genreList есть и содержит список жанров")
    void genreList() throws Exception {
        when(genreService.findAll()).thenReturn(getTestGenres());

        assertTrue(this.mvc.perform(get("/genreList"))
                .andExpect(status().isOk())
                .andExpect(view().name("genreList"))
                .andExpect(model().attributeExists("genres"))
                .andReturn()
                .getResponse()
                .getContentAsString()
                .contains("Genre1"));
    }


    @Test
    @DisplayName("Тест editGenre view")
    void editGenreTest() throws Exception {
        when(genreService.findById(anyString()))
                .thenReturn(Optional.of(new Genre("GenreId", "Genre1")));

        assertTrue(this.mvc.perform(get("/editGenre/GenreId"))
                .andExpect(status().isOk())
                .andExpect(view().name("editGenre"))
                .andExpect(model().attributeExists("genre"))
                .andReturn()
                .getResponse()
                .getContentAsString()
                .contains("Genre1"));
    }

    @Test
    @DisplayName("Redirect после POST")
    void postEditGenre() throws Exception {
        this.mvc.perform(post("/editGenre/TestID"))
                .andExpect(redirectedUrl("/editGenre/TestID"));

        verify(genreService, times(1)).saveGenre(any(Genre.class));
    }


    @Test
    @DisplayName("Удаление жанра")
    void deleteGenreTest() throws Exception {
        when(genreService.findById(anyString()))
                .thenReturn(Optional.of(new Genre("TestId", "test")));

        this.mvc.perform(get("/deleteGenre?id=TestId"))
                .andExpect(redirectedUrl("/genreList"));

        verify(genreService, times(1)).findById("TestId");
        verify(genreService, times(1)).delete(any(Genre.class));
    }

    @Test
    @DisplayName("Добавление жанра")
    void addGenreTest() throws Exception {
        when(genreService.saveGenre(any(Genre.class)))
                .thenReturn(new Genre("TestId", "test"));

        this.mvc.perform(get("/addGenre"))
                .andExpect(status().isOk())
                .andExpect(view().name("editGenre"))
                .andExpect(model().attributeExists("genre"));

        verify(genreService, times(1)).saveGenre(any(Genre.class));
    }


    private List<Genre> getTestGenres() {
        return List.of(new Genre("Genre1"),
                new Genre("Genre2"),
                new Genre("Genre3"));
    }
}
