package ru.otus.library.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.messaging.MessageChannel;

@Configuration
public class IntegrationConfig {

    @Bean
    public IntegrationFlow saveBookFlow() {
        return IntegrationFlows.from("itemChannel")
                .handle("testService", "test")
                .channel("outBookChannel")
                .get();
    }

    @Bean
    public MessageChannel inBookChannel() {
        return MessageChannels.direct().get();
    }

    @Bean
    public PublishSubscribeChannel outBookChannel() {
        return MessageChannels.publishSubscribe().get();
    }
}
