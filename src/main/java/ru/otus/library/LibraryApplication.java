package ru.otus.library;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.acls.domain.AccessControlEntryImpl;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import ru.otus.library.model.Book;
import ru.otus.library.secutity.acl.MongoMutableAclService;
import ru.otus.library.secutity.acl2.mongodb.MongoDBMutableAclService;

import java.sql.SQLException;
import java.util.List;

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

        MongoDBMutableAclService aclService = ctx.getBean(MongoDBMutableAclService.class);

        MongoTemplate template = ctx.getBean(MongoTemplate.class);
        Book book = template.findAll(Book.class).get(0);


ObjectIdentity oid = new ObjectIdentityImpl(book);
        System.out.println(oid);

        PrincipalSid admin = new PrincipalSid("admin");

        AuthenticationManager authManager = ctx.getBean(AuthenticationManager.class);

        UsernamePasswordAuthenticationToken authReq
                = new UsernamePasswordAuthenticationToken("admin", "password");
        Authentication auth = authManager.authenticate(authReq);
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);

        MutableAcl acl = aclService.createAcl(oid);
        acl.setOwner(admin);


        acl.insertAce(acl.getEntries().size(), BasePermission.READ, admin, true);
        acl.insertAce(acl.getEntries().size(), BasePermission.ADMINISTRATION, admin, true);
// обновить ACL в БД aclService.updateAcl(acl);

        aclService.updateAcl(acl);

//        PrincipalSid adm = new PrincipalSid("admin");
//        Sid owner = acl.getOwner();
//
//        boolean isOwner = owner.equals(adm);
//
//        List<AccessControlEntry> aces = acl.getEntries();
//
//        boolean granted = acl.isGranted(List.of(BasePermission.READ), List.of(adm), false);
//        System.out.println(granted);
    }

}
