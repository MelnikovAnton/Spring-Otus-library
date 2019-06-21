package ru.otus.library.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.library.dao.BookDao;
import ru.otus.library.dao.mappers.BookMapper;
import ru.otus.library.model.Author;
import ru.otus.library.model.Book;
import ru.otus.library.model.Genre;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@SuppressWarnings("SqlResolve")
@Repository
@RequiredArgsConstructor
public class BookDaoImpl implements BookDao {

    private final NamedParameterJdbcOperations jdbc;

    private final BookMapper mapper;


    @Override
    public int count() {
        return jdbc.queryForObject("select count(*) from book", new HashMap<>(), Integer.class);
    }

    @Override
    public int insert(Book book) {
        final Map<String, Object> params = new HashMap<>(1);
        params.put("id", book.getId());
        params.put("title", book.getTitle());
        return jdbc.update("insert into book (id, title) values (:id, :title);", params);
    }

    @Override
    public Book getById(int id) {
        final Map<String, Object> params = new HashMap<>(1);
        params.put("id", id);
        return jdbc.queryForObject("select * from book where id =:id;", params, mapper);
    }

    @Override
    public List<Book> getAll() {
        return jdbc.query("select * from book", new HashMap<>(), mapper);
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
        return jdbc.query("select * from book where title like :title;", params, mapper);
    }

    @Override
    public List<Book> getByAuthor(Author author) {
        return null;
    }

    @Override
    public List<Book> getByGenre(Genre genre) {
        return null;
    }
}
