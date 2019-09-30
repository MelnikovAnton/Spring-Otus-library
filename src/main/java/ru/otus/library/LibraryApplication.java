package ru.otus.library;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import ru.otus.library.model.Author;
import ru.otus.library.model.Book;
import ru.otus.library.model.Comment;
import ru.otus.library.model.Genre;
import ru.otus.library.security.model.UserEntity;
import ru.otus.library.security.util.AclEditService;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication(/*exclude = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
}*/)
@EnableAuthorizationServer
@EnableResourceServer
@EnableIntegration
@IntegrationComponentScan
public class LibraryApplication {


    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(LibraryApplication.class, args);

        PasswordEncoder encoder = ctx.getBean(PasswordEncoder.class);
        AuthenticationManager authManager = ctx.getBean(AuthenticationManager.class);

        AclEditService util = ctx.getBean(AclEditService.class);

        MongoTemplate template = ctx.getBean(MongoTemplate.class);
        upgradePasswords(template, encoder);

        System.out.println(encoder.encode("password"));
        manualAuthenticate(authManager, encoder);

        List<ObjectIdentity> oids = getAllObjects(template);
        oids.forEach(util::createDefaultAcl);


    }

    private static List<ObjectIdentity> getAllObjects(MongoTemplate template) {
        List<ObjectIdentity> objects = template.findAll(Book.class)
                .stream()
                .map(ObjectIdentityImpl::new)
                .collect(Collectors.toList());
        objects.addAll(template.findAll(Author.class)
                .stream()
                .map(ObjectIdentityImpl::new)
                .collect(Collectors.toList()));
        objects.addAll(template.findAll(Genre.class)
                .stream()
                .map(ObjectIdentityImpl::new)
                .collect(Collectors.toList()));
        objects.addAll(template.findAll(Comment.class)
                .stream()
                .map(ObjectIdentityImpl::new)
                .collect(Collectors.toList()));

        return objects;
    }

    private static void manualAuthenticate(AuthenticationManager authManager, PasswordEncoder encoder) {
        UsernamePasswordAuthenticationToken authReq
                = new UsernamePasswordAuthenticationToken("admin", "password");
        Authentication auth = authManager.authenticate(authReq);
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);
    }

    private static void upgradePasswords(MongoTemplate template, PasswordEncoder encoder) {
        List<UserEntity> users = template.findAll(UserEntity.class);
        users.forEach(user -> {
            user.setPassword(encoder.encode("password"));
            template.save(user);
        });

    }

}
