package ru.otus.library.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Data
public class Book {

    private int id;
    private String title;
    private List<Author> authors;
    private List<Genre> genres;
    private String contentPath;


    public Book(int id, String title, String contentPath) {
        this.id=id;
        this.title=title;
        this.contentPath=contentPath;
    }

    public Book(String title, String contentPath) {
        this.title=title;
        this.contentPath=contentPath;
    }
}
