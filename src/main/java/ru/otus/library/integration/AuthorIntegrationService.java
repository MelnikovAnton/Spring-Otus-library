package ru.otus.library.integration;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.library.model.Author;

@MessagingGateway
public interface AuthorIntegrationService {

    @Gateway(requestChannel = "inAuthorChannel", replyChannel = "outAuthorChannel")
    Author createAuthor(Author author);

    @Gateway(requestChannel = "inDellAuthorChannel")
    void deleteAuthor(Author author);
}
