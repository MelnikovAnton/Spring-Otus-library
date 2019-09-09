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
import ru.otus.library.model.Comment;
import ru.otus.library.services.BookService;
import ru.otus.library.services.CommentService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("WebTest")
public class CommentControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private BookService bookService;
    @Autowired
    private CommentService commentService;
  
  
    @Test
    @WithMockUser(username = "admin", password = "password")
    @DisplayName("Тест получение списка комментариев")
    void getCommentsList() throws Exception {

        when(commentService.findAll()).thenReturn(List.of(new Comment(new Book(), "Comment1")));
        assertTrue(this.mvc.perform(get("/comments/")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString()
                .contains("Comment1"));
    }

    @Test
    @WithMockUser(username = "admin", password = "password")
    @DisplayName("Тест получение комментария по ID книги")
    void getCommentByBookId() throws Exception {
        when(bookService.findById(anyString())).thenReturn(Optional.of(new Book("title","Test")));
        when(commentService.findCommentsByBook(any(Book.class))).thenReturn(List.of(new Comment(new Book(), "Comment1")));
        assertTrue(this.mvc.perform(get("/comments/id")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString()
                .contains("Comment1"));
    }

    @Test
    @WithMockUser(username = "admin", password = "password")
    @DisplayName("Добавление комментария")
    void create() throws Exception {
        when(commentService.saveComment(any(Comment.class))).thenReturn(new Comment(new Book(), "Test"));

        assertTrue(this.mvc.perform(post("/comments/")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":\"Id\",\"comment\":\"xxx\"}"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString()
                .contains("Test"));
        verify(commentService, times(1)).saveComment(any(Comment.class));
    }


    @Test
    @WithMockUser(username = "admin", password = "password")
    @DisplayName("Update комментария")
    void update() throws Exception {
        when(commentService.findById(anyString())).thenReturn(Optional.of(new Comment(new Book(), "test")));

        when(commentService.saveComment(any(Comment.class))).thenReturn(new Comment(new Book(), "test"));

        assertTrue(this.mvc.perform(put("/comments/TestID")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":\"Id\",\"comment\":\"xxx\"}"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString()
                .contains("test"));

        verify(commentService, times(1)).saveComment(any(Comment.class));
    }

    @Test
    @WithMockUser(username = "admin", password = "password")
    @DisplayName("Удаление комментария")
    void deleteCommentTest() throws Exception {
        when(commentService.findById(anyString())).thenReturn(Optional.of(new Comment(new Book(), "test")));

        this.mvc.perform(delete("/comments/TestId").with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());

        verify(commentService, times(1)).findById("TestId");
        verify(commentService, times(1)).delete(any(Comment.class));
    }
}
