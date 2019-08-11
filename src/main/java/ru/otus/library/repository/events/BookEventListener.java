package ru.otus.library.repository.events;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterDeleteEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.otus.library.model.Book;
import ru.otus.library.repository.CommentRepository;


@Component
@RequiredArgsConstructor
public class BookEventListener extends AbstractMongoEventListener<Mono<Book>> {

    private final CommentRepository repository;

    @Override
    public void onAfterDelete(AfterDeleteEvent<Mono<Book>> event) {
        super.onAfterDelete(event);
        val source = event.getSource();
        val bookId = source.get("_id").toString();
        repository.deleteCommentsByBookId(bookId);

    }

}
