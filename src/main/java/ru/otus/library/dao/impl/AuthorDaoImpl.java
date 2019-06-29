package ru.otus.library.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcInsertOperations;
import org.springframework.stereotype.Repository;
import ru.otus.library.dao.AuthorDao;
import ru.otus.library.dao.mappers.AuthorMapper;
import ru.otus.library.model.Author;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings("SqlResolve")
@Slf4j
@RequiredArgsConstructor
@Repository
public class AuthorDaoImpl implements AuthorDao {

    private final NamedParameterJdbcOperations jdbc;

    private final AuthorMapper mapper;
    private SimpleJdbcInsertOperations simpleJdbcInsert;


    @Autowired
    public void setSimpleJdbcInsert(DataSource dataSource) {
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("author")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public int count() {
        return jdbc.queryForObject("select count(*) from author", new HashMap<>(), Integer.class);
    }

    @Override
    public Author insert(Author author) {
        final Map<String, Object> params = new HashMap<>(1);
        params.put("name", author.getName());
        int id = simpleJdbcInsert.executeAndReturnKey(params).intValue();
        author.setId(id);
        return author;
    }

    @Override
    public Optional<Author> getById(int id) {
        final Map<String, Object> params = new HashMap<>(1);
        params.put("id", id);
        return Optional.ofNullable(jdbc.queryForObject("select * from author where id =:id;", params, mapper));
    }

    @Override
    public List<Author> getAll() {
        return jdbc.query("select * from author", new HashMap<>(), mapper);

    }

    @Override
    public int delete(Author author) {
        final Map<String, Object> params = new HashMap<>(1);
        params.put("id", author.getId());
        deleteAuthorRelation(author);
        return jdbc.update("delete from author where id=:id;", params);
    }

    @Override
    public List<Author> findByName(String name) {
        final Map<String, Object> params = new HashMap<>(1);
        params.put("name", "%" + name + "%");
        return jdbc.query("select * from author where name like :name;", params, mapper);
    }

    @Override
    public List<Author> findByBookId(int id) {
        final Map<String, Object> params = new HashMap<>(1);
        params.put("id", id);
        return jdbc.query("select * from author where id in " +
                "(select author_id from author_book where book_id=:id);", params, mapper);
    }

    private void deleteAuthorRelation(Author genre) {
        final Map<String, Object> params = new HashMap<>(1);
        params.put("authorId", genre.getId());
        String query = "delete from author_book where author_id = :authorId;";
        jdbc.update(query, params);
    }
}
