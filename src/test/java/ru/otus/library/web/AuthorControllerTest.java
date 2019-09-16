package ru.otus.library.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.acls.model.AclService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("WebTest")
public class AuthorControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private AuthorService authorService;

    @MockBean
    private AclService aclService;

    @Test
    @WithMockUser(username = "admin", password = "password")
    @DisplayName("Тест получение списка авторов")
    void getAuthorList() throws Exception {

        when(authorService.findAll()).thenReturn(getTestAuthors());
        assertTrue(this.mvc.perform(get("/authors/"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString()
                .contains("Auth1"));
    }

    @Test
    @WithMockUser(username = "admin", password = "password")
    @DisplayName("Тест получение автора по ID")
    void getAuthorById() throws Exception {
        when(authorService.findById(anyString())).thenReturn(Optional.of(new Author("Test1", "test")));

        assertTrue(this.mvc.perform(get("/authors/id"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString()
                .contains("Test1"));
    }

    @Test
    @WithMockUser(username = "admin", password = "password")
    @DisplayName("Добавление автора")
    void create() throws Exception {
        when(authorService.saveAuthor(any(Author.class))).thenReturn(new Author("TestId", "Test"));

        assertTrue(this.mvc.perform(post("/authors/")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":\"Id\",\"name\":\"xxx\"}"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString()
                .contains("Test"));
        verify(authorService, times(1)).saveAuthor(any(Author.class));
    }


    @Test
    @WithMockUser("admin")
    @DisplayName("Update автора")
    void update() throws Exception {
        when(authorService.findById(anyString())).thenReturn(Optional.of(new Author("Test1", "test")));

        when(authorService.saveAuthor(any(Author.class))).thenReturn(new Author("Test1", "test"));

        assertTrue(this.mvc.perform(put("/authors/TestID")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":\"Id\",\"name\":\"xxx\"}"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString()
                .contains("Test1"));

        verify(authorService, times(1)).saveAuthor(any(Author.class));
    }

    @Test
    @WithMockUser(username = "admin", password = "password")
    @DisplayName("Удаление автора")
    void deleteAuthorTest() throws Exception {
        when(authorService.findById(anyString())).thenReturn(Optional.of(new Author("Test1", "test")));

        this.mvc.perform(delete("/authors/TestId")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());

        verify(authorService, times(1)).findById("TestId");
        verify(authorService, times(1)).delete(any(Author.class));
    }


    private List<Author> getTestAuthors() {
        return List.of(new Author("Auth1"),
                new Author("Auth2"),
                new Author("Auth3"));
    }

}
