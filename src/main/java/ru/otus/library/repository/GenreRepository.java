package ru.otus.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.otus.library.model.Genre;

import java.util.List;

@Repository
public interface GenreRepository extends JpaRepository<Genre,Long> {

    List<Genre> findByNameContaining(String name);

    @Query("select b.genres from Book b where  b.id = :id")
    List<Genre> findByBookId(long id);
}
