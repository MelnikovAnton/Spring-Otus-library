package ru.otus.library.actuator;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import ru.otus.library.model.Book;
import ru.otus.library.model.Comment;
import ru.otus.library.repository.BookRepository;
import ru.otus.library.repository.CommentRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class LibraryHealthIndicator implements HealthIndicator {

    private final BookRepository bookRepository;
    private final CommentRepository commentRepository;

    @Override
    public Health health() {
        List<Book> books = bookRepository.findAll();
        if (books.isEmpty()) return Health.down()
                .withDetail("message", "No Books in Repository")
                .build();
        Map<String, Object> details = new HashMap<>();
        details.put("Total books", books.size());
        details.put("Books has no Author", books.stream()
                .filter(b -> b.getAuthors().isEmpty())
                .collect(Collectors.toList()));
        details.put("Books has no Genre", books.stream()
                .filter(b -> b.getGenres().isEmpty())
                .collect(Collectors.toList()));
        details.put("Books has no Comments", books.stream()
                .filter(b -> commentRepository.findByBookId(b.getId()).isEmpty())
                .collect(Collectors.toList()));


        return Health.up()
                .withDetails(details)
                .build();
    }
}
