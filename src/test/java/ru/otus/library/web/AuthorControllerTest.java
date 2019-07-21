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
import ru.otus.library.model.Author;
import ru.otus.library.services.AuthorService;

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
public class AuthorControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private AuthorService authorService;


    @Test
    @DisplayName("View authorList есть и содержит список авторов")
    void authorList() throws Exception {
        when(authorService.findAll()).thenReturn(getTestAuthors());

        assertTrue(this.mvc.perform(get("/authorList"))
                .andExpect(status().isOk())
                .andExpect(view().name("authorList"))
                .andExpect(model().attributeExists("authors"))
                .andReturn()
                .getResponse()
                .getContentAsString()
                .contains("Auth1"));
    }


    @Test
    @DisplayName("Тест editAuthor view")
    void editAuthorTest() throws Exception {
        when(authorService.findById(anyString()))
                .thenReturn(Optional.of(new Author("AuthorId", "Auth1")));

        assertTrue(this.mvc.perform(get("/editAuthor/AuthorId"))
                .andExpect(status().isOk())
                .andExpect(view().name("editAuthor"))
                .andExpect(model().attributeExists("author"))
                .andReturn()
                .getResponse()
                .getContentAsString()
                .contains("Auth1"));
    }

    @Test
    @DisplayName("Redirect после POST")
    void postEditAuthor() throws Exception {
        this.mvc.perform(post("/editAuthor/TestID"))
                .andExpect(redirectedUrl("/editAuthor/TestID"));

        verify(authorService, times(1)).saveAuthor(any(Author.class));
    }


    @Test
    @DisplayName("Удаление автора")
    void deleteAuthorTest() throws Exception {
        when(authorService.findById(anyString()))
                .thenReturn(Optional.of(new Author("TestId", "test")));

        this.mvc.perform(get("/deleteAuthor?id=TestId"))
                .andExpect(redirectedUrl("/authorList"));

        verify(authorService, times(1)).findById("TestId");
        verify(authorService, times(1)).delete(any(Author.class));
    }

    @Test
    @DisplayName("Добавление автора")
    void addAuthorTest() throws Exception {
        when(authorService.saveAuthor(any(Author.class)))
                .thenReturn(new Author("TestId", "test"));

        this.mvc.perform(get("/addAuthor"))
                .andExpect(status().isOk())
                .andExpect(view().name("editAuthor"))
                .andExpect(model().attributeExists("author"));

        verify(authorService, times(1)).saveAuthor(any(Author.class));
    }


    private List<Author> getTestAuthors() {
        return List.of(new Author("Auth1"),
                new Author("Auth2"),
                new Author("Auth3"));
    }
}
