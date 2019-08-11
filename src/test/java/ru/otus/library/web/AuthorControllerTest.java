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
import ru.otus.library.model.Author;
import ru.otus.library.services.AuthorService;

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
public class AuthorControllerTest {

//    @Autowired
//    private MockMvc mvc;
//    @Autowired
//    private AuthorService authorService;

//
//    @Test
//    @DisplayName("Тест получение списка авторов")
//    void getBookList() throws Exception {
//
//        when(authorService.findAll()).thenReturn(getTestAuthors());
//        assertTrue(this.mvc.perform(get("/authorApi/"))
//                .andExpect(status().isOk())
//                .andReturn()
//                .getResponse()
//                .getContentAsString()
//                .contains("Auth1"));
//    }
//
//    @Test
//    @DisplayName("Тест получение автора по ID")
//    void getBookById() throws Exception {
//        when(authorService.findById(anyString())).thenReturn(Optional.of(new Author("Test1", "test")));
//
//        assertTrue(this.mvc.perform(get("/authorApi/id"))
//                .andExpect(status().isOk())
//                .andReturn()
//                .getResponse()
//                .getContentAsString()
//                .contains("Test1"));
//    }
//
//    @Test
//    @DisplayName("Добавление автора")
//    void create() throws Exception {
//        when(authorService.saveAuthor(any(Author.class))).thenReturn(new Author("TestId", "Test"));
//
//        assertTrue(this.mvc.perform(post("/authorApi/")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content("{\"id\":\"Id\",\"name\":\"xxx\"}"))
//                .andExpect(status().isOk())
//                .andReturn()
//                .getResponse()
//                .getContentAsString()
//                .contains("Test"));
//        verify(authorService, times(1)).saveAuthor(any(Author.class));
//    }
//
//
//    @Test
//    @DisplayName("Update автора")
//    void update() throws Exception {
//        when(authorService.findById(anyString())).thenReturn(Optional.of(new Author("Test1", "test")));
//
//        when(authorService.saveAuthor(any(Author.class))).thenReturn(new Author("Test1", "test"));
//
//        assertTrue(this.mvc.perform(put("/authorApi/TestID")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content("{\"id\":\"Id\",\"name\":\"xxx\"}"))
//                .andExpect(status().isOk())
//                .andReturn()
//                .getResponse()
//                .getContentAsString()
//                .contains("Test1"));
//
//        verify(authorService, times(1)).saveAuthor(any(Author.class));
//    }
//
//    @Test
//    @DisplayName("Удаление автора")
//    void deleteBookTest() throws Exception {
//        when(authorService.findById(anyString())).thenReturn(Optional.of(new Author("Test1", "test")));
//
//        this.mvc.perform(delete("/authorApi/TestId"))
//                .andExpect(status().isOk());
//
//        verify(authorService, times(1)).findById("TestId");
//        verify(authorService, times(1)).delete(any(Author.class));
//    }
//
//
//
//    private List<Author> getTestAuthors() {
//        return List.of(new Author("Auth1"),
//                new Author("Auth2"),
//                new Author("Auth3"));
//    }
}
