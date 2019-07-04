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
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.library.dao.BookDao;
import ru.otus.library.model.Author;
import ru.otus.library.model.Book;
import ru.otus.library.model.Genre;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false"})
class BookServiceTest {

    @Autowired
    private BookService bookService;

    @MockBean
    private BookDao bookDao;
    @MockBean
    private AuthorService authorService;
    @MockBean
    private GenreService genreService;

    @Test
    @DisplayName("Сохранение книги без автора и жанра")
    void saveBook() {
        Book book = new Book("test", "test");

        doAnswer(invocation -> {
            Book b = (Book) invocation.getArgument(0);
            b.setId(1);
            return null;
        }).when(bookDao).save(any(Book.class));

        Book b = assertDoesNotThrow(() -> bookService.saveBook(book));
        assertEquals(book, b);

        verify(bookDao, times(1)).save(any(Book.class));
        verify(authorService, never()).saveAuthor(any(Author.class));
        verify(genreService, never()).saveGenre(any(Genre.class));
    }

    @Test
    @DisplayName("Сохранение книги с автором и жанром")
    void saveBookWithAuthAndGenre() {

        doAnswer(invocation -> {
            Book b = (Book) invocation.getArgument(0);
            b.setId(1);
            return b;
        }).when(bookDao).save(any(Book.class));

        Book book = new Book("test", "test");
        book.addGenre(new Genre("Test"));
        book.addAuthor(new Author("Test"));

        Book b = assertDoesNotThrow(() -> bookService.saveBook(book));
        assertEquals(book, b);

        verify(bookDao, times(1)).save(any(Book.class));
        verify(authorService, times(1)).saveAuthor(any(Author.class));
        verify(genreService, times(1)).saveGenre(any(Genre.class));
    }

    @Test
    void findBooksByTitle() {
        when(bookDao.findByTitle(anyString())).thenReturn(getTestBooks());
        List<Book> books = bookService.findBooksByTitle("test");
        assertEquals(getTestBooks(), books);
    }

    @Test
    void findBooksByAuthor() {
        when(authorService.findAuthorsByName(anyString()))
                .thenReturn(List.of(new Author(1, "Auth1"),
                        new Author(2, "Auth2"),
                        new Author(3, "Auth3")));

        when(bookDao.getByAuthor(any(Author.class))).thenReturn(getTestBooks());

        List<Book> books = assertDoesNotThrow(() -> bookService.findBooksByAuthor("test"));

        assertEquals(getTestBooks(), books);

        verify(authorService, times(1)).findAuthorsByName("test");
        verify(bookDao, times(3)).getByAuthor(any(Author.class));
    }

    @Test
    void findBooksByGenre() {
        when(genreService.findGenresByName(anyString()))
                .thenReturn(List.of(new Genre(1, "Genre1"),
                        new Genre(2, "Genre2"),
                        new Genre(3, "Genre3")));

        when(bookDao.getByGenre(any(Genre.class))).thenReturn(getTestBooks());

        List<Book> books = assertDoesNotThrow(() -> bookService.findBooksByGenre("test"));

        assertEquals(getTestBooks(), books);

        verify(genreService, times(1)).findGenresByName("test");
        verify(bookDao, times(3)).getByGenre(any(Genre.class));
    }

    @TestFactory
    @DisplayName("Поиск по ID")
    List<DynamicTest> findById() {
        DynamicTest isPresent = DynamicTest.dynamicTest("Книга найдена", () -> {
            when(bookDao.findById(anyLong())).thenReturn(Optional.of(new Book("test", "test")));
            Optional<Book> oBook = bookService.findById(1);
            System.out.println(bookDao.findById(1L));
            System.out.println(oBook);
            assertTrue(oBook.isPresent());
        });
        DynamicTest isNotPresent = DynamicTest.dynamicTest("Книга не найдена", () -> {
            doThrow(new EmptyResultDataAccessException(1)).when(bookDao).findById(anyLong());
            Optional<Book> book = assertDoesNotThrow(() -> bookService.findById(1));
            assertTrue(book.isEmpty());
        });
        return Arrays.asList(isPresent, isNotPresent);
    }

    @Test
    void delete() {
        doAnswer(invocation -> {
            Book a = invocation.getArgument(0);
            assertEquals(1, a.getId());
            return null;
        }).when(bookDao).delete(any(Book.class));

        Book book = new Book("Test","Test");
        book.setId(1);

        assertDoesNotThrow(() -> bookService.delete(book));
    }

    @Test
    void findAll() {
        when(bookDao.findAll()).thenReturn(getTestBooks());

        List<Book> books = assertDoesNotThrow(() -> bookService.findAll());
        assertEquals(getTestBooks(), books);

        //      verify(bookDao, times(1)).findAll();
    }

    @Test
    void addRelations() {
        assertDoesNotThrow(() -> bookService.addRelations(new Book("Test", "test")));
        verify(bookDao, times(1)).save(any(Book.class));
    }

    private List<Book> getTestBooks() {
        return List.of(new Book("Test1", "Test1"),
                new Book("Test2", "Test2"),
                new Book("Test3", "Test3"));
    }

}