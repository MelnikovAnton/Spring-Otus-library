package ru.otus.library.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.otus.library.model.Comment;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentDao extends JpaRepository<Comment,Long> {

    @Query("select c from Comment c where c.book.id = :id")
    List<Comment> findByBookId(Long id);
}
