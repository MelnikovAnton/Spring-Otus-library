package ru.otus.library.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.otus.library.model.Genre;

import java.util.List;
import java.util.Optional;

@Repository
public interface GenreDao extends JpaRepository<Genre,Long> {

    List<Genre> findByName(String name);

    @Query("select b.genres from Book b where  b.id = :id")
    List<Genre> findByBookId(long id);
}
