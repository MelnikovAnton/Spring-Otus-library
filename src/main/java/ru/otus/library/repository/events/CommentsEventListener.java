package ru.otus.library.repository.events;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.stereotype.Component;
import ru.otus.library.model.Book;
import ru.otus.library.model.Comment;
import ru.otus.library.repository.BookRepository;

@Component
@RequiredArgsConstructor
public class CommentsEventListener extends AbstractMongoEventListener<Comment> {

    private final BookRepository bookRepository;

    @Override
    public void onBeforeSave(BeforeSaveEvent<Comment> event) {
        val comment = event.getSource();
        Book book = comment.getBook();
        String bookId = (book == null) ? "WrongID To throw exception" : book.getId();
        bookRepository.existsById(bookId).subscribe(isExists -> {
            if (!isExists) throw new RuntimeException("No Book With ID " + bookId);
            else super.onBeforeSave(event);
        });
    }
}
