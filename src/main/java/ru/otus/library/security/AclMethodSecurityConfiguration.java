package ru.otus.library.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class AclMethodSecurityConfiguration extends GlobalMethodSecurityConfiguration {
    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired
    MethodSecurityExpressionHandler oAuth2MethodSecurityExpressionHandler;

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        return oAuth2MethodSecurityExpressionHandler;
    }

}
