package ru.otus.library.secutity.acl.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.library.secutity.acl.domain.MongoAcl;
import ru.otus.library.util.CustomLog;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface AclRepository extends MongoRepository<MongoAcl, Serializable> {
    @CustomLog
    Optional<MongoAcl> findById(Serializable id);

    @CustomLog
    List<MongoAcl> findByInstanceIdAndClassName(Serializable instanceId, String className);
    @CustomLog
    List<MongoAcl> findByParentId(Serializable parentId);
    @CustomLog
    Long deleteByInstanceId(Serializable instanceId);
}
