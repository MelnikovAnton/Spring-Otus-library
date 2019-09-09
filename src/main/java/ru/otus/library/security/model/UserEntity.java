package ru.otus.library.security.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import java.util.Set;

@Document
@Data
public class UserEntity {
    @Id
    private String id;
    private String username;
    private String password;
    private Set<Role> roles;

}
