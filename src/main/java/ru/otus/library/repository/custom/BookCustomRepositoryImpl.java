package ru.otus.library.repository.custom;

import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import ru.otus.library.model.Book;

@RequiredArgsConstructor
@Slf4j
public class BookCustomRepositoryImpl implements BookCustomRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public void removeAuthorById(String id) {
        val query = Query.query(Criteria.where("_id").is(id));
        val update = new Update().pull("authors", query);
        UpdateResult result = mongoTemplate.updateMulti(new Query(), update, Book.class);
        log.info("removed {} authors from books {}.",result.getModifiedCount(),result.getMatchedCount());

    }

    @Override
    public void removeGenreById(String id) {
        val query = Query.query(Criteria.where("_id").is(id));
        val update = new Update().pull("genres", query);
        UpdateResult result = mongoTemplate.updateMulti(new Query(), update, Book.class);
        log.info("removed {} genres from books {}.",result.getModifiedCount(),result.getMatchedCount());
    }
}
