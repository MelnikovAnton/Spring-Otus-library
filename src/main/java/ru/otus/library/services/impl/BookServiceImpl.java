package ru.otus.library.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.library.model.Book;
import ru.otus.library.repository.BookRepository;
import ru.otus.library.services.BookService;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public Mono<Book> saveBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public Flux<Book> findBooksByTitle(String title) {
        return bookRepository.findByTitleContaining(title);
    }

    @Override
    public Flux<Book> findBooksByAuthor(String author) {
        return bookRepository.findByAuthorsNameContains(author);
    }

    @Override
    public Flux<Book> findBooksByGenre(String genre) {
        return  bookRepository.findByGenresNameContains(genre);
    }

    @Override
    public Mono<Book> findById(String id) {
            return bookRepository.findById(id);
    }

    @Override
    public Mono<Void> delete(String bookId) {
        return bookRepository.deleteById(bookId);
    }

    @Override
    public Flux<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public void addRelations(Book book) {
        bookRepository.save(book);
    }
}
