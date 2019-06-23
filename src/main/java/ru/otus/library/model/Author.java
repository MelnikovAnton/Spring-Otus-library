package ru.otus.library.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@AllArgsConstructor
public class Author {

    private int id;
    private String name;

    public Author(String name) {
        this.name = name;
    }
}
