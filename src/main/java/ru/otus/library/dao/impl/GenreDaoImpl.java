package ru.otus.library.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.library.dao.GenreDao;
import ru.otus.library.dao.mappers.GenreMapper;
import ru.otus.library.model.Genre;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("SqlResolve")
@Repository
@RequiredArgsConstructor
public class GenreDaoImpl implements GenreDao {

    private final NamedParameterJdbcOperations jdbc;

    private final GenreMapper mapper;

    @Override
    public int count() {
        return jdbc.queryForObject("select count(*) from genre;", new HashMap<>(), Integer.class);
    }

    @Override
    public int insert(Genre genre) {
        final Map<String, Object> params = new HashMap<>(1);
        params.put("id", genre.getId());
        params.put("name", genre.getName());
        return jdbc.update("insert into genre (id, 'name') values (:id, :name);", params);
    }

    @Override
    public Genre getById(int id) {
        final Map<String, Object> params = new HashMap<>(1);
        params.put("id", id);
        return jdbc.queryForObject("select * from genre where id =:id;", params, mapper);
    }

    @Override
    public List<Genre> getAll() {
        return jdbc.query("select * from genre;", new HashMap<>(), mapper);

    }

    @Override
    public int delete(Genre genre) {
        final Map<String, Object> params = new HashMap<>(1);
        params.put("id", genre.getId());
        return jdbc.update("delete from genre where id=:id;", params);
    }

    @Override
    public List<Genre> findByName(String name) {
        final Map<String, Object> params = new HashMap<>(1);
        params.put("title", "%" + name + "%");
        return jdbc.query("select * from genre where 'name' like :title;", params, mapper);
    }
}
