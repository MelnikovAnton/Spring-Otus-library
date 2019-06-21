package ru.otus.library.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.library.dao.BookDao;
import ru.otus.library.model.Book;
import ru.otus.library.services.BookService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookDao bookDao;


    @Override
    public int saveBook(Book book) {
        return bookDao.insert(book);
    }

    @Override
    public List<Book> findBooksByTitle(String title) {
        return bookDao.findByTitle(title);
    }

    @Override
    public List<Book> findBooksByTitleAuthor(String author) {
        return null;
    }

    @Override
    public List<Book> findBooksByTitleGenre(String genre) {
        return null;
    }


}
