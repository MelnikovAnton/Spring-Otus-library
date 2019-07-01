package ru.otus.library.shell;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.shell.table.BorderStyle;
import org.springframework.shell.table.Table;
import org.springframework.shell.table.TableBuilder;
import org.springframework.shell.table.TableModelBuilder;
import ru.otus.library.model.Book;
import ru.otus.library.model.Comment;
import ru.otus.library.services.BookService;
import ru.otus.library.services.CommentService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ShellComponent(value = "Comment shell commands")
@RequiredArgsConstructor
@ShellCommandGroup("Comment commands")
@Slf4j
public class CommentShellCommands {

    private final CommentService commentService;
    private final BookService bookService;


    @ShellMethod(value = "delete Comment", key = {"deleteComment", "delc"})
    private String deleteGenre(@ShellOption(value = {"-i", "--id"}) long id) {
        Optional<Comment> oComment = commentService.findById(id);
        if (oComment.isEmpty()) return "no comment with id " + id;
        long r = commentService.delete(oComment.get());
        return "Comment deleted id=" + r;
    }

    @ShellMethod(value = "add Comment", key = {"addComment", "addc"})
    private String addComment(@ShellOption(value = {"-b", "--book"}) long book_id,
                              @ShellOption(value = {"-c", "--comment"}) String comment) {

        Optional<Book> book = bookService.findById(book_id);
        if (book.isEmpty()) {
            log.warn("No book with id {}", book_id);
            return "No book with id " + book_id;
        }
        if (comment.isEmpty()) return "Pleas Enter Comment";

        Comment comm = new Comment(book.get(), comment);
        commentService.saveComment(comm);

        return "Comment added id=" + comm.getId();
    }

    @ShellMethod(value = "find Comments", key = {"findComments", "fc"})
    private Table findComments(@ShellOption(value = {"-i", "--id"}, defaultValue = "-1") long id,
                               @ShellOption(value = {"-b", "--book"}, defaultValue = "-1") long book_id) {

        List<Comment> comments = new ArrayList<>();
        if (id > 0) commentService.findById(id).ifPresent(comments::add);
        if (book_id > 0) {
            Optional<Book> book = bookService.findById(book_id);
            if (book.isEmpty()) {
                log.warn("No book with id {}", book_id);
                return getGenresTable(comments);
            }
            comments.addAll(commentService.findCommentsByBook(book.get()));
        }
        return getGenresTable(comments);
    }

    @ShellMethod(value = "find all Comments", key = {"findAllComments", "fca"})
    private Table findAllComments() {
        List<Comment> comments = commentService.findAll();
        return getGenresTable(comments);
    }

    private Table getGenresTable(List<Comment> comments) {
        TableModelBuilder<Object> modelBuilder = new TableModelBuilder<>();
        modelBuilder.addRow()
                .addValue("id")
                .addValue("Book")
                .addValue("Comment");
        comments.forEach(c -> modelBuilder.addRow()
                .addValue(c.getId())
                .addValue(c.getBook().getTitle())
                .addValue(c.getComment())
        );

        TableBuilder builder = new TableBuilder(modelBuilder.build());

        return builder.addFullBorder(BorderStyle.fancy_double).build();
    }
}


