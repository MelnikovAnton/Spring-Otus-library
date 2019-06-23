package ru.otus.library.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcInsertOperations;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.dao.BookDao;
import ru.otus.library.dao.mappers.AuthorMapper;
import ru.otus.library.dao.mappers.BookMapper;
import ru.otus.library.dao.mappers.GenreMapper;
import ru.otus.library.model.Author;
import ru.otus.library.model.Book;
import ru.otus.library.model.Genre;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@SuppressWarnings("SqlResolve")
@Repository
@RequiredArgsConstructor
@Transactional
public class BookDaoImpl implements BookDao {

    private final NamedParameterJdbcOperations jdbc;
    private final BookMapper bookMapper;
    private final GenreMapper genreMapper;
    private final AuthorMapper authorMapper;
    private SimpleJdbcInsertOperations simpleJdbcInsert;

    @Autowired
    public void setSimpleJdbcInsert(DataSource dataSource) {
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("book")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public int count() {
        return jdbc.queryForObject("select count(*) from book", new HashMap<>(), Integer.class);
    }

    @Override
    public Book insert(Book book) {
        final Map<String, Object> params = new HashMap<>(2);
        params.put("title", book.getTitle());
        params.put("content_path", book.getContentPath());
        int id = simpleJdbcInsert.executeAndReturnKey(params).intValue();
        book.setId(id);
        addRelations(book);
        return book;
    }

    @Override
    public Book getById(int id) {
        final Map<String, Object> params = new HashMap<>(1);
        params.put("id", id);
        Book book = jdbc.queryForObject("select * from book where id =:id;", params, bookMapper);
        return addAuthorAndGenre(book);
    }

    @Override
    public List<Book> getAll() {
        List<Book> books = jdbc.query("select * from book", new HashMap<>(), bookMapper);
        books.forEach(this::addAuthorAndGenre);
        return books;
    }


    @Override
    public int delete(Book book) {
        final Map<String, Object> params = new HashMap<>(1);
        params.put("id", book.getId());
        return jdbc.update("delete from book where id=:id;", params);
    }

    @Override
    public List<Book> findByTitle(String title) {
        final Map<String, Object> params = new HashMap<>(1);
        params.put("title", "%" + title + "%");
        List<Book> books = jdbc.query("select * from book where title like :title;", params, bookMapper);
        books.forEach(this::addAuthorAndGenre);
        return books;
    }

    @Override
    public List<Book> getByAuthor(Author author) {
        final Map<String, Object> params = new HashMap<>(1);
        params.put("id", author.getId());
        List<Book> books = jdbc.query("select * from book where id in " +
                "(select book_id from author_book where author_id=:id);", params, bookMapper);
        books.forEach(this::addAuthorAndGenre);
        return books;
    }

    @Override
    public List<Book> getByGenre(Genre genre) {
        final Map<String, Object> params = new HashMap<>(1);
        params.put("id", genre.getId());
        List<Book> books = jdbc.query("select * from book where id in " +
                "(select book_id from genre_book where genre_id=:id);", params, bookMapper);
        books.forEach(this::addAuthorAndGenre);
        return books;
    }


    private Book addAuthorAndGenre(Book book) {
        int id = book.getId();
        book.setGenres(findGenre(id));
        book.setAuthors(findAuthor(id));
        return book;
    }


    private List<Genre> findGenre(int bookId) {
        final Map<String, Object> params = new HashMap<>(1);
        params.put("id", bookId);
        return jdbc.query("select * from genre where id in " +
                "(select genre_id from genre_book where book_id=:id);", params, genreMapper);
    }


    private List<Author> findAuthor(int bookId) {
        final Map<String, Object> params = new HashMap<>(1);
        params.put("id", bookId);
        return jdbc.query("select * from author where id in " +
                "(select author_id from author_book where book_id=:id);", params, authorMapper);
    }

    private void addRelations(Book book) {
        addAuthorRelations(book);
        addGenreRelations(book);
    }

    private void addAuthorRelations(Book book) {
        final Map<String, Object> params = new HashMap<>(2);
        params.put("bookId", book.getId());
        List<Author> authors = book.getAuthors();
        if (authors==null || authors.isEmpty()) return;
        String query = "insert into author_book (book_id, author_id)\n" +
                "values (:bookId, :authorId);";
        authors.forEach(a -> {
            params.put("authorId", a.getId());
            jdbc.update(query, params);
        });
    }

    private void addGenreRelations(Book book) {
        final Map<String, Object> params = new HashMap<>(2);
        params.put("bookId", book.getId());
        List<Genre> genres = book.getGenres();
        if (genres==null || genres.isEmpty()) return;
        String query = "insert into genre_book (book_id, genre_id)\n" +
                "values (:bookId, :genreId);";
        genres.forEach(a -> {
            params.put("genreId", a.getId());
            jdbc.update(query, params);
        });
    }


}
