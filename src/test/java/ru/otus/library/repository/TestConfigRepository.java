package ru.otus.library.repository;

import com.github.cloudyrock.mongock.Mongock;
import com.github.cloudyrock.mongock.SpringMongockBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.library.config.MongoProps;


//@Configuration
public class TestConfigRepository {

    private static final String CHANGELOGS_PACKAGE = "ru.otus.library.changelog";
//
//    @Bean
//    public Mongock mongock(com.mongodb.MongoClient mongoClient) {
//        //      com.mongodb.MongoClient mongoClient = new com.mongodb.MongoClient(new MongoClientURI(mongoProps.getUri()));
//        return new SpringMongockBuilder(mongoClient, "library", CHANGELOGS_PACKAGE)
//                .build();
//    }

}
