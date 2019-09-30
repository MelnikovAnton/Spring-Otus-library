package ru.otus.library.services;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.test.context.ActiveProfiles;
import ru.otus.library.integration.AuthorIntegrationService;
import ru.otus.library.integration.BookIntegrationService;
import ru.otus.library.integration.CommentIntegrationService;
import ru.otus.library.integration.GenreIntegrationService;
import ru.otus.library.repository.AuthorRepository;
import ru.otus.library.repository.BookRepository;
import ru.otus.library.repository.CommentRepository;
import ru.otus.library.repository.GenreRepository;
import ru.otus.library.security.util.AclEditService;


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
@EnableIntegration
@IntegrationComponentScan
@ActiveProfiles("ServiceTest")
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
    @MockBean
    public AclEditService aclEditService;

    @MockBean
    private AuthorIntegrationService authorIntegrationService;

    @MockBean
    private BookIntegrationService bookIntegrationService;

    @MockBean
    private GenreIntegrationService genreIntegrationService;

    @MockBean
    private CommentIntegrationService commentIntegrationService;

}
