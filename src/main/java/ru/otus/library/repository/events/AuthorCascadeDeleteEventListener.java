package ru.otus.library.repository.events;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterDeleteEvent;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.stereotype.Component;
import ru.otus.library.model.Author;
import ru.otus.library.repository.BookRepository;

@Component
@RequiredArgsConstructor
public class AuthorCascadeDeleteEventListener extends AbstractMongoEventListener<Author> {

    private final BookRepository repository;

    @Override
    public void onAfterDelete(AfterDeleteEvent<Author> event) {
        super.onAfterDelete(event);
        val source = event.getSource();
        val id = source.get("_id").toString();
        repository.removeAuthorById(id);
    }

    @Override
    public void onAfterSave(AfterSaveEvent<Author> event) {
        super.onAfterSave(event);
        val source = event.getSource();
        repository.updateAuthorName(source);
    }
}
