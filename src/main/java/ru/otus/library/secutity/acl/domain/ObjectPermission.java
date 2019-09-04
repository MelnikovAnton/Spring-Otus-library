package ru.otus.library.secutity.acl.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

@Data
public class ObjectPermission {

    @Id
    private String id;

    private final MongoSid sid;

    private int permission;

    private final boolean granting;

    private boolean auditFailure;

    private boolean auditSuccess;
}
