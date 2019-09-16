package ru.otus.library.web;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.library.model.Book;
import ru.otus.library.services.BookService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("WebTest")
public class BookControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private BookService bookService;

    @Test
    @WithMockUser(username = "admin", password = "password")
    @DisplayName("View bookList есть и содержит список книг")
    void bookList() throws Exception {
        when(bookService.findAll()).thenReturn(getTestBooks());

        assertTrue(this.mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("bookList"))
                .andReturn()
                .getResponse()
                .getContentAsString()
                .contains("<div id=\"app\">"));
    }

    @Test
    @WithMockUser(username = "admin", password = "password")
    @DisplayName("Тест получение списка книг")
    void getBookList() throws Exception {

        when(bookService.findAll()).thenReturn(getTestBooks());
        assertTrue(this.mvc.perform(get("/books/")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString()
                .contains("Test1"));
    }

    @Test
    @WithMockUser(username = "admin", password = "password")
    @DisplayName("Тест получение книги по ID")
    void getBookById() throws Exception {
        when(bookService.findById(anyString())).thenReturn(Optional.of(new Book("Test1", "test")));

        assertTrue(this.mvc.perform(get("/books/id")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString()
                .contains("Test1"));
    }

    @Test
    @WithMockUser(username = "admin", password = "password")
    @DisplayName("Добавление книги")
    void create() throws Exception {
        when(bookService.saveBook(any(Book.class))).thenReturn(new Book("TestId", "Test", "Test"));

        assertTrue(this.mvc.perform(post("/books/")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":\"Id\",\"title\":\"xxx\",\"contentPath\":\"xxx\",\"author\":[],\"genre\":[]}"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString()
                .contains("Test"));
        verify(bookService, times(1)).saveBook(any(Book.class));
    }


    @Test
    @WithMockUser(username = "admin", password = "password")
    @DisplayName("Update книги")
    void update() throws Exception {
        when(bookService.findById(anyString())).thenReturn(Optional.of(new Book("Test1", "test")));

        when(bookService.saveBook(any(Book.class))).thenReturn(new Book("Test1", "test"));

        assertTrue(this.mvc.perform(put("/books/TestID")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":\"Id\",\"title\":\"xxx\",\"contentPath\":\"xxx\",\"author\":[],\"genre\":[]}"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString()
                .contains("Test1"));

        verify(bookService, times(1)).saveBook(any(Book.class));
    }

    @Test
    @WithMockUser(username = "admin", password = "password")
    @DisplayName("Удаление книги")
    void deleteBookTest() throws Exception {
        when(bookService.findById(anyString())).thenReturn(Optional.of(new Book("Test1", "test")));

        this.mvc.perform(delete("/books/TestId")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());

        verify(bookService, times(1)).findById("TestId");
        verify(bookService, times(1)).delete(any(Book.class));
    }


    private List<Book> getTestBooks() {
        return List.of(new Book("id1", "Test1", "Test1"),
                new Book("id1", "Test2", "Test2"),
                new Book("id1", "Test3", "Test3"));
    }
}
