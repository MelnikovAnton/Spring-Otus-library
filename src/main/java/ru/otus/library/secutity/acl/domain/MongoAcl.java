package ru.otus.library.secutity.acl.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Document
@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class MongoAcl {

    @Id
    private String id;

    private String className;

    private Serializable instanceId;

    private MongoSid owner;

    @Indexed
    private Serializable parentId = null;

    private boolean inheritPermissions = false;

    private List<ObjectPermission> permissions = new ArrayList<>();

}
