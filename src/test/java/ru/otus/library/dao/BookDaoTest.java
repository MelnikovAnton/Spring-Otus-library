package ru.otus.library.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.otus.library.model.Author;
import ru.otus.library.model.Book;
import ru.otus.library.model.Genre;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
class BookDaoTest {

    @Autowired
    private BookDao bookDao;

    @PersistenceContext
    private EntityManager em;

    @Test
    @DisplayName("Получениене количества книг")
    void count() {
        long count = bookDao.count();
        assertEquals(count, 3);
    }

    @Test
    @DisplayName("Вставка с получением ID")
    void insert() {
        Book book = new Book("qwe", "ewq");
        assertDoesNotThrow(() -> bookDao.save(book));

        em.refresh(book);
        em.detach(book);

        Optional<Book> result = assertDoesNotThrow(() -> bookDao.findById(book.getId()));
        assertTrue(result.isPresent());
        assertEquals(book, result.get());
    }

    @TestFactory
    @DisplayName("Получение книги по ID")
    List<DynamicTest> getById() {
        DynamicTest book1 = DynamicTest.dynamicTest("ID = 1", () -> {
            Book book = assertDoesNotThrow(() -> bookDao.findById(1L).orElseThrow());
            assertEquals(book.getId(), 1);
        });
        DynamicTest book2 = DynamicTest.dynamicTest("ID = Integer.MAX_VALUE+1", () -> {
            Optional<Book> book = assertDoesNotThrow(() -> bookDao.findById(Integer.MAX_VALUE + 1L));
            assertTrue(book.isEmpty());
        });
        return Arrays.asList(book1, book2);
    }


    @Test
    @DisplayName("Получение всех книг")
    void getAll() {
        List<Book> books = assertDoesNotThrow(() -> bookDao.findAll());
        assertEquals(books.size(), 3);
    }

    @TestFactory
    @DisplayName("Удаление книги")
    List<DynamicTest> delete() {
        Book book = new Book("Test", "Test");

        DynamicTest delExists = DynamicTest.dynamicTest("Удаление существующей книги", () -> {
            book.setId(1);
            assertDoesNotThrow(() -> bookDao.delete(book));
            assertTrue(bookDao.findById(1L).isEmpty());

        });

        DynamicTest delDoseNotExists = DynamicTest.dynamicTest("Удаление не существующей книги", () -> {
            book.setId(Integer.MAX_VALUE + 1);
            assertDoesNotThrow(() -> bookDao.delete(book));
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
            List<Book> books = assertDoesNotThrow(() -> bookDao.findByTitle("oo"));
            assertEquals(books.size(), 3);
        });
        DynamicTest full = DynamicTest.dynamicTest("Полный заголовок", () -> {
            List<Book> books = assertDoesNotThrow(() -> bookDao.findByTitle("Book 2"));
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
        Book book = bookDao.findById(1L).orElseThrow();

        Genre genre = new Genre("Genre3");
        genre.setId(3);

        Author author = new Author("Author3");
        author.setId(3);

        assertFalse(book.getAuthors().contains(author));
        assertFalse(book.getGenres().contains(genre));

        book.addAuthor(author);
        book.addGenre(genre);

        assertDoesNotThrow(() -> bookDao.save(book));

        assertTrue(bookDao.getByGenre(genre).contains(book));
        assertTrue(bookDao.getByAuthor(author).contains(book));

    }
}