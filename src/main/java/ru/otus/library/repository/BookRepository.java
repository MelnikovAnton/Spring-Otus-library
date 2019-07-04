package ru.otus.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.otus.library.model.Author;
import ru.otus.library.model.Book;
import ru.otus.library.model.Genre;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book,Long> {

    List<Book> findByTitleContaining(String title);
    @Query("select b from Book b " +
            "join b.authors a" +
            " where a= :author")
    List<Book> getByAuthor(Author author);
    @Query("select b from Book b " +
            "join b.genres g" +
            " where g= :genre")
    List<Book> getByGenre(Genre genre);

}
