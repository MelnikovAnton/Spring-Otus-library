package ru.otus.library.secutity.acl.domain;

import lombok.Data;

@Data
public class MongoSid {

    private String name;
    private boolean isPrincipal;
}
