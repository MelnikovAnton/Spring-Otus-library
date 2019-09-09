package ru.otus.library.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.stereotype.Service;
import ru.otus.library.model.Book;
import ru.otus.library.repository.BookRepository;
import ru.otus.library.security.util.AclCreationUtil;
import ru.otus.library.services.BookService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AclCreationUtil aclCreationUtil;

    @Override
    public Book saveBook(Book book) {
        Book savedBook = bookRepository.save(book);
        aclCreationUtil.createDefaultAcl(new ObjectIdentityImpl(savedBook));
        return savedBook;
    }

    @Override
    public List<Book> findBooksByTitle(String title) {
        return bookRepository.findByTitleContaining(title);
    }

    @Override
    public List<Book> findBooksByAuthor(String author) {
        return bookRepository.findByAuthorsNameContains(author);
    }

    @Override
    public List<Book> findBooksByGenre(String genre) {
        return bookRepository.findByGenresNameContains(genre);
    }

    @Override
    public Optional<Book> findById(String id) {
        try {
            return bookRepository.findById(id);
        } catch (EmptyResultDataAccessException e) {
            log.warn("Return Empty result.", e);
            return Optional.empty();
        }

    }

    @Override
    public String delete(Book book) {
        bookRepository.delete(book);
        return book.getId();
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public void addRelations(Book book) {
        bookRepository.save(book);
    }
}
