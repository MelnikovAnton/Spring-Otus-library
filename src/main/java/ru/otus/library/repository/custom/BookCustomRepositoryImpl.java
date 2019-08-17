package ru.otus.library.repository.custom;

import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import reactor.core.publisher.Mono;
import ru.otus.library.model.Author;
import ru.otus.library.model.Book;
import ru.otus.library.model.Genre;

@RequiredArgsConstructor
@Slf4j
public class BookCustomRepositoryImpl implements BookCustomRepository {

    private final ReactiveMongoTemplate mongoTemplate;

    @Override
    public void removeAuthorById(String id) {
        val query = Query.query(Criteria.where("_id").is(id));
        val update = new Update().pull("authors", query);
        Mono<UpdateResult> updateResult = mongoTemplate.updateMulti(new Query(), update, Book.class);
        updateResult.subscribe(result ->
                log.info("removed {} authors from books {}.", result.getModifiedCount(), result.getMatchedCount()));
    }

    @Override
    public void removeGenreById(String id) {
        val query = Query.query(Criteria.where("_id").is(id));
        val update = new Update().pull("genres", query);
        Mono<UpdateResult> updateResult = mongoTemplate.updateMulti(new Query(), update, Book.class);
        updateResult.subscribe(result ->
                log.info("removed {} genres from books {}.", result.getModifiedCount(), result.getMatchedCount()));
    }

    @Override
    public void updateAuthorName(Author author) {
        val query = Query.query(Criteria.where("authors").elemMatch(Criteria.where("_id").is(author.getId())));
        val update = new Update().set("authors.$.name", author.getName());
        Mono<UpdateResult> updateResult = mongoTemplate.updateMulti(query, update, Book.class);
        updateResult.subscribe(result ->
                log.info("Updated {} authors from books {}.", result.getModifiedCount(), result.getMatchedCount()));
    }

    @Override
    public void updateGenreName(Genre genre) {
        val query = Query.query(Criteria.where("genres").elemMatch(Criteria.where("_id").is(genre.getId())));
        val update = new Update().set("genres.$.name", genre.getName());
        Mono<UpdateResult> updateResult = mongoTemplate.updateMulti(query, update, Book.class);
        updateResult.subscribe(result ->
                log.info("Updated {} genres from books {}.", result.getModifiedCount(), result.getMatchedCount()));
    }

}
