package ru.otus.library.repository;

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


/*
 *Сделал чтобы в тестах каждый раз не поднималась embeddedMongo
 * Наверняка можно сделать как-то проще.
 */

@SpringBootConfiguration
@EnableConfigurationProperties
@ComponentScan(basePackages = {"ru.otus.library.services", "ru.otus.library.repository"})
@EnableAutoConfiguration(exclude = {EmbeddedMongoAutoConfiguration.class,
        MongoAutoConfiguration.class,
        MongoDataAutoConfiguration.class})
@ActiveProfiles("RepositoryTest")
@Profile("!Test")
public class TestConfigServices {

    @MockBean
    public BookRepository bookRepository;
    @MockBean
    public GenreRepository genreRepository;
    @MockBean
    public AuthorRepository authorRepository;
    @MockBean
    public CommentRepository commentRepository;

}
