package ru.otus.library.secutity.acl.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.ObjectIdentity;
import ru.otus.library.secutity.acl.domain.MongoAclImpl;
import ru.otus.library.util.CustomLog;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MyAclRepository extends MongoRepository<MongoAclImpl, String> {

    Optional<MongoAclImpl> findById(Serializable id);

    List<MongoAclImpl> findByParentAcl(Acl parent);

    void deleteById(Serializable id);

    List<MongoAclImpl> findByObjectIdentity (ObjectIdentity oid);

    List<MongoAclImpl> findByParentAclObjectIdentity (ObjectIdentity oid);

    MongoAclImpl findFirstByObjectIdentity(ObjectIdentity objectIdentity);

    List<MongoAclImpl> findByObjectIdentityIn(Collection<ObjectIdentity> oids);
}
