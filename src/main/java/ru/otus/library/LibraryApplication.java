package ru.otus.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import java.sql.SQLException;
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

    }

}
