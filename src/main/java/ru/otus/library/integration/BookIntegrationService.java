package ru.otus.library.integration;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.library.model.Book;

@MessagingGateway
public interface BookIntegrationService {

    @Gateway(requestChannel = "inBookChannel", replyChannel = "outBookChannel")
    Book createBook(Book book);

    @Gateway(requestChannel = "inDellBookChannel")
    void deleteBook(Book book);
}
