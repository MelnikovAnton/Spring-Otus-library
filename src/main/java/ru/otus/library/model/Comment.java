package ru.otus.library.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@Document
public class Comment {

//    @Id
    private String id;

//    @DBRef
    private Book book;

    private String comment;

    public Comment(Book book, String comment) {
        this.book = book;
        this.comment = comment;
    }
}
