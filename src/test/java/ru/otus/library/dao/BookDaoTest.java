package ru.otus.library.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.otus.library.dao.impl.BookDaoImpl;
import ru.otus.library.dao.mappers.AuthorMapper;
import ru.otus.library.dao.mappers.BookMapper;
import ru.otus.library.dao.mappers.GenreMapper;
import ru.otus.library.model.Author;
import ru.otus.library.model.Book;
import ru.otus.library.model.Genre;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@JdbcTest
@Import({BookDaoImpl.class, BookMapper.class, GenreMapper.class, AuthorMapper.class})
class BookDaoTest {

    @Autowired
    private BookDao bookDao;

    @Test
    @DisplayName("Получениене количества книг")
    void count() {
        int count = bookDao.count();
        assertEquals(count, 3);
    }

    @Test
    @DisplayName("Вставка с получением ID")
    void insert() {
        Book book = assertDoesNotThrow(() -> bookDao.insert(new Book("qwe", "ewq")));
        assertTrue(book.getId() > 0);
    }

    @TestFactory
    @DisplayName("Получение книги по ID")
    List<DynamicTest> getById() {
        DynamicTest book1 = DynamicTest.dynamicTest("ID = 1", () -> {
            Book book = assertDoesNotThrow(() -> bookDao.getById(1).orElseThrow());
            assertEquals(book.getId(), 1);
        });
        DynamicTest book2 = DynamicTest.dynamicTest("ID = 10", () -> assertThrows(EmptyResultDataAccessException.class, () -> bookDao.getById(10)));
        return Arrays.asList(book1, book2);
    }


    @Test
    @DisplayName("Получение всех книг")
    void getAll() {
        List<Book> books = assertDoesNotThrow(() -> bookDao.getAll());
        assertEquals(books.size(), 3);
    }

    @TestFactory
    @DisplayName("Удаление книги")
    List<DynamicTest> delete() {
        Book book = new Book("Test", "Test");

        DynamicTest delExists = DynamicTest.dynamicTest("Удаление существующей книги", () -> {
            book.setId(1);
            int c = assertDoesNotThrow(() -> bookDao.delete(book));
            assertEquals(c, 1);
        });

        DynamicTest delDoseNotExists = DynamicTest.dynamicTest("Удаление не существующей книги", () -> {
            book.setId(10);
            int c1 = assertDoesNotThrow(() -> bookDao.delete(book));
            assertEquals(c1, 0);
        });

        return Arrays.asList(delExists, delDoseNotExists);
    }

    @TestFactory
    @DisplayName("Поиск по заголовку")
    List<DynamicTest> findByTitle() {
        DynamicTest empty = DynamicTest.dynamicTest("Пустой заголовок", () -> {
            List<Book> books = assertDoesNotThrow(() -> bookDao.findByTitle(""));
            assertEquals(books.size(), 3);
        });
        DynamicTest part = DynamicTest.dynamicTest("Часть заголовока", () -> {
            List<Book> books = assertDoesNotThrow(() -> bookDao.findByTitle("est"));
            assertEquals(books.size(), 3);
        });
        DynamicTest full = DynamicTest.dynamicTest("Полный заголовок", () -> {
            List<Book> books = assertDoesNotThrow(() -> bookDao.findByTitle("Test2"));
            assertEquals(books.size(), 1);
            assertEquals(books.get(0).getId(), 2);
        });
        DynamicTest noMatch = DynamicTest.dynamicTest("Не совпадающий заголовок", () -> {
            List<Book> books = assertDoesNotThrow(() -> bookDao.findByTitle("!№;%:?*:;%;№"));
            assertEquals(books.size(), 0);
        });
        return Arrays.asList(empty, part, full, noMatch);
    }

    @TestFactory
    @DisplayName("Поиск по автору")
    List<DynamicTest> getByAuthor() {
        DynamicTest auth1 = DynamicTest.dynamicTest("Автор с ID 1(есть в базе)", () -> {
            Author author = new Author("Test");
            author.setId(1);
            List<Book> books = assertDoesNotThrow(() -> bookDao.getByAuthor(author));
            // System.out.println(books);
            assertEquals(books.size(), 1);
        });

        DynamicTest auth2 = DynamicTest.dynamicTest("Автор с ID 10(нет есть в базе)", () -> {
            Author author = new Author("Test");
            author.setId(10);
            List<Book> books = assertDoesNotThrow(() -> bookDao.getByAuthor(author));
            //    System.out.println(books);
            assertEquals(books.size(), 0);
        });
        return Arrays.asList(auth1, auth2);
    }

    @TestFactory
    @DisplayName("Поиск по жанру")
    List<DynamicTest> getByGenre() {
        DynamicTest genre1 = DynamicTest.dynamicTest("Жанр с ID 1(есть в базе)", () -> {
            Genre genre = new Genre("Test");
            genre.setId(1);
            List<Book> books = assertDoesNotThrow(() -> bookDao.getByGenre(genre));
            // System.out.println(books);
            assertEquals(books.size(), 1);
        });

        DynamicTest genre2 = DynamicTest.dynamicTest("Жанр с ID 10(нет есть в базе)", () -> {
            Genre genre = new Genre("Test");
            genre.setId(10);
            List<Book> books = assertDoesNotThrow(() -> bookDao.getByGenre(genre));
            //    System.out.println(books);
            assertEquals(books.size(), 0);
        });
        return Arrays.asList(genre1, genre2);
    }

    @Test
    @DisplayName("Добавление авторов и жанров в книгу")
    void addRelations() {
        Book book = bookDao.getById(1).orElseThrow();

        Genre genre = new Genre("Genre3");
        genre.setId(3);

        Author author = new Author("Author3");
        author.setId(3);

        assertFalse(book.getAuthors().contains(author));
        assertFalse(book.getGenres().contains(genre));

        book.addAuthor(author);
        book.addGenre(genre);

        assertDoesNotThrow(() -> bookDao.addRelations(book));

        assertTrue(bookDao.getByGenre(genre).contains(book));
        assertTrue(bookDao.getByAuthor(author).contains(book));

    }
}