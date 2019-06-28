package ru.otus.library.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.simple.SimpleJdbcInsertOperations;
import org.springframework.stereotype.Repository;
import ru.otus.library.dao.GenreDao;
import ru.otus.library.dao.mappers.GenreMapper;
import ru.otus.library.model.Genre;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings("SqlResolve")
@Repository
@RequiredArgsConstructor
public class GenreDaoImpl implements GenreDao {

    private final NamedParameterJdbcOperations jdbc;

    private final GenreMapper mapper;


    private SimpleJdbcInsertOperations simpleJdbcInsert;


    @Autowired
    public void setSimpleJdbcInsert(DataSource dataSource) {
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("genre")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public int count() {
        return jdbc.queryForObject("select count(*) from genre;", new HashMap<>(), Integer.class);
    }

    @Override
    public Genre insert(Genre genre) {
        final Map<String, Object> params = new HashMap<>(1);
        params.put("name", genre.getName());
        int id = simpleJdbcInsert.executeAndReturnKey(params).intValue();
        genre.setId(id);
        return genre;
    }

    @Override
    public Optional<Genre> getById(int id) {
        final Map<String, Object> params = new HashMap<>(1);
        params.put("id", id);
        return Optional.ofNullable(jdbc.queryForObject("select * from genre where id =:id;", params, mapper));
    }

    @Override
    public List<Genre> getAll() {
        return jdbc.query("select * from genre;", new HashMap<>(), mapper);

    }

    @Override
    public int delete(Genre genre) {
        final Map<String, Object> params = new HashMap<>(1);
        params.put("id", genre.getId());
        deleteGenreRelation(genre);
        return jdbc.update("delete from genre where id=:id;", params);
    }

    @Override
    public List<Genre> findByName(String name) {
        final Map<String, Object> params = new HashMap<>(1);
        params.put("title", "%" + name + "%");
        return jdbc.query("select * from genre where name like :title;", params, mapper);
    }


    @Override
    public List<Genre> findByBookId(int id) {
        final Map<String, Object> params = new HashMap<>(1);
        params.put("id", id);
        return jdbc.query("select * from genre where id in " +
                "(select genre_id from genre_book where book_id=:id);", params, mapper);
    }

    private void deleteGenreRelation(Genre genre) {
        final Map<String, Object> params = new HashMap<>(1);
        params.put("genreId", genre.getId());
        String query = "delete from genre_book where genre_id = :genreId;";
        jdbc.update(query, params);
    }
}
