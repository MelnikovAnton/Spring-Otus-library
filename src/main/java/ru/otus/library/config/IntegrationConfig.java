package ru.otus.library.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;

@Configuration
@RequiredArgsConstructor
public class IntegrationConfig {
    //Books Flow
    @Bean
    public IntegrationFlow saveBookFlow() {
        return IntegrationFlows.from("inBookChannel")
                .handle("bookRepository", "save")
                .handle("aclEditService", "createAclForBook")
                .channel("outBookChannel")
                .get();
    }

    @Bean
    public IntegrationFlow deleteBookFlow() {
        return IntegrationFlows.from("inDellBookChannel")
                .handle("aclEditService", "deleteAclForBook")
                .handle("bookRepository", "delete")
                .get();
    }

    //Author Flow

    @Bean
    public IntegrationFlow saveAuthorFlow() {
        return IntegrationFlows.from("inAuthorChannel")
                .handle("authorRepository", "save")
                .handle("aclEditService", "createAclForAuthor")
                .channel("outAuthorChannel")
                .get();
    }

    @Bean
    public IntegrationFlow deleteAuthorFlow() {
        return IntegrationFlows.from("inDellAuthorChannel")
                .handle("aclEditService", "deleteAclForAuthor")
                .handle("authorRepository", "delete")
                .get();
    }

    // Genre Flow
    @Bean
    public IntegrationFlow saveGenreFlow() {
        return IntegrationFlows.from("inGenreChannel")
                .handle("genreRepository", "save")
                .handle("aclEditService", "createAclForGenre")
                .channel("outGenreChannel")
                .get();
    }

    @Bean
    public IntegrationFlow deleteGenreFlow() {
        return IntegrationFlows.from("inDellGenreChannel")
                .handle("aclEditService", "deleteAclForGenre")
                .handle("genreRepository", "delete")
                .get();
    }

    // Comment Flow
    @Bean
    public IntegrationFlow saveCommentFlow() {
        return IntegrationFlows.from("inCommentChannel")
                .handle("commentRepository", "save")
                .handle("aclEditService", "createAclForComment")
                .channel("outCommentChannel")
                .get();
    }

    @Bean
    public IntegrationFlow deleteCommentFlow() {
        return IntegrationFlows.from("inDellCommentChannel")
                .handle("aclEditService", "deleteAclForComment")
                .handle("commentRepository", "delete")
                .get();
    }


    // BookChannels
    @Bean
    public DirectChannel inBookChannel() {
        return MessageChannels.direct().get();
    }

    @Bean
    public DirectChannel outBookChannel() {
        return MessageChannels.direct().get();

    }

    @Bean
    public DirectChannel inDellBookChannel() {
        return MessageChannels.direct().get();
    }

    //AuthorChannels
    @Bean
    public DirectChannel inAuthorChannel() {
        return MessageChannels.direct().get();
    }

    @Bean
    public DirectChannel outAuthorChannel() {
        return MessageChannels.direct().get();

    }

    @Bean
    public PublishSubscribeChannel inDellAuthorChannel() {
        return MessageChannels.publishSubscribe().get();
    }

    //Genre Channels
    @Bean
    public DirectChannel inGenreChannel() {
        return MessageChannels.direct().get();
    }

    @Bean
    public DirectChannel outGenreChannel() {
        return MessageChannels.direct().get();
    }

    @Bean
    public PublishSubscribeChannel inDellGenreChannel() {
        return MessageChannels.publishSubscribe().get();
    }

    //Comments Channels
    @Bean
    public DirectChannel inCommentChannel() {
        return MessageChannels.direct().get();
    }

    @Bean
    public DirectChannel outCommentChannel() {
        return MessageChannels.direct().get();
    }

    @Bean
    public PublishSubscribeChannel inDellCommentChannel() {
        return MessageChannels.publishSubscribe().get();
    }

}
