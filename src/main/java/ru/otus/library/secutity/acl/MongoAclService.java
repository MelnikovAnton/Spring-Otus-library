package ru.otus.library.secutity.acl;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.acls.jdbc.LookupStrategy;
import org.springframework.security.acls.model.*;
import ru.otus.library.secutity.acl.domain.MongoAclImpl;
import ru.otus.library.secutity.acl.repository.MyAclRepository;

import java.util.*;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Slf4j
@EqualsAndHashCode
@ToString
public class MongoAclService implements AclService {

    protected final MyAclRepository aclRepository;

    private final LookupStrategy lookupStrategy;


    @Override
    public List<ObjectIdentity> findChildren(ObjectIdentity objectIdentity) {
        return aclRepository.findByParentAclObjectIdentity(objectIdentity)
                .stream()
                .map(MongoAclImpl::getObjectIdentity)
                .collect(Collectors.toList());
    }

    @Override
    public Acl readAclById(ObjectIdentity objectIdentity) throws NotFoundException {
        return readAclById(objectIdentity, null);
    }

    @Override
    public Acl readAclById(ObjectIdentity objectIdentity, List<Sid> sids) throws NotFoundException {
        Map<ObjectIdentity, Acl> map = readAclsById(Collections.singletonList(objectIdentity), sids);
        return map.get(objectIdentity);
    }

    @Override
    public Map<ObjectIdentity, Acl> readAclsById(List<ObjectIdentity> list) throws NotFoundException {
        return readAclsById(list, null);
    }

    @Override
    public Map<ObjectIdentity, Acl> readAclsById(List<ObjectIdentity> objects, List<Sid> sids) throws NotFoundException {
        Map<ObjectIdentity, Acl> result = lookupStrategy.readAclsById(objects, sids);

        for (ObjectIdentity oid : objects) {
            if (!result.containsKey(oid)) {
                throw new NotFoundException(
                        "Unable to find ACL information for object identity '" + oid
                                + "'");
            }
        }

        return result;
    }
}
