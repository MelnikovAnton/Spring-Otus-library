package ru.otus.library;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.CumulativePermission;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import ru.otus.library.model.Book;
import ru.otus.library.secutity.acl.domain.MongoSid;
import ru.otus.library.services.impl.BookServiceImpl;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
})
@EnableAuthorizationServer
@EnableResourceServer
public class LibraryApplication {


    public static void main(String[] args) throws SQLException {
        ConfigurableApplicationContext ctx = SpringApplication.run(LibraryApplication.class, args);
        PasswordEncoder encoder = ctx.getBean(PasswordEncoder.class);

        System.out.println(encoder);
        String p = ctx.getBean(PasswordEncoder.class).encode("secret");
        System.out.println("PASSWORD:" + p);

        AclService aclService = ctx.getBean(AclService.class);

        MongoTemplate template = ctx.getBean(MongoTemplate.class);
        Book book = template.findAll(Book.class).get(0);


ObjectIdentity oi = new ObjectIdentityImpl(book);
        System.out.println(oi);

        Acl acl = aclService.readAclById(oi);

        PrincipalSid adm = new PrincipalSid("admin");
        Sid owner = acl.getOwner();

        boolean isOwner = owner.equals(adm);

        List<AccessControlEntry> aces = acl.getEntries();
        
        boolean granted = acl.isGranted(List.of(BasePermission.READ), List.of(adm), false);
        System.out.println(granted);
    }

}
