package ru.otus.library.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.library.dao.AuthorDao;
import ru.otus.library.dao.BookDao;
import ru.otus.library.dao.GenreDao;
import ru.otus.library.model.Author;
import ru.otus.library.model.Book;
import ru.otus.library.model.Genre;
import ru.otus.library.services.AuthorService;
import ru.otus.library.services.BookService;
import ru.otus.library.services.GenreService;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookDao bookDao;

    private final AuthorService authorService;
    private final GenreService genreService;


    @Override
    public Book saveBook(Book book) {
        List<Author> addedAuthors = book.getAuthors().stream()
                .filter(a -> a.getId() <= 0)
                .map(authorService::saveAuthor)
                .collect(Collectors.toList());
        List<Genre> addedGenres = book.getGenres().stream()
                .filter(a -> a.getId() <= 0)
                .map(genreService::saveGenre)
                .collect(Collectors.toList());
        return bookDao.insert(book);
    }

    @Override
    public List<Book> findBooksByTitle(String title) {
        return bookDao.findByTitle(title);
    }

    @Override
    public List<Book> findBooksByAuthor(String author) {
        return authorService.findAuthorsByName(author)
                .stream()
                .map(bookDao::getByAuthor)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

    }

    @Override
    public List<Book> findBooksByGenre(String genre) {
        return genreService.findGenresByName(genre)
                .stream()
                .map(bookDao::getByGenre)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @Override
    public Book findById(int id) {
        return bookDao.getById(id);
    }

    @Override
    public int delete(Book book) {
        return bookDao.delete(book);
    }

    @Override
    public List<Book> findAll() {
        return bookDao.getAll();
    }
}
