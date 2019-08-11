package ru.otus.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import reactor.core.publisher.Mono;
import ru.otus.library.model.Author;
import ru.otus.library.services.AuthorService;

@SpringBootApplication
public class LibraryApplication {


    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(LibraryApplication.class, args);
        Mono<Author> author = ctx.getBean(AuthorService.class).saveAuthor(new Author("Test"));

        System.out.println("!!!!!!!!!!!!!!!!!!!!");
        System.out.println(author.block());
    }

}
