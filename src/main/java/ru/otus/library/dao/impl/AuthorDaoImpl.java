package ru.otus.library.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.library.dao.AuthorDao;
import ru.otus.library.dao.mappers.AuthorMapper;
import ru.otus.library.model.Author;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("SqlResolve")
@Slf4j
@RequiredArgsConstructor
@Repository
public class AuthorDaoImpl implements AuthorDao {

    private final NamedParameterJdbcOperations jdbc;

    private final AuthorMapper mapper;

    @Override
    public int count() {
        return jdbc.queryForObject("select count(*) from author", new HashMap<>(), Integer.class);
    }

    @Override
    public int insert(Author author) {
        final Map<String, Object> params = new HashMap<>(1);
        params.put("id", author.getId());
        params.put("name", author.getName());
        return jdbc.update("insert into author (id, 'name') values (:id, :name);", params);
    }

    @Override
    public Author getById(int id) {
        final Map<String, Object> params = new HashMap<>(1);
        params.put("id", id);
        return jdbc.queryForObject("select * from author where id =:id;", params, mapper);
    }

    @Override
    public List<Author> getAll() {
        return jdbc.query("select * from author", new HashMap<>(), mapper);

    }

    @Override
    public int delete(Author author) {
        final Map<String, Object> params = new HashMap<>(1);
        params.put("id", author.getId());
        return jdbc.update("delete from author where id=:id;", params);
    }

    @Override
    public List<Author> findByName(String name) {
        final Map<String, Object> params = new HashMap<>(1);
        params.put("title", "%" + name + "%");
        return jdbc.query("select * from author where title like :title;", params, mapper);
    }
}
