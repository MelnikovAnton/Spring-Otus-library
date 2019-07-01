package ru.otus.library.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.dao.BookDao;
import ru.otus.library.model.Author;
import ru.otus.library.model.Book;
import ru.otus.library.model.Genre;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;


@SuppressWarnings("ALL")
@Repository
@RequiredArgsConstructor
@Transactional
public class BookDaoImpl implements BookDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public long count() {
        return (long) em.createQuery("select count(b) from Book b").getSingleResult();
    }

    @Override
    public void insert(Book book) {
        em.persist(book);
    }

    @Override
    public Optional<Book> getById(long id) {
        return Optional.ofNullable(em.find(Book.class, id));
    }

    @Override
    public List<Book> getAll() {
        return em.createQuery("select b from Book b").getResultList();
    }

    @Override
    public void delete(Book book) {
        em.remove(em.contains(book)?book:em.merge(book));
    }

    @Override
    public List<Book> findByTitle(String title) {
        Query query = em.createQuery("select b from Book b where title like :title");
        query.setParameter("title", "%" + title + "%");
        return query.getResultList();
    }

    @Override
    public List<Book> getByAuthor(Author author) {
        Query query = em.createQuery("select b from Book b " +
                "join b.authors a" +
                " where a= :author");
        query.setParameter("author", author);
        return query.getResultList();
    }

    @Override
    public List<Book> getByGenre(Genre genre) {
        Query query = em.createQuery("select b from Book b " +
                "join b.genres g" +
                " where g= :genre");
        query.setParameter("genre", genre);
        return query.getResultList();
    }

    @Override
    public void addRelations(Book book) {
        em.merge(book);
    }
}
