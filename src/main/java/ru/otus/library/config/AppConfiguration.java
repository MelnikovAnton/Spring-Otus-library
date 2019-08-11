package ru.otus.library.config;

import com.github.cloudyrock.mongock.Mongock;
import com.github.cloudyrock.mongock.SpringMongockBuilder;
import com.mongodb.MongoClientURI;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.otus.library.model.Author;
import ru.otus.library.model.Book;
import ru.otus.library.model.Comment;
import ru.otus.library.model.Genre;
import ru.otus.library.services.AuthorService;
import ru.otus.library.services.BookService;
import ru.otus.library.services.CommentService;
import ru.otus.library.services.GenreService;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Configuration
public class AppConfiguration {

    private static final String CHANGELOGS_PACKAGE = "ru.otus.library.changelog";

    @Bean
    public Mongock mongock(MongoProps mongoProps) {
        com.mongodb.MongoClient mongoClient = new com.mongodb.MongoClient(new MongoClientURI(mongoProps.getUri()));
        return new SpringMongockBuilder(mongoClient, mongoProps.getDatabase(), CHANGELOGS_PACKAGE)
                .build();
    }

    @Bean
    public MongoClient mongoClient() {
        return MongoClients.create();
    }


    @Bean("BookRestController")
    public RouterFunction<ServerResponse> bookRestEndpoint(BookService bookService) {
        return route()
                .GET("/books", request -> ok()
                        .body(bookService.findAll(), Book.class))
                .GET("/books/{id}", request -> ok()
                        .body(bookService.findById(request.pathVariable("id")), Book.class))
                .POST("/books", request -> ok()
                        .body(request.bodyToMono(Book.class)
                                .flatMap(bookService::saveBook), Book.class))
                .PUT("/books/{id}", request -> ok()
                        .body(request.bodyToMono(Book.class)
                                .flatMap(book -> {
                                    book.setId(request.pathVariable("id"));
                                    return Mono.just(book);
                                })
                                .flatMap(bookService::saveBook), Book.class))
                .DELETE("/books/{id}", request -> ok()
                        .body(bookService.delete(request.pathVariable("id")), Void.class))
                .build();
    }

    @Bean("CommentsRestController")
    public RouterFunction<ServerResponse> commentsRestEndpoint(CommentService commentService) {
        return route()
                .GET("/comments", request -> ok()
                        .body(commentService.findAll(), Comment.class))
                .GET("/comments/{id}", request -> ok()
                        .body(commentService.findCommentsByBook(request.pathVariable("id")), Comment.class))
                .POST("/comments", request -> ok()
                        .body(request.bodyToMono(Comment.class)
                                .flatMap(commentService::saveComment), Comment.class))
                .PUT("/comments/{id}", request -> ok()
                        .body(request.bodyToMono(Comment.class)
                                .flatMap(comment -> {
                                    comment.setId(request.pathVariable("id"));
                                    return Mono.just(comment);
                                })
                                .flatMap(commentService::saveComment), Comment.class))
                .DELETE("/comments/{id}", request -> ok()
                        .body(commentService.delete(request.pathVariable("id")), Void.class))
                .build();
    }

    @Bean("AuthorRestController")
    public RouterFunction<ServerResponse> authorsRestEndpoint(AuthorService authorService) {
        return route()
                .GET("/authors", request -> ok()
                        .body(authorService.findAll(), Author.class))
                .GET("/authors/{id}", request -> ok()
                        .body(authorService.findById(request.pathVariable("id")), Author.class))
                .POST("/authors", request -> ok()
                        .body(request.bodyToMono(Author.class)
                                .flatMap(authorService::saveAuthor), Author.class))
                .PUT("/authors/{id}", request -> ok()
                        .body(request.bodyToMono(Author.class)
                                .flatMap(author -> {
                                    author.setId(request.pathVariable("id"));
                                    return Mono.just(author);
                                })
                                .flatMap(authorService::saveAuthor), Author.class))
                .DELETE("/authors/{id}", request -> ok()
                        .body(authorService.delete(request.pathVariable("id")), Void.class))
                .build();
    }


    @Bean("GenreRestController")
    public RouterFunction<ServerResponse> genresRestEndpoint(GenreService genreService) {
        return route()
                .GET("/genres", request -> ok()
                        .body(genreService.findAll(), Genre.class))
                .GET("/genres/{id}", request -> ok()
                        .body(genreService.findById(request.pathVariable("id")), Genre.class))
                .POST("/genres", request -> ok()
                        .body(request.bodyToMono(Genre.class)
                                .flatMap(genreService::saveGenre), Genre.class))
                .PUT("/genres/{id}", request -> ok()
                        .body(request.bodyToMono(Genre.class)
                                .flatMap(genre -> {
                                    genre.setId(request.pathVariable("id"));
                                    return Mono.just(genre);
                                })
                                .flatMap(genreService::saveGenre), Genre.class))
                .DELETE("/genres/{id}", request -> ok()
                        .body(genreService.delete(request.pathVariable("id")), Void.class))
                .build();
    }

}
