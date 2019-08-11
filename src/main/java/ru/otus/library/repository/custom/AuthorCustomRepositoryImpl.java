package ru.otus.library.repository.custom;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import reactor.core.publisher.Flux;
import ru.otus.library.model.Author;
import ru.otus.library.model.Book;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@RequiredArgsConstructor
public class AuthorCustomRepositoryImpl implements AuthorCustomRepository {

    private final ReactiveMongoTemplate mongoTemplate;

    @Override
    public Flux<Author> findByBookId(String bookId) {
        val aggregation = newAggregation(
                match(Criteria.where("id").is(bookId))
                , unwind("authors")
                , project().andExclude("_id").and("authors.id").as("_id").and("authors.name").as("name")
        );
        return mongoTemplate.aggregate(aggregation, Book.class, Author.class);
    }
}
