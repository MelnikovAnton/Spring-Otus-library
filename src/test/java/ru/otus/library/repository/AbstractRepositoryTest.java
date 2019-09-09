package ru.otus.library.repository;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerEndpointsConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerSecurityConfiguration;
import org.springframework.test.annotation.DirtiesContext;

@DataMongoTest
@EnableConfigurationProperties
@ComponentScan({"ru.otus.library.config", "ru.otus.library.repository"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public abstract class AbstractRepositoryTest {
    @MockBean
    public AuthorizationServerEndpointsConfiguration authorizationServerEndpointsConfiguration;
    @MockBean
    public ObjectPostProcessor objectPostProcessor;
    @MockBean
    public AuthorizationServerSecurityConfiguration authorizationServerSecurityConfiguration;
    @MockBean
    public AuthenticationConfiguration authenticationConfiguration;
}
