package ru.otus.library.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.dao.AuthorDao;
import ru.otus.library.model.Author;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("SqlResolve")
@Slf4j
@RequiredArgsConstructor
@Repository
@Transactional
public class AuthorDaoImpl implements AuthorDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public long count() {
        return (long) em.createQuery("select count(a) from Author a").getSingleResult();
    }

    @Override
    public void insert(Author author) {
        em.persist(author);
    }

    @Override
    public Optional<Author> getById(long id) {
        return Optional.ofNullable(em.find(Author.class, id));
    }

    @Override
    public List<Author> getAll() {
        return em.createQuery("select a from Author a").getResultList();
    }

    @Override
    public void delete(Author author) {
        em.remove(em.contains(author) ? author : em.merge(author));
    }

    @Override
    public List<Author> findByName(String name) {
        Query query = em.createQuery("select a from Author a where name like :name");
        query.setParameter("name", "%" + name + "%");
        return query.getResultList();
    }

    @Override
    public List<Author> findByBookId(long id) {
        Query query = em.createQuery("select b.authors from Book b " +
                "where  b.id = :id");
        query.setParameter("id", id);
        return query.getResultList();
    }
}
