package ru.otus.library.repository.events;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterDeleteEvent;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.stereotype.Component;
import ru.otus.library.model.Genre;
import ru.otus.library.repository.BookRepository;

@Component
@RequiredArgsConstructor
public class GenreCascadeDeleteEventListener extends AbstractMongoEventListener<Genre> {

    private final BookRepository repository;

    @Override
    public void onAfterDelete(AfterDeleteEvent<Genre> event) {
        super.onAfterDelete(event);
        val source = event.getSource();
        val id = source.get("_id").toString();
        repository.removeGenreById(id);
    }

    @Override
    public void onAfterSave(AfterSaveEvent<Genre> event) {
        super.onAfterSave(event);
        val source = event.getSource();
        repository.updateGenreName(source);
    }
}
