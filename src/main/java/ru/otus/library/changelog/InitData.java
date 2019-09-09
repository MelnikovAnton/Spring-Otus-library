package ru.otus.library.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.otus.library.model.Author;
import ru.otus.library.model.Book;
import ru.otus.library.model.Comment;
import ru.otus.library.model.Genre;
import ru.otus.library.security.model.Role;
import ru.otus.library.security.model.UserEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@ChangeLog(order = "001")
public class InitData {

    @Autowired
    PasswordEncoder passwordEncoder;

    private final List<Genre> genres = new ArrayList<>();
    private final List<Author> authors = new ArrayList<>();

    @ChangeSet(order = "000", id = "dropDB", author = "MelnikovAnton", runAlways = true)
    public void dropDB(MongoDatabase database) {
        database.drop();
    }


    @ChangeSet(order = "001", id = "initAuthors", author = "MelnikovAnton", runAlways = true)
    public void initAuthors(MongoTemplate template) {
        getAuthors().forEach(a -> authors.add(template.save(a)));
    }

    @ChangeSet(order = "002", id = "initGenres", author = "MelnikovAnton", runAlways = true)
    public void initGenres(MongoTemplate template) {
        getGenres().forEach(g -> genres.add(template.save(g)));
    }

    @ChangeSet(order = "003", id = "initBooks", author = "MelnikovAnton", runAlways = true)
    public void initBooks(MongoTemplate template) {
        getBooks().forEach(template::save);
    }

    @ChangeSet(order = "004", id = "initComments", author = "MelnikovAnton", runAlways = true)
    public void initComments(MongoTemplate template) {
        List<Book> books = template.findAll(Book.class);
        books.forEach(b -> template.save(new Comment(b, "comment")));
        books.forEach(b -> template.save(new Comment(b, "comment2")));
        books.forEach(b -> template.save(new Comment(b, "comment3")));
    }

    @ChangeSet(order = "005", id = "initUsers", author = "MelnikovAnton", runAlways = true)
    public void initUsers(MongoTemplate template) {
        UserEntity admin = new UserEntity();
        admin.setUsername("admin");
        admin.setPassword("$2a$10$kBqoMjEtLzYCAlQGAlVTnekUmSwOkuUtMCoanS1BwHyVhLxJXqPLi");
        admin.setRoles(Set.of(Role.ROLE_ADMIN));

        template.save(admin);

        UserEntity user = new UserEntity();
        user.setUsername("user");
        user.setPassword("$2a$10$kBqoMjEtLzYCAlQGAlVTnekUmSwOkuUtMCoanS1BwHyVhLxJXqPLi");
        user.setRoles(Set.of(Role.ROLE_USER));

        template.save(user);
    }


    private List<Author> getAuthors() {
        return List.of(new Author("Author1"),
                new Author("Author2"),
                new Author("Author3"));
    }

    private List<Genre> getGenres() {
        return List.of(new Genre("Genre1"),
                new Genre("Genre2"),
                new Genre("Genre3"));
    }


    private List<Book> getBooks() {
        Book book1 = new Book("Book1", "content");
        book1.addAuthor(authors.get(0));
        book1.addGenre(genres.get(0));

        Book book2 = new Book("Book2", "content");
        book2.addAuthor(authors.get(0));
        book2.addAuthor(authors.get(1));
        book2.addGenre(genres.get(0));
        book2.addGenre(genres.get(1));

        Book book3 = new Book("Book3", "content");
        book3.addAuthor(authors.get(0));
        book3.addAuthor(authors.get(1));
        book3.addAuthor(authors.get(2));
        book3.addGenre(genres.get(0));
        book3.addGenre(genres.get(1));
        book3.addGenre(genres.get(2));

        return List.of(book1, book2, book3);
    }
}
