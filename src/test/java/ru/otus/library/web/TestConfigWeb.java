package ru.otus.library.web;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import ru.otus.library.services.AuthorService;
import ru.otus.library.services.BookService;
import ru.otus.library.services.CommentService;
import ru.otus.library.services.GenreService;


/*
 *Сделал чтобы в тестах каждый раз не поднималась embeddedMongo
 * Наверняка можно сделать как-то проще.
 */

@SpringBootConfiguration
@EnableConfigurationProperties
@ComponentScan(basePackages = {"ru.otus.library.services"
        , "ru.otus.library.repository"
        , "ru.otus.library.controllers"})
@EnableAutoConfiguration(exclude = {EmbeddedMongoAutoConfiguration.class,
        MongoAutoConfiguration.class,
        MongoDataAutoConfiguration.class})
@ActiveProfiles("WebTest")
@Profile("!Test")
public class TestConfigWeb {

    @MockBean
    public BookService bookService;
    @MockBean
    public GenreService genreService;
    @MockBean
    public AuthorService authorService;
    @MockBean
    public CommentService commentService;

}
