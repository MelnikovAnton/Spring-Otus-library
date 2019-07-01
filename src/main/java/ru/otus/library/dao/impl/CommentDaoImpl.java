package ru.otus.library.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.library.dao.CommentDao;
import ru.otus.library.model.Book;
import ru.otus.library.model.Comment;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;


@SuppressWarnings("JpaQlInspection")
@Repository
@RequiredArgsConstructor
@Transactional
public class CommentDaoImpl implements CommentDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public long count() {
        return (long) em.createQuery("select count(c) from Comment c").getSingleResult();
    }

    @Override
    public void insert(Comment comment) {
        em.persist(comment);
    }

    @Override
    public List<Comment> findByBook(Book book) {
        Query query = em.createQuery("select c from Comment c " +
                "join c.book b" +
                " where b= :book");
        query.setParameter("book", book);
        return query.getResultList();
    }

    @Override
    public Optional<Comment> findById(long id) {
        return Optional.ofNullable(em.find(Comment.class, id));
    }

    @Override
    public List<Comment> findAll() {
        return em.createQuery("select c from Comment c").getResultList();
    }

    @Override
    public void delete(Comment comment) {
        em.remove(em.contains(comment) ? comment : em.merge(comment));
    }
}
