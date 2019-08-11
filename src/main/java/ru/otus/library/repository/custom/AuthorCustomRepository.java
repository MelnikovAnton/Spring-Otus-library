package ru.otus.library.repository.custom;

import reactor.core.publisher.Flux;
import ru.otus.library.model.Author;

public interface AuthorCustomRepository {

    Flux<Author> findByBookId(String BookId);
}
