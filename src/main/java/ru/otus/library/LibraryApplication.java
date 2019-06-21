package ru.otus.library;

import org.h2.tools.Console;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import ru.otus.library.dao.BookDao;
import ru.otus.library.model.Book;

import java.sql.SQLException;

@SpringBootApplication
public class LibraryApplication {

    public static void main(String[] args) throws SQLException {
        ApplicationContext ctx = SpringApplication.run(LibraryApplication.class, args);

        BookDao bookDao = ctx.getBean(BookDao.class);

//        Console.main(args);
//
        Book b1 = new Book(10, "Test");
        Book b2 = new Book(20, "Test2");
        System.out.println("count before insert" + bookDao.count());
        bookDao.insert(b1);
        bookDao.insert(b2);
        System.out.println("get by id " + bookDao.getById(1));
        System.out.println("get all " + bookDao.getAll());
        System.out.println("find by title " + bookDao.findByTitle("est"));
        System.out.println("delite " + bookDao.delete(b2));
        System.out.println(bookDao.count());
        System.out.println("find by title" + bookDao.findByTitle("est"));


        Console.main(args);
    }

}
