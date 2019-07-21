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
import ru.otus.library.model.Comment;
import ru.otus.library.services.BookService;
import ru.otus.library.services.CommentService;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

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
    @DisplayName("Удаление комментария")
    void deleteCommentTest() throws Exception {
        Comment comment = new Comment();
        comment.setBook(new Book("TestID", "test", "Test"));
        comment.setId("testCommentId");
        comment.setComment("comment");

        when(commentService.findById(anyString())).thenReturn(Optional.of(comment));

        this.mvc.perform(get("/deleteComment?id=commentId"))
                .andExpect(redirectedUrl("/edit/TestID"));

        verify(commentService, times(1)).delete(comment);
    }

    @Test
    @DisplayName("Добавление комментария")
    void addCommentTest() throws Exception {
        when(bookService.findById(anyString()))
                .thenReturn(Optional.of(new Book("TestBookId","test","test")));
        this.mvc.perform(post("/addComment/TestBookId")
                .param("comment", "comment"))
                .andExpect(redirectedUrl("/edit/TestBookId"));

        verify(commentService, times(1)).saveComment(any(Comment.class));
    }

}
