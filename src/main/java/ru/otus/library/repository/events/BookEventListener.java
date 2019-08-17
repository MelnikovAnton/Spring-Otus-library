package ru.otus.library.repository.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterDeleteEvent;
import org.springframework.stereotype.Component;
import ru.otus.library.model.Book;
import ru.otus.library.repository.CommentRepository;


@Component
@RequiredArgsConstructor
@Slf4j
public class BookEventListener extends AbstractMongoEventListener<Book> {

    private final CommentRepository repository;

    @Override
    public void onAfterDelete(AfterDeleteEvent<Book> event) {
        super.onAfterDelete(event);
        val source = event.getSource();
        val bookId = source.get("id").toString();
        repository.deleteCommentsByBookId(bookId).subscribe(count -> log.info("deleted {} comments", count));

    }

}
