package ru.otus.library.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.otus.library.dao.impl.BookDaoImpl;
import ru.otus.library.dao.mappers.AuthorMapper;
import ru.otus.library.dao.mappers.BookMapper;
import ru.otus.library.dao.mappers.GenreMapper;
import ru.otus.library.model.Book;

//import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;


//TODO Implements
@JdbcTest
@Import({BookDaoImpl.class, BookMapper.class,GenreMapper.class,AuthorMapper.class})
class BookDaoTest {

    @Autowired
    private BookDao bookDao;

    @Test
    void count() {
        int count = bookDao.count();
        assertThat(count).isEqualTo(3);
    }

    @Test
    void insert() {
        bookDao.insert(new Book("qwe","ewq"));
        System.out.println(bookDao.count());
    }

    @Test
    void getById() {
        System.out.println(bookDao.count());
    }

    @Test
    void getAll() {
        System.out.println(bookDao.count());
    }

    @Test
    void delete() {
        Optional<Book> book = bookDao.getById(1);
        bookDao.delete(book.orElseThrow());
        System.out.println(bookDao.count());

    }

    @Test
    void findByTitle() {
    }

    @Test
    void getByAuthor() {
    }

    @Test
    void getByGenre() {
    }

    @Test
    void addRelations() {
    }
}