package ru.otus.library.repository.custom;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import ru.otus.library.model.Book;
import ru.otus.library.model.Genre;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@RequiredArgsConstructor
public class GenreCustomRepositoryImpl implements GenreCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public List<Genre> findByBookId(String book_id) {
        val aggregation = newAggregation(
                match(Criteria.where("id").is(book_id))
                , unwind("genres")
                , project().andExclude("_id").and("genres.id").as("_id").and("genres.name").as("name")
        );
        return mongoTemplate.aggregate(aggregation, Book.class, Genre.class).getMappedResults();
    }
}
