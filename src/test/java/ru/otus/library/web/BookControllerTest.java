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
import ru.otus.library.model.Book;
import ru.otus.library.services.BookService;

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
public class BookControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private BookService bookService;

    @Test
    @DisplayName("View bookList есть и содержит список книг")
    void bookList() throws Exception {
        when(bookService.findAll()).thenReturn(getTestBooks());

        assertTrue(this.mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("bookList"))
                .andExpect(model().attributeExists("books"))
                .andReturn()
                .getResponse()
                .getContentAsString()
                .contains("Test1"));
    }

    @Test
    @DisplayName("Тест editBook view")
    void editBookTest() throws Exception {

        when(bookService.findById(anyString())).thenReturn(Optional.of(new Book("TestID", "Test1", "test")));
        assertTrue(this.mvc.perform(get("/edit/TestID"))
                .andExpect(status().isOk())
                .andExpect(view().name("editBook"))
                .andExpect(model().attributeExists("book"))
                .andReturn()
                .getResponse()
                .getContentAsString()
                .contains("Test1"));

    }

    @Test
    @DisplayName("Redirect после POST")
    void postEditBook() throws Exception {
        this.mvc.perform(post("/edit/TestID"))
                .andExpect(redirectedUrl("/edit/TestID"));

        verify(bookService, times(1)).saveBook(any(Book.class));
    }

    @Test
    @DisplayName("Удаление книги")
    void deleteBookTest() throws Exception {
        when(bookService.findById(anyString())).thenReturn(Optional.of(new Book("Test1", "test")));

        this.mvc.perform(get("/deleteBook?id=TestId"))
                .andExpect(redirectedUrl("/"));

        verify(bookService, times(1)).findById("TestId");
        verify(bookService, times(1)).delete(any(Book.class));
    }

    @Test
    @DisplayName("Добавление книги")
    void addBookTest() throws Exception {
        when(bookService.saveBook(any(Book.class))).thenReturn(new Book("TestId", "Test", "Test"));

        this.mvc.perform(get("/addBook"))
                .andExpect(status().isOk())
                .andExpect(view().name("editBook"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attributeExists("comments"));
        verify(bookService, times(1)).saveBook(any(Book.class));
    }

    private List<Book> getTestBooks() {
        return List.of(new Book("id1", "Test1", "Test1"),
                new Book("id1", "Test2", "Test2"),
                new Book("id1", "Test3", "Test3"));
    }
}
