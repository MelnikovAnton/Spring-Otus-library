package ru.otus.library.security;

import org.springframework.cache.ehcache.EhCacheFactoryBean;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.acls.AclPermissionCacheOptimizer;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.domain.*;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.AclCache;
import org.springframework.security.acls.model.AclService;
import org.springframework.security.acls.model.PermissionGrantingStrategy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;
import ru.otus.library.security.acl.dao.AclRepository;
import ru.otus.library.security.acl.mongodb.BasicLookupStrategy;
import ru.otus.library.security.acl.mongodb.MongoDBMutableAclService;

import java.util.Objects;

@Configuration
public class AclConfig {

    @Bean
    public AclCache aclCache() {
        return new EhCacheBasedAclCache(
                Objects.requireNonNull(aclEhCacheFactoryBean().getObject()),
                permissionGrantingStrategy(),
                aclAuthorizationStrategy()
        );
    }

    @Bean
    public EhCacheFactoryBean aclEhCacheFactoryBean() {
        EhCacheFactoryBean ehCacheFactoryBean = new EhCacheFactoryBean();
        ehCacheFactoryBean.setCacheManager(aclCacheManager().getObject());
        ehCacheFactoryBean.setCacheName("aclCache");
        return ehCacheFactoryBean;
    }

    @Bean
    public EhCacheManagerFactoryBean aclCacheManager() {
        return new EhCacheManagerFactoryBean();
    }

    @Bean
    public PermissionGrantingStrategy permissionGrantingStrategy() {
        return new DefaultPermissionGrantingStrategy(new ConsoleAuditLogger());
    }

    @Bean
    public AclAuthorizationStrategy aclAuthorizationStrategy() {
        return new AclAuthorizationStrategyImpl(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    @Bean
    public MethodSecurityExpressionHandler methodSecurityExpressionHandler(AclService aclService) {
        DefaultMethodSecurityExpressionHandler expressionHandler = new OAuth2MethodSecurityExpressionHandler();

        AclPermissionEvaluator permissionEvaluator = new AclPermissionEvaluator(aclService);
        expressionHandler.setPermissionEvaluator(permissionEvaluator);
        expressionHandler.setPermissionCacheOptimizer(new AclPermissionCacheOptimizer(aclService));
        return expressionHandler;
    }

    @Bean
    public LookupStrategy lookupStrategy(MongoTemplate mongoTemplate) {
        return new BasicLookupStrategy(mongoTemplate, aclCache(), aclAuthorizationStrategy(), permissionGrantingStrategy());
    }


    @Bean
    public AclService aclService(LookupStrategy lookupStrategy, AclRepository aclRepository) {
        return new MongoDBMutableAclService(aclRepository, lookupStrategy, aclCache());
    }


//    @Bean
//    public LookupStrategy lookupStrategy() {
//        return new BasicLookupStrategy(dataSource, aclCache(), aclAuthorizationStrategy(), new ConsoleAuditLogger());
//    }

//    @Bean
//    public AclService aclService() {
//        return new JdbcMutableAclService(dataSource, lookupStrategy(), aclCache());
//    }


}
