package ru.otus.library.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.otus.library.integration.BookIntegrationService;
import ru.otus.library.model.Book;
import ru.otus.library.repository.BookRepository;
import ru.otus.library.services.BookService;

import java.util.List;
import java.util.Optional;

@Service("bookService")
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookIntegrationService bookIntegrationService;


    @Override
    public Book saveBook(Book book) {
        log.info("Save book " + book);
        return bookIntegrationService.createBook(book);
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
        log.info("Delete book " + book);
        bookIntegrationService.deleteBook(book);
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
