package ru.otus.library.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Book {

    private int id;
    private String title;
    private List<Author> authors = new ArrayList<>();
    private List<Genre> genres = new ArrayList<>();
    private String contentPath;


    public Book(int id, String title, String contentPath) {
        this.id = id;
        this.title = title;
        this.contentPath = contentPath;
    }

    public Book(String title, String contentPath) {
        this.title = title;
        this.contentPath = contentPath;
    }

    public void addAuthor(Author author) {
        this.authors.add(author);
    }

    public void addGenre(Genre genre) {
        this.genres.add(genre);
    }
}
