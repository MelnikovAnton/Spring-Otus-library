package ru.otus.library.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.library.integration.AuthorIntegrationService;
import ru.otus.library.integration.BookIntegrationService;
import ru.otus.library.model.Author;
import ru.otus.library.model.Book;
import ru.otus.library.model.Genre;
import ru.otus.library.repository.BookRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("ServiceTest")
class BookServiceTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;
    @MockBean
    private AuthorService authorService;
    @MockBean
    private GenreService genreService;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private BookIntegrationService bookIntegrationService;

    @Test
    @DisplayName("Сохранение книги без автора и жанра")
    void saveBook() {
        Book book = new Book("test", "test");

        doAnswer(invocation -> {
            Book b = (Book) invocation.getArgument(0);
            b.setId("1");
            return b;
        }).when(bookIntegrationService).createBook(any(Book.class));

        Book b = assertDoesNotThrow(() -> bookService.saveBook(book));
        assertEquals(book, b);

        verify(authorService, never()).saveAuthor(any(Author.class));
        verify(genreService, never()).saveGenre(any(Genre.class));
    }

    @Test
    @DisplayName("Сохранение книги с автором и жанром")
    void saveBookWithAuthAndGenre() {

        doAnswer(invocation -> {
            Book b = (Book) invocation.getArgument(0);
            b.setId("1");
            return b;
        }).when(bookIntegrationService).createBook(any(Book.class));

        Book book = new Book("test", "test");
        book.addGenre(new Genre("Test"));
        book.addAuthor(new Author("Test"));

        Book b = assertDoesNotThrow(() -> bookService.saveBook(book));
        assertEquals(book, b);
    }

    @Test
    void findBooksByTitle() {
        when(bookRepository.findByTitleContaining(anyString())).thenReturn(getTestBooks());
        List<Book> books = bookService.findBooksByTitle("test");
        assertEquals(getTestBooks(), books);
    }

    @Test
    void findBooksByAuthor() {
        when(authorService.findAuthorsByName(anyString()))
                .thenReturn(List.of(new Author("1", "Auth1"),
                        new Author("2", "Auth2"),
                        new Author("3", "Auth3")));

        when(bookRepository.findByAuthorsNameContains(anyString())).thenReturn(getTestBooks());

        List<Book> books = assertDoesNotThrow(() -> bookService.findBooksByAuthor("test"));

        assertEquals(getTestBooks(), books);

        verify(bookRepository, times(1)).findByAuthorsNameContains(anyString());
    }

    @Test
    void findBooksByGenre() {
        when(genreService.findGenresByName(anyString()))
                .thenReturn(List.of(new Genre("1", "Genre1"),
                        new Genre("2", "Genre2"),
                        new Genre("3", "Genre3")));

        when(bookRepository.findByGenresNameContains(anyString())).thenReturn(getTestBooks());

        List<Book> books = assertDoesNotThrow(() -> bookService.findBooksByGenre("test"));

        assertEquals(getTestBooks(), books);

        verify(bookRepository, times(1)).findByGenresNameContains(anyString());
    }

    @TestFactory
    @DisplayName("Поиск по ID")
    List<DynamicTest> findById() {
        DynamicTest isPresent = DynamicTest.dynamicTest("Книга найдена", () -> {
            when(bookRepository.findById(anyString())).thenReturn(Optional.of(new Book("test", "test")));
            Optional<Book> oBook = bookService.findById("1");
            assertTrue(oBook.isPresent());
        });
        DynamicTest isNotPresent = DynamicTest.dynamicTest("Книга не найдена", () -> {
            doThrow(new EmptyResultDataAccessException(1)).when(bookRepository).findById(anyString());
            Optional<Book> book = assertDoesNotThrow(() -> bookService.findById("1"));
            assertTrue(book.isEmpty());
        });
        return Arrays.asList(isPresent, isNotPresent);
    }

    @Test
    void delete() {
        doAnswer(invocation -> {
            Book a = invocation.getArgument(0);
            assertEquals("1", a.getId());
            return null;
        }).when(bookIntegrationService).deleteBook(any(Book.class));

        Book book = new Book("Test", "Test");
        book.setId("1");

        assertDoesNotThrow(() -> bookService.delete(book));
    }

    @Test
    void findAll() {
        when(bookRepository.findAll()).thenReturn(getTestBooks());

        List<Book> books = assertDoesNotThrow(() -> bookService.findAll());
        assertEquals(getTestBooks(), books);
    }

    @Test
    void addRelations() {
        assertDoesNotThrow(() -> bookService.addRelations(new Book("Test", "test")));
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    private List<Book> getTestBooks() {
        return List.of(new Book("Test1", "Test1"),
                new Book("Test2", "Test2"),
                new Book("Test3", "Test3"));
    }

}