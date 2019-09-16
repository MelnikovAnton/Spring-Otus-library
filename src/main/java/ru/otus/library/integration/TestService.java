package ru.otus.library.integration;

import org.springframework.stereotype.Service;
import ru.otus.library.model.Book;

@Service
public class TestService {

    public Book test(Book book){
        System.out.println("******************");
        System.out.println(book);
        System.out.println("*******************");
        return book;
    }
}
