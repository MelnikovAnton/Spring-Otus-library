package ru.otus.library.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.dao.GenreDao;
import ru.otus.library.model.Author;
import ru.otus.library.model.Genre;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("SqlResolve")
@Repository
@RequiredArgsConstructor
@Transactional
public class GenreDaoImpl implements GenreDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public long count() {
        return (long) em.createQuery("select count(g) from Genre g").getSingleResult();
    }

    @Override
    public void insert(Genre genre) {
        em.persist(genre);
    }

    @Override
    public Optional<Genre> getById(long id) {
        return Optional.ofNullable(em.find(Genre.class, id));
    }

    @Override
    public List<Genre> getAll() {
        return em.createQuery("select g from Genre g").getResultList();
    }

    @Override
    public void delete(Genre genre) {
        em.remove(em.contains(genre)?genre:em.merge(genre));
    }

    @Override
    public List<Genre> findByName(String name) {
        Query query = em.createQuery("select g from Genre g where name like :name");
        query.setParameter("name", "%" + name + "%");
        return query.getResultList();
    }

    @Override
    public List<Genre> findByBookId(long id) {
        Query query = em.createQuery("select b.genres from Book b " +
                "where  b.id = :id");
        query.setParameter("id", id);
        return query.getResultList();
    }
}
