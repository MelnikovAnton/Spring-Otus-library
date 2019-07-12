package ru.otus.library;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.otus.library.repository.AuthorRepository;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class LibraryApplication {

    @Autowired
    private AuthorRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(LibraryApplication.class, args);
    }

    @PostConstruct
    public void init() {
        repository.findByBookId("BOOK");
    }
}
