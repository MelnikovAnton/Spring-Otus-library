package ru.otus.library.integration;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.library.model.Genre;

@MessagingGateway
public interface GenreIntegrationService {

    @Gateway(requestChannel = "inGenreChannel", replyChannel = "outGenreChannel")
    Genre createGenre(Genre genre);

    @Gateway(requestChannel = "inDellGenreChannel")
    void deleteGenre(Genre genre);
}
