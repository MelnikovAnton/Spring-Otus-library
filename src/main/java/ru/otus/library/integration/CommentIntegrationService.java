package ru.otus.library.integration;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.library.model.Comment;

@MessagingGateway
public interface CommentIntegrationService {

    @Gateway(requestChannel = "inCommentChannel", replyChannel = "outCommentChannel")
    Comment createComment(Comment comment);

    @Gateway(requestChannel = "inDellCommentChannel")
    void deleteComment(Comment comment);
}
