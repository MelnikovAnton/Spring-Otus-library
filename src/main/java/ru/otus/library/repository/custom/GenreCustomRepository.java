package ru.otus.library.repository.custom;

import reactor.core.publisher.Flux;
import ru.otus.library.model.Genre;

import java.util.List;

public interface GenreCustomRepository {

    Flux<Genre> findByBookId(String book_id);
}
