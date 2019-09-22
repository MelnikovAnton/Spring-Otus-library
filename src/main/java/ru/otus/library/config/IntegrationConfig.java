package ru.otus.library.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.Channels;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import ru.otus.library.security.util.AclCreationUtil;

@Configuration
@RequiredArgsConstructor
public class IntegrationConfig {

    @Bean
    public IntegrationFlow saveBookFlow() {
        return IntegrationFlows.from("inBookChannel")
                .handle("bookRepository", "save")
                .handle("aclCreationUtil", "createAclForBook")
                .channel("outBookChannel")
                .get();
    }

    @Bean
    public IntegrationFlow deleteBookFlow() {
        return IntegrationFlows.from("inDellBookChannel")
                .handle("aclCreationUtil", "deleteAclForBook")
                .handle("bookRepository", "delete")
//                .channel("addBook.out")
                .get();
    }

    @Bean
    public DirectChannel inBookChannel() {
        DirectChannel inBookChannel = new DirectChannel();
        return inBookChannel;
    }

    @Bean
    public DirectChannel outBookChannel() {
        DirectChannel outBookChannel = MessageChannels.direct().get();
        return outBookChannel;
    }

    @Bean
    public DirectChannel inDellBookChannel() {
       return MessageChannels.direct().get();
    }
}
