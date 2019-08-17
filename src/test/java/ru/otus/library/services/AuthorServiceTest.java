package ru.otus.library.services;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.otus.library.model.Author;
import ru.otus.library.repository.AuthorRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("ServiceTest")
class AuthorServiceTest {

    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private AuthorService authorService;

    @Test
    void saveAuthor() {
        Author author = new Author("test");

        doAnswer(inv -> {
            Author a = inv.getArgument(0);
            a.setId("1");
            return Mono.just(a);
        }).when(authorRepository).save(any(Author.class));

        Mono<Author> authorMono = authorService.saveAuthor(author);

        StepVerifier
                .create(authorMono)
                .expectNextMatches(a -> "1".equals(a.getId()))
                .expectComplete()
                .verify();
    }

    @Test
    void findAuthorsByName() {
        when(authorRepository.findByNameContainingIgnoreCase(anyString()))
                .thenReturn(Flux.fromIterable(getTestAuthors()));
        Flux<Author> authorFlux = authorService.findAuthorsByName("test");

        StepVerifier
                .create(authorFlux)
                .expectNextCount(3)
                .verifyComplete();
    }


    @Test
    @DisplayName("Поиск по ID")
    void findById() {
        when(authorRepository.findById(anyString())).thenReturn(Mono.just(new Author("test")));
        Mono<Author> authorMono = authorService.findById("1");
        StepVerifier
                .create(authorMono)
                .expectNextMatches(a -> "test".equals(a.getName()))
                .verifyComplete();
    }


    @Test
    void delete() {
        doAnswer(invocation -> {
            String a = invocation.getArgument(0);
            assertEquals("test", a);
            return Mono.just(Void.TYPE);
        }).when(authorRepository).deleteById(anyString());

        authorService.delete("test");
    }

    @Test
    void findAll() {
        when(authorRepository.findAll()).thenReturn(Flux.fromIterable(getTestAuthors()));

        Flux<Author> authorFlux = authorService.findAll();

        StepVerifier
                .create(authorFlux)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    void findByBookId() {
        when(authorRepository.findByBookId(anyString())).thenReturn(Flux.fromIterable(getTestAuthors()));

        Flux<Author> authorFlux = authorService.findByBookId("1");

        StepVerifier
                .create(authorFlux)
                .expectNextCount(3)
                .verifyComplete();
    }

    private List<Author> getTestAuthors() {
        return List.of(new Author("Auth1"),
                new Author("Auth2"),
                new Author("Auth3"));
    }
}