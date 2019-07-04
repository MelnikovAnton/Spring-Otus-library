package ru.otus.library.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.otus.library.model.Author;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorDao extends JpaRepository<Author,Long> {

    List<Author> findByNameContaining(String name);
    @Query("select b.authors from Book b where  b.id = :id")
    List<Author> findByBookId(Long id);
}
